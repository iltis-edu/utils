package de.tudortmund.cs.iltis.utils.io.parser.fault;

import de.tudortmund.cs.iltis.utils.function.SerializableComparator;
import de.tudortmund.cs.iltis.utils.io.parser.general.GeneralParsingFaultReason;
import de.tudortmund.cs.iltis.utils.io.parser.parentheses.ParenthesesParsingFaultReason;
import de.tudortmund.cs.iltis.utils.io.reader.general.ReadingFault;

/**
 * A fault which is extended by a partOfFormula field. The interface {@link ParsingFaultReason} is
 * used as concrete type for reason to allow general reasons ({@link GeneralParsingFaultReason}) be
 * extended by specialized reasons (e.g. for parentheses {@link ParenthesesParsingFaultReason} or
 * grammar specific ones).
 */
public class ParsingFault extends ReadingFault<ParsingFaultReason> {

    /**
     * A comparator which uses the group of the reason for ordering (see {@link
     * ParsingFaultReason#getGroup()}).
     *
     * <p>Note: this comparator imposes orderings that are inconsistent with equals.
     */
    public static final SerializableComparator<ParsingFault> COMPARATOR =
            (a, b) -> a.getReason().getGroup() - b.getReason().getGroup();

    /** the erroneous part of the input; may empty but not null */
    protected String partOfInput;

    /** Creates a new ParsingFault. */
    public ParsingFault(
            ParsingFaultReason reason,
            int line,
            int charPositionInLine,
            String text,
            String partOfInput) {
        super(reason, line, charPositionInLine, text);
        this.partOfInput = partOfInput != null ? partOfInput : "";
    }

    /** Creates a new ParsingFault with empty partOfInput. */
    public ParsingFault(ParsingFaultReason reason, int line, int charPositionInLine, String text) {
        super(reason, line, charPositionInLine, text);
        partOfInput = "";
    }

    /**
     * @see ParsingFault#partOfInput
     */
    public String getPartOfInput() {
        return partOfInput;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + partOfInput.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        ParsingFault other = (ParsingFault) obj;
        return partOfInput.equals(other.partOfInput);
    }

    @Override
    public String toString() {
        return "ParsingFault [reason = "
                + getReason()
                + ", line = "
                + line
                + ", charPositionInLine = "
                + charPositionInLine
                + ", text = "
                + text
                + ", partOfInput = "
                + partOfInput
                + "]";
    }

    /**
     * Creates a shallow copy of this object.
     *
     * <p><b>Note:</b> the reason is not cloned.
     *
     * @return a shallow copy of this object
     */
    @Override
    protected ParsingFault clone() {
        return new ParsingFault(getReason(), line, charPositionInLine, text, partOfInput);
    }

    /** For serialization */
    private static final long serialVersionUID = 1L;

    /** For serialization */
    @SuppressWarnings("unused")
    protected ParsingFault() {}
}
