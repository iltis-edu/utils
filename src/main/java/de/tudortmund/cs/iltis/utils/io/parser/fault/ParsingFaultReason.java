package de.tudortmund.cs.iltis.utils.io.parser.fault;

import de.tudortmund.cs.iltis.utils.io.parser.general.GeneralParsingFaultReason;
import de.tudortmund.cs.iltis.utils.io.parser.parentheses.ParenthesesParsingFaultReason;
import java.io.Serializable;

/**
 * An interface to be used in {@link ParsingFault} to allow general reasons ({@link
 * GeneralParsingFaultReason}) be extended by specialized reasons (e.g. for parentheses {@link
 * ParenthesesParsingFaultReason} or grammar specific ones).
 */
public interface ParsingFaultReason extends Serializable {

    /**
     * Returns the group which is an integer of this enum element.
     *
     * <h6>Meaning of the group</h6>
     *
     * If an error is encountered during parsing, a fault is created and the attempt is made to
     * "correct" the entered formula. Especially for long formulae however, these corrections might
     * make more damage than they repair. In other words: if these corrections are not accurate,
     * more parsing errors will occur and therefore more faults will be created. Now, the group is
     * an indicator, how "basic" the error is. The lower the group is, the earlier in the parsing
     * process the error occurred and, thus, the more accurate the reported fault should be. As a
     * consequence, if multiple faults are reported for a formula, you might prefer the faults with
     * lower group numbers, as they cannot be caused by (for this particular formula) misleading
     * corrections.
     *
     * <h6>Implementation note</h6>
     *
     * Because GWT serialization does not support any fields in enums, the group value has to be
     * calculated in this method and cannot be saved as a field.
     */
    int getGroup();
}
