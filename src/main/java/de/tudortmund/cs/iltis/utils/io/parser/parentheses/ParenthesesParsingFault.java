package de.tudortmund.cs.iltis.utils.io.parser.parentheses;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFault;
import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultReason;
import java.util.Objects;

/** A specialized formula fault which includes the type of parentheses. */
public class ParenthesesParsingFault extends ParsingFault {

    private ParenthesesType type;

    /**
     * Creates a new fault.
     *
     * @throws NullPointerException if the type is null
     */
    public ParenthesesParsingFault(
            ParsingFaultReason reason,
            int line,
            int charPositionInLine,
            String text,
            String partOfInput,
            ParenthesesType type) {
        super(reason, line, charPositionInLine, text, partOfInput);
        Objects.requireNonNull(type);
        this.type = type;
    }

    /**
     * @return the parentheses type, never null
     */
    public ParenthesesType getParenthesesType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + type.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        ParenthesesParsingFault other = (ParenthesesParsingFault) obj;
        if (type != other.type) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ParenthesesParsingFault [reason = "
                + getReason()
                + ", line = "
                + line
                + ", charPositionInLine = "
                + charPositionInLine
                + ", text = "
                + text
                + ", partOfInput = "
                + partOfInput
                + ", type = "
                + type
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
    protected ParenthesesParsingFault clone() {
        return new ParenthesesParsingFault(
                getReason(), line, charPositionInLine, text, partOfInput, type);
    }

    /** for serialization */
    private static final long serialVersionUID = 1L;

    /** for GWT serialization */
    @SuppressWarnings("unused")
    private ParenthesesParsingFault() {}
}
