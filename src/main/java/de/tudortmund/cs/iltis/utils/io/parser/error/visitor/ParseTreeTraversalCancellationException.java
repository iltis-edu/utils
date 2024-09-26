package de.tudortmund.cs.iltis.utils.io.parser.error.visitor;

import org.antlr.v4.runtime.misc.ParseCancellationException;

public class ParseTreeTraversalCancellationException extends ParseCancellationException {

    /**
     * This exception can only be thrown by {@link VisitorErrorHandler}, hence it is {@code
     * protected}.
     */
    protected ParseTreeTraversalCancellationException() {
        super();
    }
}
