package de.tudortmund.cs.iltis.utils.io.parser.error.visitor;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFault;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultCollection;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultErrorListener;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultReason;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsingReader;
import org.antlr.v4.runtime.Token;

/**
 * A superclass which subclasses can be used by a subclass of {@link
 * org.antlr.v4.runtime.tree.ParseTreeVisitor} to add faults and errors to the {@link
 * ParsingFaultCollection} while parsing with the ANTLR framework using the {@link ParsingReader}.
 *
 * <p>This needs to be a separate listener because the general {@link ParsingFaultErrorListener}
 * expects a {@link org.antlr.v4.runtime.Recognizer} which is not known to the ParseTreeVisitor.
 */
public interface VisitorErrorListener {

    void reportFault(ParsingFault fault, String msg);

    /**
     * The default implementation for reporting a fault. A {@link ParsingFault}-object will be
     * created automatically.
     *
     * @param reason The reason of this fault.
     * @param token The token that caused this fault.
     * @param faultText The erroneous part of the input; may be empty but not null
     * @param msg The error message describing the fault more detailed.
     */
    default void reportFault(ParsingFaultReason reason, Token token, String faultText, String msg) {
        reportFault(
                new ParsingFault(
                        reason,
                        token.getLine() - 1,
                        token.getCharPositionInLine(),
                        token.getText(),
                        faultText),
                msg);
    }
}
