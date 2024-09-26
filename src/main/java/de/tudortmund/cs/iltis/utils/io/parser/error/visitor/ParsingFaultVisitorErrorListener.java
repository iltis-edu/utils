package de.tudortmund.cs.iltis.utils.io.parser.error.visitor;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFault;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultCollection;
import java.util.ArrayList;
import java.util.List;

/**
 * An error listener for errors or faults thrown while traversing a {@link
 * org.antlr.v4.runtime.tree.ParseTree} using a {@link org.antlr.v4.runtime.tree.ParseTreeVisitor}
 * or a subclass of the ParseTreeVisitor.
 */
public class ParsingFaultVisitorErrorListener implements VisitorErrorListener {

    private final List<ParsingFault> faults;

    public ParsingFaultVisitorErrorListener() {
        faults = new ArrayList<>();
    }

    /** {@inheritDoc} */
    @Override
    public void reportFault(ParsingFault fault, String msg) {
        faults.add(fault);
    }

    public ParsingFaultCollection getFaultCollection() {
        return new ParsingFaultCollection(faults);
    }
}
