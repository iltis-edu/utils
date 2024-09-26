package de.tudortmund.cs.iltis.utils.io.parser.fault;

import de.tudortmund.cs.iltis.utils.io.parser.error.CustomRecognitionException;
import de.tudortmund.cs.iltis.utils.io.parser.general.GeneralParsingFaultReason;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.antlr.v4.runtime.misc.Utils;

/**
 * ErrorListener for a predicate formula parser who returns the reported faults as {@link
 * ParsingFaultCollection}-object.
 */
public class ParsingFaultErrorListener extends BaseErrorListener {

    private List<ParsingFault> faults;

    /** Creates a new {@link ParsingFaultErrorListener}. */
    public ParsingFaultErrorListener() {
        faults = new ArrayList<>();
    }

    @Override
    public void syntaxError(
            @NotNull Recognizer<?, ?> recognizer,
            @Nullable Object offendingSymbol,
            int line,
            int charPositionInLine,
            @NotNull String msg,
            @Nullable RecognitionException e) {
        // msg (message) is not used, since it is grammar specific
        // and therefore cannot be interpreted by caller
        ParsingFault newFault = null;

        if (e != null) {
            if (e instanceof CustomRecognitionException) {
                // standard case of faults with individual error rules
                newFault = ((CustomRecognitionException) e).getFault();
            } else if (e instanceof NoViableAltException) {
                // currently, no special handling
            } else if (e instanceof InputMismatchException) {
                // currently, no special handling
            } else if (e instanceof FailedPredicateException) {
                // currently, no special handling; it should not occur
            } else if (e instanceof LexerNoViableAltException) {
                // from lexer only
                newFault =
                        new ParsingFault(
                                GeneralParsingFaultReason.INVALID_SYMBOL,
                                line - 1,
                                charPositionInLine,
                                getTextOfLexerNoViableAltException((LexerNoViableAltException) e),
                                null);
            }
        }

        if (newFault == null) {
            // default behavior if cause of error cannot be determined
            newFault =
                    new ParsingFault(
                            GeneralParsingFaultReason.VARIOUS,
                            line - 1,
                            charPositionInLine,
                            getTextOfSymbol(offendingSymbol),
                            null);
        }

        faults.add(newFault);
    }

    private String getTextOfSymbol(Object symbol) {
        String text;
        if (symbol == null) text = "";
        else if (symbol instanceof Token) text = ((Token) symbol).getText();
        else text = symbol.toString();
        return text;
    }

    /** Copied from {@link LexerNoViableAltException#toString()}. */
    private String getTextOfLexerNoViableAltException(LexerNoViableAltException e) {
        String symbol = "";
        if (e.getStartIndex() >= 0 && e.getStartIndex() < e.getInputStream().size()) {
            symbol = e.getInputStream().getText(Interval.of(e.getStartIndex(), e.getStartIndex()));
            symbol = Utils.escapeWhitespace(symbol, false);
        }
        return symbol;
    }

    public ParsingFaultCollection getFaultCollection() {
        return new ParsingFaultCollection(faults);
    }
}
