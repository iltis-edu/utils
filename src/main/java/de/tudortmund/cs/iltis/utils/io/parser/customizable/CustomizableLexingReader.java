package de.tudortmund.cs.iltis.utils.io.parser.customizable;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.collections.Pair;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFault;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultCollection;
import de.tudortmund.cs.iltis.utils.io.parser.general.AbstractParser;
import de.tudortmund.cs.iltis.utils.io.parser.general.GeneralParsingFaultReason;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsableSymbol;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsingCreator;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsingReader;
import de.tudortmund.cs.iltis.utils.io.parser.general.SymbolToken;
import de.tudortmund.cs.iltis.utils.io.parser.general.TokenUtils;
import de.tudortmund.cs.iltis.utils.io.parser.parentheses.ParenthesesChecker;
import de.tudortmund.cs.iltis.utils.io.parser.symbol.SymbolSplittingPolicy;
import de.tudortmund.cs.iltis.utils.io.parser.symbol.SymbolsNotSplittableException;
import java.util.*;
import java.util.function.Function;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Triple;

/**
 * An extension of {@link ParsingReader} that uses a {@link CustomizableLexer} to lex the input.
 *
 * @param <ParserOutputT> the type the parsing process returns
 * @param <ReaderOutputT> the type the {@link #read(Object)}-method returns
 * @param <ParserT> the type of parser used for parsing
 */
