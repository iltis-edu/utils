package de.tudortmund.cs.iltis.utils.io.parser.fault;

import de.tudortmund.cs.iltis.utils.io.parser.general.GeneralParsingFaultReason;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsableType;
import java.util.Objects;

/**
 * A specialized parsing fault which includes the two operator types that are placed on the same
 * level but need parentheses.
 */
public class ParenthesesMissingParsingFault extends ParsingFault {

    private ParsableType typeLeft;
    private ParsableType typeRight;

    /**
     * Creates a new fault.
     *
     * @throws NullPointerException if any of the two types is null
     */
    public ParenthesesMissingParsingFault(
            int line,
            int charPositionInLine,
            String text,
            String partOfInput,
            ParsableType typeLeft,
            ParsableType typeRight) {
        super(
                GeneralParsingFaultReason.PARENTHESES_MISSING,
                line,
                charPositionInLine,
                text,
                partOfInput);
        Objects.requireNonNull(typeLeft);
        Objects.requireNonNull(typeRight);
        this.typeLeft = typeLeft;
        this.typeRight = typeRight;
    }

    /**
     * @return the left type, never null
     */
    public ParsableType getTypeLeft() {
        return typeLeft;
    }

    /**
     * @return the right type, never null
     */
    public ParsableType getTypeRight() {
        return typeRight;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + typeLeft.hashCode();
        result = prime * result + typeRight.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        ParenthesesMissingParsingFault other = (ParenthesesMissingParsingFault) obj;
        return typeRight.equals(other.typeRight) && typeLeft.equals(other.typeLeft);
    }

    @Override
    public String toString() {
        return "ParenthesesMissingParsingFault [line = "
                + line
                + ", charPositionInLine = "
                + charPositionInLine
                + ", text = "
                + text
                + ", partOfInput = "
                + partOfInput
                + ", typeLeft = "
                + typeLeft
                + ", typeRight = "
                + typeRight
                + "]";
    }

    @Override
    protected ParenthesesMissingParsingFault clone() {
        return new ParenthesesMissingParsingFault(
                line, charPositionInLine, text, partOfInput, typeLeft, typeRight);
    }

    /** For serialization */
    private static final long serialVersionUID = 1L;

    /** For serialization */
    @SuppressWarnings("unused")
    private ParenthesesMissingParsingFault() {
        super();
    }
}
