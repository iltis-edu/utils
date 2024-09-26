package de.tudortmund.cs.iltis.utils.io.parser.error.visitor;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFault;

/**
 * General {@link VisitorErrorListener} whose only purpose is to print out every reported error to
 * the (error) command line.
 */
public class VerboseVisitorErrorListener implements VisitorErrorListener {

    public VerboseVisitorErrorListener() {}

    /** {@inheritDoc} */
    @Override
    public void reportFault(ParsingFault fault, String msg) {
        System.err.println(
                "VerboseVisitorErrorListener: msg: "
                        + msg
                        + ", erroneous part of input: "
                        + fault.getPartOfInput()
                        + "; read input: "
                        + fault.getText()
                        + "; line: "
                        + fault.getLine()
                        + "; pos: "
                        + fault.getCharPositionInLine());
    }
}