public abstract class CustomizableLexingReader<
                ParserOutputT, ReaderOutputT, ParserT extends AbstractParser>
        extends ParsingReader<ParserOutputT, ReaderOutputT, ParserT> {

    /**
     * Determines, if the parser should bail out, when encountering an unexpected error. Is used in
     * {@link #parseInput(Object, ParsingCreator)}. Default value is true.
     */
    protected boolean isBailOutInLexer = true;

    /** Properties used in the customizable lexer. */
    protected CustomizableLexingPropertiesProvidable properties;

    /**
     * @see #registerPostLexConverter(Function)
     */
    protected List<Function<Token, Triple<List<Token>, List<ParsingFault>, Boolean>>>
            postLexConverters = new ArrayList<>();

    /**
     * Creates a {@link CustomizableLexingReader} <b>without</b> testing for imbalanced or
     * disallowed parentheses.
     *
     * @param properties the properties for the {@link CustomizableLexer}; are cloned
     */
    public CustomizableLexingReader(CustomizableLexingPropertiesProvidable properties) {
        super();
        Objects.requireNonNull(properties, "properties may not be null");
        this.properties = properties.clone();
        init();
    }

    /**
     * Creates a {@link CustomizableLexingReader} with the given parentheses checker.
     *
     * @param properties the properties for the {@link CustomizableLexer}; are cloned
     * @param parenChecker the parentheses checker to use
     */
    public CustomizableLexingReader(
            CustomizableLexingPropertiesProvidable properties, ParenthesesChecker parenChecker) {
        super(parenChecker);
        Objects.requireNonNull(properties, "properties may not be null");
        this.properties = properties.clone();
        init();
    }

    private void init() {
        registerDefaultPostLexConverters();
    }

    /** Prepares the customizable lexer. */
    @Override
    protected Lexer prepareLexer(CharStream inputStream) {
        // create a lexer that feeds off of input CharStream
        CustomizableLexer lexer = new CustomizableLexer(inputStream);
        // possible options of lexer
        lexer.setBailOut(isBailOutInLexer);
        lexer.setProperties(properties);
        return lexer;
    }

    /**
     * Determines, if the lexer should bail out, when encountering an unexpected error. Is used in
     * {@link #parseInput(Object, ParsingCreator)}. Default value is true.
     */
    public boolean isBailOutInLexer() {
        return isBailOutInLexer;
    }

    /**
     * Determines, if the parser should bail out, when encountering an unexpected error. Is used in
     * {@link #parseInput(Object, ParsingCreator)}. Default value is true.
     */
    public void setBailOutInLexer(boolean value) {
        isBailOutInLexer = value;
    }

    /**
     * Is called directly after lexing and before checking for imbalanced and disallowed
     * parentheses. Here the registered postlex converters are applied to the token stream.
     *
     * <p>
     *
     * @see #registerDefaultPostLexConverters()
     * @see #registerPostLexConverter(Function)
     */
    @Override
    protected Triple<List<Token>, ParsingFaultCollection, Boolean> postLex(List<Token> tokenList) {
        List<ParsingFault> faults = new ArrayList<>();
        boolean bailOut = false;

        // list to collect the new tokens in
        List<Token> newTokenList = new ArrayList<>();

        for (Token token : tokenList) {
            Triple<List<Token>, List<ParsingFault>, Boolean> result = null;

            for (Function<Token, Triple<List<Token>, List<ParsingFault>, Boolean>> converter :
                    postLexConverters) {
                result = converter.apply(token);
                if (result != null) break;
            }

            if (result == null)
                result =
                        new Triple<>(
                                Collections.singletonList(token), Collections.emptyList(), false);

            newTokenList.addAll(result.a);
            faults.addAll(result.b);
            bailOut |= result.c;
        }

        return new Triple<>(newTokenList, new ParsingFaultCollection(faults), bailOut);
    }

    /**
     * Registers the given converter which is called during postlexing.
     *
     * <p>Converters take a token and convert that to a) a list of token to replace this token with
     * in the input stream; b) a list of parsing faults to add to the fault list during postlex; and
     * c) a boolean which determines whether the postLex (and therefore the complete reading
     * process) shall stop (known as bailing out, true = stop; false = continue).
     *
     * @param converter the converter
     */
    public void registerPostLexConverter(
            Function<Token, Triple<List<Token>, List<ParsingFault>, Boolean>> converter) {
        Objects.requireNonNull(converter, "converter may not be null");
        postLexConverters.add(converter);
    }

    /** Clear all postlex converters. */
    public void clearPostLexConverters() {
        postLexConverters.clear();
    }

    /** Registers default postlex converter, i.e. the converter for non separating operators. */
    protected void registerDefaultPostLexConverters() {
        registerPostLexConverter(
                token -> {
                    if (token.getType() == properties.getSymbolForNonSeparatedText().getTokenType())
                        return convertOperatorOrSymbolToken(token);
                    return null;
                });
    }

    /** Used in {@link #registerDefaultPostLexConverters()}. */
    private Triple<List<Token>, List<ParsingFault>, Boolean> convertOperatorOrSymbolToken(
            Token token) {
        ParsableSymbol op = properties.getNonSeparatingSymbols().get(token.getText());
        if (op != null)
            return new Triple<>(
                    Collections.singletonList(
                            TokenUtils.copyTokenWithType(token, op.getTokenType())),
                    Collections.emptyList(),
                    false);
        return splitIntoISYMBOLs(token, properties.getSymbolForISymbols().getTokenType());
    }

    /**
     * Used in {@link #registerDefaultPostLexConverters()}. Uses a {@link SymbolSplittingPolicy} to
     * split a string into (multiple) indexed symbols.
     */
    protected Triple<List<Token>, List<ParsingFault>, Boolean> splitIntoISYMBOLs(
            Token oldToken, int newType) {
        List<ParsingFault> faultList = new ArrayList<>();
        List<Pair<Integer, IndexedSymbol>> symbols;

        try {
            symbols = properties.getSymbolSplittingPolicy().splitSymbols(oldToken.getText());
        } catch (SymbolsNotSplittableException e) {
            faultList.add(
                    new ParsingFault(
                            GeneralParsingFaultReason.INVALID_SYMBOL,
                            oldToken.getLine() - 1,
                            oldToken.getCharPositionInLine(),
                            oldToken.getText()));
            return new Triple<>(new LinkedList<>(), faultList, isBailOutInLexer);
        }

        List<Token> newTokenList = getNewTokenList(oldToken, symbols, newType);
        return new Triple<>(newTokenList, faultList, false);
    }

    /** Used in {@link #splitIntoISYMBOLs(Token, int)}. */
    protected List<Token> getNewTokenList(
            Token oldToken, List<Pair<Integer, IndexedSymbol>> symbols, int targetTokenType) {
        List<Token> newTokenList = new LinkedList<>();

        int lastOffset = oldToken.getText().length();
        for (int i = symbols.size(); i > 0; i--) {
            Pair<Integer, IndexedSymbol> symbol = symbols.get(i - 1);
            newTokenList.add(
                    0,
                    new SymbolToken(
                            oldToken,
                            targetTokenType,
                            symbol.first(),
                            lastOffset,
                            symbol.second()));
            lastOffset = symbol.first();
        }

        return newTokenList;
    }
}
