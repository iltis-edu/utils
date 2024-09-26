package de.tudortmund.cs.iltis.utils.io.parser.fault;

import de.tudortmund.cs.iltis.utils.collections.FaultCollection;
import de.tudortmund.cs.iltis.utils.io.parser.general.GeneralParsingFaultReason;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A fault collection for faults occurring during parsing. It does not contain any additional
 * attributes.
 */
public class ParsingFaultCollection extends FaultCollection<ParsingFaultReason, ParsingFault> {

    /** Creates an empty fault collection. */
    public ParsingFaultCollection() {
        super();
    }

    /** Creates a fault collection, already containing the given faults. */
    public ParsingFaultCollection(List<ParsingFault> faults) {
        super(faults);
    }

    @Override
    public ParsingFaultCollection withFault(ParsingFault fault) {
        return (ParsingFaultCollection) super.withFault(fault);
    }

    @Override
    public ParsingFaultCollection withFaults(
            FaultCollection<? extends ParsingFaultReason, ? extends ParsingFault> faults) {
        return (ParsingFaultCollection) super.withFaults(faults);
    }

    /**
     * Utility function for {@link #getFaults(ParsingFaultReason)} that does conversion to the
     * specialized {@link ParenthesesMissingParsingFault}-type. Any fault with reason {@link
     * GeneralParsingFaultReason#PARENTHESES_MISSING} which is no instance of {@link
     * ParenthesesMissingParsingFault} (which poses an error) is ignored.
     */
    public List<ParenthesesMissingParsingFault> getFaultsOfParenthesesMissing() {
        return faults.stream()
                .filter(fault -> fault.getReason() == GeneralParsingFaultReason.PARENTHESES_MISSING)
                .filter(fault -> fault instanceof ParenthesesMissingParsingFault)
                .map(fault -> (ParenthesesMissingParsingFault) fault)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a newly created list with all faults contained by this collection. The faults are
     * sorted according to the group of their reasons.
     *
     * @return a list with all faults
     * @see ParsingFaultReason#getGroup()
     * @see ParsingFault#COMPARATOR
     */
    @Override
    public List<ParsingFault> getFaults() {
        List<ParsingFault> list = super.getFaults();
        list.sort(ParsingFault.COMPARATOR);
        return list;
    }

    @Override
    public String toString() {
        return "ParsingFaultCollection [faults = " + getFaults() + "]";
    }

    @Override
    public ParsingFaultCollection clone() {
        // cloning is done in constructor
        return new ParsingFaultCollection(faults);
    }

    /** for serialization */
    private static final long serialVersionUID = 1L;
}
