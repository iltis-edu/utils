package de.tudortmund.cs.iltis.utils.io.parser.symbol;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.collections.Pair;
import de.tudortmund.cs.iltis.utils.function.SerializableFunction;
import de.tudortmund.cs.iltis.utils.general.Data;
import de.tudortmund.cs.iltis.utils.io.reader.general.Reader;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * An implementation of the {@link SymbolSplittingPolicy} which splits the input by the use of
 * regular expressions. In the constructors, a regular expression for a single symbol can be
 * specified.
 */
public class RegularSymbolSplittingPolicy extends BaseSymbolSplittingPolicy {

    private static final SerializableFunction<String, IndexedSymbol> ungroup =
            str -> new IndexedSymbol(str.replace("'", ""));
    private static final SerializableFunction<IndexedSymbol, String> group =
            symbol ->
                    "'"
                            + symbol.getName()
                            + "'^'"
                            + symbol.getSuperscript()
                            + "'_'"
                            + symbol.getSubscript()
                            + "'";

    /**
     * Symbols may consist of one upper or lower case Latin letter without any sub- and
     * superscripts.
     */
    public static final RegularSymbolSplittingPolicy TU_DORTMUND_LS1_LOGIK_POLICY =
            new RegularSymbolSplittingPolicy("[a-zA-Z]");

    /**
     * Symbols may consist of one upper or lower case Latin letter with arbitrary numeric sub- and
     * superscripts.
     */
    public static final RegularSymbolSplittingPolicy TU_DORTMUND_LS1_WITH_NUMERIC_INDEX_POLICY =
            new RegularSymbolSplittingPolicy("[a-zA-Z]", "[0-9]+");

    /**
     * Symbols may consist of an alphanumeric name and alphanumeric sub- and superscripts, complying
     * with the following restrictions: (a) the first character of the name part may have to be a
     * letter (i.e. not a digit) and (b) if the name, the sub- or the superscript consists of
     * multiple characters, the respective part have to be enclosed with apostrophes, e.g.
     * a^'long'_'symbol', 'all'_0
     */
    public static final RegularSymbolSplittingPolicy TU_DORTMUND_LS1_WITH_UNARY_INDEX_POLICY =
            new RegularSymbolSplittingPolicy(
                    "[a-zA-Z]|'[a-zA-Z0-9]+'", "[a-zA-Z0-9]|'[a-zA-Z0-9]+'", ungroup, group);

    /** Symbols may consist of any sort of Unicode. */
    public static final RegularSymbolSplittingPolicy ALL_UNARY_SYMBOLS_POLICY =
            new RegularSymbolSplittingPolicy(".");

    /**
     * Symbols may consist of an arbitrary name and arbitrary sub- and superscripts, complying with
     * the following restriction: if the name, the sub- or the superscript consists of multiple
     * characters, the respective part have to be enclosed with apostrophes, e.g. a^'long'_'symbol',
     * 'all'_0
     */
    public static final RegularSymbolSplittingPolicy UNARY_NAME_AND_INDEX_POLICY =
            new RegularSymbolSplittingPolicy(
                    "[^\\^_']|'[^\\^_']+'", "[^\\^_']|'[^\\^_']+'", ungroup, group);

    /** Symbols may consist of all digits (0-9) and must be at least one digit long. */
    public static final RegularSymbolSplittingPolicy NUMBERS_POLICY =
            new RegularSymbolSplittingPolicy("[0-9]+");

    public static RegularSymbolSplittingPolicy CREATE_OF_SYMBOLS(Collection<String> symbols) {
        Objects.requireNonNull(symbols);
        if (symbols.size() == 0) throw new IllegalArgumentException("symbols may not be empty");

        StringBuilder pattern = new StringBuilder();
        for (String symbol : symbols) {
            pattern.append("|(");
            pattern.append(RegExp.quote(symbol));
            pattern.append(")");
        }
        pattern.deleteCharAt(0);
        return new RegularSymbolSplittingPolicy(pattern.toString());
    }

    public static RegularSymbolSplittingPolicy CREATE_OF_SYMBOLS(String... symbols) {
        Objects.requireNonNull(symbols);
        return CREATE_OF_SYMBOLS(Data.newArrayList(symbols));
    }

    private String completeRegExp;
    private String tailRegExp;
    private String headRegExp;
    private String innerPattern;

    /** For serialization */
    @SuppressWarnings("unused")
    private RegularSymbolSplittingPolicy() {}

    public RegularSymbolSplittingPolicy(
            String symbolPattern,
            Reader<IndexedSymbol> toIndexedSymbol,
            Writer<IndexedSymbol> toString) {
        this(
                symbolPattern,
                (SerializableFunction<String, IndexedSymbol>) toIndexedSymbol::read,
                (SerializableFunction<IndexedSymbol, String>) toString::write);
    }

