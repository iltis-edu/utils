package de.tudortmund.cs.iltis.utils.io.parser.parentheses;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFault;
import java.util.Objects;

/**
 * A specialized formula fault which includes the parentheses type expected to be closed and the
 * type actually found in the input.
 */
public class ParenthesesMismatchParsingFault extends ParsingFault {

    private ParenthesesType actualType;
    private ParenthesesType expectedType;

    /**
     * Creates a new fault.
     *
     * @throws NullPointerException if any of the two types is null
     */
    public ParenthesesMismatchParsingFault(
            int line,
            int charPositionInLine,
            String text,
            String partOfInput,
            ParenthesesType actualType,
            ParenthesesType expectedType) {
        super(
                ParenthesesParsingFaultReason.PARENTHESES_MISMATCH,
                line,
                charPositionInLine,
                text,
                partOfInput);
        Objects.requireNonNull(actualType);
        Objects.requireNonNull(expectedType);
        this.actualType = actualType;
        this.expectedType = expectedType;
    }

    /**
     * @return the expected type, to be closed, never null
     */
    public ParenthesesType getTypeExpectedToBeClosed() {
        return expectedType;
    }

    /**
     * @return the right type, never null
     */
    public ParenthesesType getActuallyClosedType() {
        return actualType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        /**
         * @CodeReview Both types can not be null. --> right you are. Fixed.
         */
        result = prime * result + actualType.hashCode();
        result = prime * result + expectedType.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        ParenthesesMismatchParsingFault other = (ParenthesesMismatchParsingFault) obj;
        if (actualType != other.actualType) return false;
        if (expectedType != other.expectedType) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ParenthesesMismatchParsingFault [line = "
                + line
                + ", charPositionInLine = "
                + charPositionInLine
                + ", text = "
                + text
                + ", partOfInput = "
                + partOfInput
                + ", actualType = "
                + actualType
                + ", expectedType = "
                + expectedType
                + "]";
    }

    @Override
    protected ParenthesesMismatchParsingFault clone() {
        return new ParenthesesMismatchParsingFault(
                line, charPositionInLine, text, partOfInput, actualType, expectedType);
    }

    /** for serialization */
    private static final long serialVersionUID = 1L;

    /** for GWT serialization */
    @SuppressWarnings("unused")
    private ParenthesesMismatchParsingFault() {}
}
