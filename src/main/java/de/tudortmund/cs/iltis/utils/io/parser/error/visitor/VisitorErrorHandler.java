package de.tudortmund.cs.iltis.utils.io.parser.error.visitor;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFault;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultReason;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This class forms the interface between a subclass of {@link ParseTreeVisitor} and the error
 * listener (subclass of {@link VisitorErrorListener}).
 */
public class VisitorErrorHandler {

    private final VisitorErrorListener listener;
    private final boolean isCustomBailOut;

    public VisitorErrorHandler(VisitorErrorListener listener, boolean isCustomBailOut) {
        this.listener = listener;
        this.isCustomBailOut = isCustomBailOut;
    }

    /**
     * Reports the fault to the specified error handler and bails out afterward if specified by
     * isCustomBailOut.
     *
     * @param fault The fault which shall be delegated to the error listener
     */
    public void reportFault(ParsingFault fault, String msg) {
        listener.reportFault(fault, msg);

        if (isCustomBailOut) throw new ParseTreeTraversalCancellationException();
    }

    /**
     * Reports the fault to the specified error handler and bails out afterward if specified by
     * isCustomBailOut.
     *
     * @param reason The reason this fault occurred
     * @param token The token which caused this fault
     * @param faultText The erroneous part of the input; may be empty but not null
     * @param msg The error message
     */
    public void reportFault(ParsingFaultReason reason, Token token, String faultText, String msg) {
        listener.reportFault(reason, token, faultText, msg);

        if (isCustomBailOut) throw new ParseTreeTraversalCancellationException();
    }

    /**
     * Reports the fault to the specified error handler and <b>always</b> bails out afterwards.
     *
     * @param fault The fault which shall be delegated to the error listener
     */
    public void reportFaultAndAlwaysBailOut(ParsingFault fault, String msg) {
        listener.reportFault(fault, msg);

        throw new ParseTreeTraversalCancellationException();
    }

    /**
     * Reports the fault to the specified error handler and <b>always</b> bails out afterwards.
     *
     * @param reason The reason this fault occurred
     * @param token The token which caused this fault
     * @param faultText The erroneous part of the input; may be empty but not null
     * @param msg The error message
     */
    public void reportFaultAndAlwaysBailOut(
            ParsingFaultReason reason, Token token, String faultText, String msg) {
        listener.reportFault(reason, token, faultText, msg);

        throw new ParseTreeTraversalCancellationException();
    }

    public boolean isCustomBailOut() {
        return isCustomBailOut;
    }
}
