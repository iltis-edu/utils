package de.tudortmund.cs.iltis.utils.io.parser.general;

import de.tudortmund.cs.iltis.utils.io.parser.error.BailAndNotifyErrorStrategy;
import de.tudortmund.cs.iltis.utils.io.parser.error.IncorrectParseInputException;
import de.tudortmund.cs.iltis.utils.io.parser.error.VerboseErrorListener;
import de.tudortmund.cs.iltis.utils.io.parser.error.visitor.*;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultCollection;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultErrorListener;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultTypeMapping;
import de.tudortmund.cs.iltis.utils.io.parser.parentheses.NotCheckingParenthesesChecker;
import de.tudortmund.cs.iltis.utils.io.parser.parentheses.ParenthesesChecker;
import de.tudortmund.cs.iltis.utils.io.reader.general.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ListTokenSource;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.misc.Triple;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * A basic reader using some kind of parsing process. This class offers a structure for different
 * readers and different grammars to build on. With {@link #parseInput(Object, ParsingCreator)} a
 * method is supplied that encapsulates the parsing process consisting of a) lexing, b) postlexing,
 * c) checking for imbalanced and disallowed parentheses, and d) parsing. Furthermore, it ensures
 * that the correct error listener is used and the generated faults are reported correctly.
 *
 * @param <ParserOutputT> the type the parsing process returns
 * @param <ReaderOutputT> the type the {@link #read(Object)}-method returns
 * @param <ParserT> the type of parser used for parsing
 */
public abstract class ParsingReader<ParserOutputT, ReaderOutputT, ParserT extends AbstractParser>
        implements Reader<ReaderOutputT> {

    /**
     * @see #isBailOutInParser()
     */
    protected boolean isBailOutInParser = true;

    /**
     * @see #isCustomBailOut()
     */
    protected boolean isCustomBailOut = false;

    /**
     * @see #isTesting()
     */
    protected boolean isTesting = false;

    /**
     * @see #isVerbose()
     */
    protected boolean isVerbose = false;

    /** The given parentheses checker */
    protected ParenthesesChecker parenChecker;

    /**
     * Creates a {@link ParsingReader} <b>without</b> testing for imbalanced or disallowed
     * parentheses.
     */
    public ParsingReader() {
        this(new NotCheckingParenthesesChecker());
    }

    /**
     * Creates a {@link ParsingReader} with the given parentheses checker.
     *
     * @param parenChecker the parentheses checker to use
     */
    public ParsingReader(ParenthesesChecker parenChecker) {
        Objects.requireNonNull(parenChecker, "parenChecker may not be null");
        this.parenChecker = parenChecker;
    }

    /**
     * The complete process of lexing and parsing. Inside of this method (via other methods), the
     * abstract methods {@link #prepareLexer(CharStream)}, {@link #prepareParser(TokenStream)},
     * {@link #prepareParseTreeVisitor(ParsingCreator, VisitorErrorHandler)}, and {@link
     * #executeParser(AbstractParser, AbstractParseTreeVisitor)} are called. Implement these methods
     * to adapt this reader to your grammar. Furthermore, by overriding {@link
     * #isBailOutInParser()}, {@link #isCustomBailOut()}, {@link #isVerbose()} and {@link
     * #isTesting()} the behavior of this method can be adapted.
     *
     * <p><b>Important note:</b> in the <b>lexer</b> throwing any exception not subclass of {@link
     * RecognitionException} (known as bailing out) is <b>customizable</b> only in special
     * subclasses of {@link Lexer}, like {@link MultiTokenLexer}. Throwing such errors is supported
     * in any lexer and caught in this class, however.
     *
     * <p><b>Implementation note:</b> all the helper methods called in this method have the return
     * type <code>Triple&lt;ResultOfOperation, ParsingFaultCollection, Boolean&gt;</code> with the
     * following meaning:
     *
     * <ul>
     *   <li>ResultOfOperation: the content of the operation (not null, except from parsing)
     *   <li>ParsingFaultCollection: a collection of faults, that occurred at this step (not null,
     *       faults that are reported to a listener are not contained)
     *   <li>Boolean: true iff the parsing process should stop (because of a severe fault, known as
     *       bailing out)
     * </ul>
     */
    @SuppressWarnings("null")
    protected ParsingFaultTypeMapping<ParserOutputT> parseInput(
            Object input, ParsingCreator creator) {
        // create a new instance of our error listeners
        ParsingFaultErrorListener listener = new ParsingFaultErrorListener();
        // We need to use a separate error listener for errors in the visitor because the regular
        // error listener needs
        // a recognizer as parameter, which the visitor does not know.
        ParsingFaultVisitorErrorListener visitorListener = new ParsingFaultVisitorErrorListener();

        // lexing
        Triple<List<Token>, ParsingFaultCollection, Boolean> resultOfLexing =
                executeLexingOnly(input, listener);
        if (resultOfLexing.c)
            return makeMapping(input, null, listener.getFaultCollection(), resultOfLexing.b);

        // postlexing
        if (isVerbose()) System.out.println("tokens before post processing:  " + resultOfLexing.a);
        Triple<List<Token>, ParsingFaultCollection, Boolean> resultOfPostLexing =
                postLex(resultOfLexing.a);
        if (resultOfPostLexing.c) {
            return makeMapping(
                    input,
                    null,
                    listener.getFaultCollection(),
                    resultOfLexing.b,
                    resultOfPostLexing.b);
        }

        // imbalance check
        if (isVerbose())
            System.out.println("tokens before imbalanced check: " + resultOfPostLexing.a);
        Triple<List<Token>, ParsingFaultCollection, Boolean> resultOfParenCheck =
                parenChecker.check(resultOfPostLexing.a);
        if (resultOfParenCheck.c) {
            return makeMapping(
                    input,
                    null,
                    listener.getFaultCollection(),
                    resultOfLexing.b,
                    resultOfPostLexing.b,
                    resultOfParenCheck.b);
        }

        // parsing
        if (isVerbose())
            System.out.println("tokens before parsing:          " + resultOfParenCheck.a);
        Triple<ParserOutputT, ParsingFaultCollection, Boolean> resultOfParsing =
                executeParsingOnly(resultOfParenCheck.a, creator, listener, visitorListener);
        return makeMapping(
                input,
                resultOfParsing.a,
                listener.getFaultCollection(),
                visitorListener.getFaultCollection(),
                resultOfLexing.b,
                resultOfPostLexing.b,
                resultOfParenCheck.b,
                resultOfParsing.b);
    }

    /**
     * Executes the actual lexing process.
     *
     * @param input the input
     * @param listener the listener to use
     * @return a triple containing a list of read tokens (never null), a {@link
     *     ParsingFaultCollection}-object (never null, always empty), an indicator whether the
     *     parsing should stop (bailing out)
     */
    private Triple<List<Token>, ParsingFaultCollection, Boolean> executeLexingOnly(
            Object input, ParsingFaultErrorListener listener) {
        // create a CharStream that reads the input
        ANTLRInputStream inputStream = new ANTLRInputStream(input.toString());

        Lexer lexer = prepareLexer(inputStream);
        // replace default error listener by our own
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);
        if (isVerbose()) lexer.addErrorListener(new VerboseErrorListener());

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        // execute actual lexing
        try {
            tokenStream.fill();
        } catch (ParseCancellationException e) {
            // only returning "bail out" because all errors have to be reported
            // to the error listeners before throwing an exception
            return new Triple<>(new ArrayList<>(), new ParsingFaultCollection(), true);
        }

        List<Token> tokenList = tokenStream.getTokens();
        return new Triple<>(tokenList, new ParsingFaultCollection(), false);
    }

    /**
     * Executes the actual parsing process.
     *
     * @param tokenList the list of all tokens
     * @param creator the creator to use
     * @param listener the listener to use
     * @return a triple containing the output formula (can be null), a {@link
     *     ParsingFaultCollection}-object (never null, always empty), an indicator whether the
     *     parsing should stop (bailing out, always true)
     */
    @SuppressWarnings("null")
    private Triple<ParserOutputT, ParsingFaultCollection, Boolean> executeParsingOnly(
            List<Token> tokenList,
            ParsingCreator creator,
            ParsingFaultErrorListener listener,
            ParsingFaultVisitorErrorListener visitorListener) {
        // convert list to token stream
        CommonTokenStream tokenStream = new CommonTokenStream(new ListTokenSource(tokenList));

        ParserT parser = prepareParser(tokenStream);

        ProxyVisitorErrorListener proxyVisitorListener =
                new ProxyVisitorErrorListener(visitorListener);

        // replace default error listener by our own
        parser.removeErrorListeners();
        parser.addErrorListener(listener);
        parser.setCreator(creator);
        parser.setCustomBailOut(isCustomBailOut);
        if (isVerbose()) {
            parser.addErrorListener(new VerboseErrorListener());
            proxyVisitorListener.addDelegateListener(new VerboseVisitorErrorListener());
        }
        // omit error recovery -> faster
        if (isBailOutInParser()) parser.setErrorHandler(new BailAndNotifyErrorStrategy());
        // to get reports of *all* ambiguities
        if (isTesting())
            parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);

        AbstractParseTreeVisitor<ParserOutputT> visitor =
                prepareParseTreeVisitor(
                        creator, new VisitorErrorHandler(proxyVisitorListener, isCustomBailOut));
        // Previously, under certain conditions the construction of a parse tree was disabled here
        // to gain speed.
        // However, this led to unexpected behavior in actions inside grammars and is thus omitted
        // here.
        // For our current way of using parse tree visitors, parse trees need to be constructed
        // anyway.

        // the actual process of parsing
        // the try-catch-block is only needed, if isBailOutInParser() or isBailOutInVisitor() return
        // true
        ParserOutputT output = null;
        try {
            output = executeParser(parser, visitor);
        } catch (ParseCancellationException e) {
            // error listener is already informed by error strategy
        }

        return new Triple<>(output, new ParsingFaultCollection(), false);
    }

    /** Constructs a mapping of all the input fault collections. */
    private ParsingFaultTypeMapping<ParserOutputT> makeMapping(
            Object input,
            ParserOutputT output,
            ParsingFaultCollection basicFaultCol,
            ParsingFaultCollection... moreFaultCols) {
        ParsingFaultTypeMapping<ParserOutputT> mapping =
                new ParsingFaultTypeMapping<>(input.toString(), output);
        for (ParsingFaultCollection faultCol : moreFaultCols) {
            basicFaultCol = basicFaultCol.withFaults(faultCol);
        }
        if (basicFaultCol.containsAnyFault()) mapping = mapping.with(basicFaultCol);
        return mapping;
    }

    /** The standard implementation for the #read method */
    @Override
    public ReaderOutputT read(Object o) throws IncorrectParseInputException {
        ParsingFaultTypeMapping<ReaderOutputT> mapping =
                convertParserOutputToReaderOutput(parseInput(o, new ParsingCreator()));

        return returnResult(mapping);
    }

    /**
     * Is called by {@link #parseInput(Object, ParsingCreator)}. Should create a new lexer and set
     * basic settings. Listener registration is done by {@link #parseInput(Object, ParsingCreator)}.
     *
     * @param inputStream the input char stream
     * @return the lexer, not null
     */
    protected abstract Lexer prepareLexer(CharStream inputStream);

    /**
     * Is called by {@link #parseInput(Object, ParsingCreator)}. Should create a new parser and set
     * basic settings. Listener registration is done by {@link #parseInput(Object, ParsingCreator)}.
     *
     * @param tokenStream the input token stream
     * @return the parser, not null
     */
    protected abstract ParserT prepareParser(TokenStream tokenStream);

    /**
     * Is called by {@link #parseInput(Object, ParsingCreator)}. Should create a new parse tree
     * visitor and set basic settings. If you do not want to use a parse tree visitor you do not
     * need to overwrite this method (returns null as default implementation).
     *
     * @param creator the parsing creator used
     * @param errorHandler the error handler to be used
     * @return the parse tree visitor, null iff no parse tree visitor shall be used
     */
    protected AbstractParseTreeVisitor<ParserOutputT> prepareParseTreeVisitor(
            ParsingCreator creator, VisitorErrorHandler errorHandler) {
        return null;
    }

    /**
     * Executes the given parser, running the produced parse tree through the given visitor.
     *
     * <p>In case the input is not valid, but a parse tree was still constructed (e.g. because all
     * the parse errors were caught by explicit ANTLR error-rules), this method should return a
     * "best-effort" construction, in which those errors caught explicitly using parser-rules are
     * fixed on a best-effort basis. The object constructed in this way is then used by the feedback
     * mechanism to give the student a hint as to how to fix their input (think Google's "did you
     * mean")
     *
     * <p>In case even a best-effort fix is impossible, a {@link
     * ParseTreeTraversalCancellationException} should be thrown
     *
     * <p>Called by {@link #parseInput(Object, ParsingCreator)}.
     *
     * @param parser the parser, which you can safely assume that is the same object that was
     *     returned by {@link #prepareParser(TokenStream)}
     * @param visitor the prepared parse tree visitor, you can safely assume that is the same object
     *     that was returned by {@link #prepareParseTreeVisitor(ParsingCreator,
     *     VisitorErrorHandler)}
     * @return the parsed result object, possibly {@code null} if parsing failed
     * @throws ParseCancellationException if the parse encountered an error and {@link
     *     #isBailOutInParser()} returns {@code true}, or if the visitor failed to automatically
     *     correct an error.
     */
    protected abstract ParserOutputT executeParser(
            ParserT parser, AbstractParseTreeVisitor<ParserOutputT> visitor);

    /**
     * Determines, if the parser should bail out, when encountering an unexpected error. Is used in
     * {@link #parseInput(Object, ParsingCreator)}. Default value is true.
     */
    public boolean isBailOutInParser() {
        return isBailOutInParser;
    }

    /**
     * Determines, if the parser should bail out, when encountering an unexpected error. Is used in
     * {@link #parseInput(Object, ParsingCreator)}. Default value is true.
     */
    public void setBailOutInParser(boolean value) {
        isBailOutInParser = value;
    }

    /**
     * Determines, if the parser and the visitor should bail out, when encountering a custom error.
     * Subclasses need to provide parsers and visitors which honor these attributes. Default value
     * is false.
     */
    public boolean isCustomBailOut() {
        return isCustomBailOut;
    }

    /**
     * Determines, if the parser and the visitor should bail out, when encountering a custom error.
     * Subclasses need to provide parsers and visitors which honor these attributes. Default value
     * is false.
     */
    public void setCustomBailOut(boolean value) {
        isCustomBailOut = value;
    }

    /**
     * In testing mode, a full check for ambiguities of the entered formula is performed, which can
     * take much more time. Is used in {@link #parseInput(Object, ParsingCreator)}. Default value is
     * false.
     */
    public boolean isTesting() {
        return isTesting;
    }

    /**
     * In testing mode, a full check for ambiguities of the entered formula is performed, which can
     * take much more time. Is used in {@link #parseInput(Object, ParsingCreator)}. Default value is
     * false.
     */
    public void setTesting(boolean value) {
        isTesting = value;
    }

    /**
     * Determines, if token lists and encountered errors should be printed out while parsing Is used
     * in {@link #parseInput(Object, ParsingCreator)}. Default value is false.
     */
    public boolean isVerbose() {
        return isVerbose;
    }

    /**
     * Determines, if token lists and encountered errors should be printed out while parsing Is used
     * in {@link #parseInput(Object, ParsingCreator)}. Default value is false.
     */
    public void setVerbose(boolean value) {
        isVerbose = value;
    }

    /**
     * Here, activities that shall modify the token stream before it is handed to the imbalanced
     * parentheses check and to the parser can be placed. If any faults are found, they can be
     * "repaired" by replacing or adding tokens. See {@link #parseInput(Object, ParsingCreator)} for
     * an explanation of the return type. *
     *
     * @param tokenList the original token list
     * @return a triple containing the new list of tokens (never null), a {@link
     *     ParsingFaultCollection}-object (never null), an indicator whether the parsing should stop
     *     (bailing out)
     */
    protected Triple<List<Token>, ParsingFaultCollection, Boolean> postLex(List<Token> tokenList) {
        return new Triple<>(tokenList, new ParsingFaultCollection(), false);
    }

    /**
     * A method to convert the output given by {@link #parseInput(Object, ParsingCreator)} to the
     * output for {@link #read(Object)}.
     *
     * @param mapping the mapping generated by {@link #parseInput(Object, ParsingCreator)}
     * @return a mapping with the output type
     */
    protected abstract ParsingFaultTypeMapping<ReaderOutputT> convertParserOutputToReaderOutput(
            ParsingFaultTypeMapping<ParserOutputT> mapping);

    /**
     * A utility method that unwraps the output object from the given mapping and throws an {@link
     * IncorrectParseInputException} if the given mapping is not empty.
     *
     * @param mapping the mapping
     * @return the output object from the given mapping
     * @throws IncorrectParseInputException if the given mapping is not empty
     */
    protected ReaderOutputT returnResult(ParsingFaultTypeMapping<ReaderOutputT> mapping)
            throws IncorrectParseInputException {
        if (mapping.containsAny()) throw new IncorrectParseInputException(mapping);
        return mapping.getOutput();
    }
}
