package de.tudortmund.cs.iltis.utils.io.parser.error.visitor;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFault;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultReason;
import java.util.ArrayList;
import java.util.Arrays;
import org.antlr.v4.runtime.Token;

/**
 * This subclass of {@link VisitorErrorListener} serves the purpose to hold a collection of
 * VisitorErrorListeners to which all errors reported via the {@code reportFault()}-method shall be
 * delegated.
 */
public class ProxyVisitorErrorListener implements VisitorErrorListener {

    private final ArrayList<VisitorErrorListener> delegates;

    public ProxyVisitorErrorListener(VisitorErrorListener... delegates) {
        this.delegates = new ArrayList<>(Arrays.asList(delegates));
    }

    /** {@inheritDoc} */
    @Override
    public void reportFault(ParsingFault fault, String msg) {
        for (VisitorErrorListener current : delegates) {
            current.reportFault(fault, msg);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void reportFault(ParsingFaultReason reason, Token token, String faultText, String msg) {
        for (VisitorErrorListener current : delegates) {
            current.reportFault(reason, token, faultText, msg);
        }
    }

    /**
     * Adds a {@link VisitorErrorListener} to the collection of listeners to which all error reports
     * shall be delegated.
     *
     * @param listener The listener to add to the collection.
     */
    public void addDelegateListener(VisitorErrorListener listener) {
        delegates.add(listener);
    }
}