    public RegularSymbolSplittingPolicy(
            String namePattern,
            String indexPattern,
            Reader<IndexedSymbol> toIndexedSymbol,
            Writer<IndexedSymbol> toString) {
        this(
                namePattern,
                indexPattern,
                (SerializableFunction<String, IndexedSymbol>) toIndexedSymbol::read,
                (SerializableFunction<IndexedSymbol, String>) toString::write);
    }

    public RegularSymbolSplittingPolicy(
            String namePattern,
            String subscriptPattern,
            String superscriptPattern,
            Reader<IndexedSymbol> toIndexedSymbol,
            Writer<IndexedSymbol> toString) {
        this(
                namePattern,
                subscriptPattern,
                superscriptPattern,
                (SerializableFunction<String, IndexedSymbol>) toIndexedSymbol::read,
                (SerializableFunction<IndexedSymbol, String>) toString::write);
    }

    public RegularSymbolSplittingPolicy(String symbolPattern) {
        this(symbolPattern, DEFAULT_TO_INDEXED_SYMBOL, DEFAULT_TO_STRING);
    }

    public RegularSymbolSplittingPolicy(String namePattern, String indexPattern) {
        this(namePattern, indexPattern, DEFAULT_TO_INDEXED_SYMBOL, DEFAULT_TO_STRING);
    }

    public RegularSymbolSplittingPolicy(
            String namePattern, String subscriptPattern, String superscriptPattern) {
        this(
                namePattern,
                subscriptPattern,
                superscriptPattern,
                DEFAULT_TO_INDEXED_SYMBOL,
                DEFAULT_TO_STRING);
    }

    public RegularSymbolSplittingPolicy(
            String symbolPattern,
            SerializableFunction<String, IndexedSymbol> toIndexedSymbol,
            SerializableFunction<IndexedSymbol, String> toString) {
        super(toIndexedSymbol, toString);
        Objects.requireNonNull(symbolPattern);
        innerPattern = symbolPattern;
        try {
            // Compile RegExp to check if they are valid but store as string because RegExp is not
            // serializable for some reason
            completeRegExp = "^(" + symbolPattern + ")+$";
            RegExp.compile(completeRegExp);
            headRegExp = "^(" + symbolPattern + ")$";
            RegExp.compile(headRegExp);
            tailRegExp = "^(" + symbolPattern + ")*$";
            RegExp.compile(tailRegExp);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Pattern '" + symbolPattern + "' is invalid", e);
        }
    }

    public RegularSymbolSplittingPolicy(
            String namePattern,
            String indexPattern,
            SerializableFunction<String, IndexedSymbol> toIndexedSymbol,
            SerializableFunction<IndexedSymbol, String> toString) {
        this(namePattern, indexPattern, indexPattern, toIndexedSymbol, toString);
    }

    public RegularSymbolSplittingPolicy(
            String namePattern,
            String subscriptPattern,
            String superscriptPattern,
            SerializableFunction<String, IndexedSymbol> toIndexedSymbol,
            SerializableFunction<IndexedSymbol, String> toString) {
        this(
                "("
                        + namePattern
                        + ")("
                        + "((\\^("
                        + superscriptPattern
                        + "))?(_("
                        + subscriptPattern
                        + "))?)|"
                        + "((_("
                        + subscriptPattern
                        + "))?(\\^("
                        + superscriptPattern
                        + "))?))",
                toIndexedSymbol,
                toString);
    }

    @Override
    protected List<Pair<Integer, String>> splitSymbolsToStrings(String input)
            throws SymbolsNotSplittableException {
        List<Pair<Integer, String>> symbols = new ArrayList<>();

        MatchResult matcher = RegExp.compile(completeRegExp).exec(input);
        if (matcher == null) throw new SymbolsNotSplittableException(input, innerPattern);

        int processedOffset = 0;

        while (processedOffset < input.length()) {
            for (int estimatedLengthOfSymbol = input.length();
                    estimatedLengthOfSymbol >= processedOffset;
                    estimatedLengthOfSymbol--) {
                String head = input.substring(processedOffset, estimatedLengthOfSymbol);
                String tail = input.substring(estimatedLengthOfSymbol);
                MatchResult headMatcher = RegExp.compile(headRegExp).exec(head);
                if (headMatcher == null) continue;
                MatchResult tailMatcher = RegExp.compile(tailRegExp).exec(tail);
                if (tailMatcher == null) continue;
                String lastMatchedSymbol = headMatcher.getGroup(1);
                symbols.add(new Pair<>(processedOffset, lastMatchedSymbol));
                processedOffset += lastMatchedSymbol.length();
                break;
            }
        }

        return symbols;
    }

    /**
     * Combines this policy with the given policy by using an alternative between the two regular
     * expressions. The transforming functions are taken from this policy.
     *
     * @return a newly created policy
     */
    public RegularSymbolSplittingPolicy or(RegularSymbolSplittingPolicy policy) {
        return new RegularSymbolSplittingPolicy(
                "(" + this.innerPattern + ")|(" + policy.innerPattern + ")",
                this.toIndexedSymbol,
                this.toString);
    }

    @Override
    public String toString() {
        return "regular symbol splitting policy with pattern " + innerPattern;
    }
}
