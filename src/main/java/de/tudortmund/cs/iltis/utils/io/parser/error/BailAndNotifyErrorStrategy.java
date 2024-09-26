package de.tudortmund.cs.iltis.utils.io.parser.error;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

/**
 * An error strategy based on {@link BailErrorStrategy}, which ensures that all registered error
 * listeners are informed about <b>all</b> occurring errors.
 */
public class BailAndNotifyErrorStrategy extends BailErrorStrategy {

    /**
     * Throws a {@link ParseCancellationException} <b>and</b> notifies all error listeners, which
     * {@link BailErrorStrategy} does not in this case.
     */
    @Override
    public Token recoverInline(Parser recognizer) throws RecognitionException {
        try {
            // throws an exception for sure
            super.recoverInline(recognizer);
            throw null;
        } catch (ParseCancellationException e) {
            RecognitionException recEx = (RecognitionException) e.getCause();
            recognizer.notifyErrorListeners(recEx.getOffendingToken(), recEx.getMessage(), recEx);
            throw e;
        }
    }
}
