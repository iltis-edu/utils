package de.tudortmund.cs.iltis.utils.io.parsable;

import java.util.Locale;
import javax.xml.bind.annotation.*;

/**
 * A simple specialization of a <tt>ParsableDatum</tt>, used for XML elements and attributes, for
 * example. The datum is a <tt>String</tt> and provided as an XML value (see <tt>source</tt>).
 *
 * @param <T> the target type
 */
public abstract class ParsableEntry<T> extends ParsableSource<T, String, Object> {
    public enum CaseStyle {
        UNCHANGED,
        LOWER,
        UPPER
    }

    private CaseStyle caseStyle = CaseStyle.UNCHANGED;
    private boolean trim = true;

    @XmlValue private String source;

    private T value = null;

    public ParsableEntry() {
        this(null);
    }

    public ParsableEntry(String source) {
        this.source = source;
    }

    @Override
    public String getSource() {
        String result = source;
        if (source == null) {
            return null;
        }

        if (trim) result = result.trim();
        return cleanCase(result);
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Transform all characters in the input string to lower case.
     *
     * @return this {@code ParsableString} instance
     */
    public ParsableEntry<T> lowerCase() {
        caseStyle = CaseStyle.LOWER;
        return this;
    }

    /**
     * Transform all characters in the input string to upper case.
     *
     * @return this {@code ParsableString} instance
     */
    public ParsableEntry<T> upperCase() {
        caseStyle = CaseStyle.UPPER;
        return this;
    }

    /**
     * Leave the case of all characters in the input string unchanged.
     *
     * @return this {@code ParsableString} instance
     */
    public ParsableEntry<T> unchangedCase() {
        caseStyle = CaseStyle.UNCHANGED;
        return this;
    }

    private String cleanCase(String input) {
        switch (caseStyle) {
            case LOWER:
                return input.toLowerCase(Locale.ROOT);
            case UPPER:
                return input.toUpperCase(Locale.ROOT);
            default:
                return input;
        }
    }

    /**
     * Remove all leading and trailing whitespaces.
     *
     * @return this {@code ParsableString} instance
     */
    public ParsableEntry<T> trim() {
        return this.trim(true);
    }

    /**
     * Set the trim mode. If {@code true}, then all leading and trailing whitespaces are removed;
     * otherwise, they are kept.
     *
     * @return this {@code ParsableString} instance
     */
    public ParsableEntry<T> trim(boolean active) {
        trim = active;
        return this;
    }

    @Override
    public String toString() {
        return "Input: '" + source + "'";
    }
}
