package de.tudortmund.cs.iltis.utils.io.reader.general;

import de.tudortmund.cs.iltis.utils.collections.Fault;
import java.io.Serializable;

/**
 * A fault which contains suitable fields for reporting reading errors.
 *
 * @param <ReasonT> the reason
 */
public class ReadingFault<ReasonT extends Serializable> extends Fault<ReasonT> {

    /** zero-starting line number, where error occurred */
    protected int line;

    /** zero-starting position in line, where error occurred; tabs are counted as one character */
    protected int charPositionInLine;

    /**
     * text, which was read when the error occurred; typically this will be the text starting at
     * {@code charPositionInLine} but subclasses may specify a different behavior; can be empty but
     * not null
     */
    protected String text;

    public ReadingFault(ReasonT reason, int line, int charPositionInLine, String text) {
        super(reason);
        this.line = line;
        this.charPositionInLine = charPositionInLine;
        this.text = text != null ? text : "";
    }

    /**
     * @see #line
     */
    public int getLine() {
        return line;
    }

    /**
     * @see #charPositionInLine
     */
    public int getCharPositionInLine() {
        return charPositionInLine;
    }

    /**
     * @see ReadingFault#text
     */
    public String getText() {
        return text;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + charPositionInLine;
        result = prime * result + line;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        ReadingFault<?> other = (ReadingFault<?>) obj;
        if (charPositionInLine != other.charPositionInLine) return false;
        if (line != other.line) return false;
        if (text == null) {
            return other.text == null;
        } else return text.equals(other.text);
    }

    @Override
    public String toString() {
        return "ReadingFault [reason = "
                + getReason()
                + ", line = "
                + line
                + ", charPositionInLine = "
                + charPositionInLine
                + ", text = "
                + text
                + "]";
    }

    @Override
    protected ReadingFault<ReasonT> clone() {
        return new ReadingFault<>(getReason(), line, charPositionInLine, text);
    }

    /** For serialization */
    private static final long serialVersionUID = 1L;

    /** For serialization */
    protected ReadingFault() {}
}
