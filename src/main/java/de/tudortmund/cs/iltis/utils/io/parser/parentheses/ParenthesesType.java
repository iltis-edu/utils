package de.tudortmund.cs.iltis.utils.io.parser.parentheses;

import java.io.Serializable;

/** A simple enum for types of parentheses. */
public enum ParenthesesType implements Serializable {
    PARENTHESES,
    BRACKETS,
    BRACES;

    /**
     * Returns an example string for a parenthesis of this type.
     *
     * @param opening whether a string for an opening parenthesis shall be returned, else a string
     *     for a closing one is returned
     * @return the symbol, e.g. "(" or ")"
     */
    public String getText(boolean opening) {
        switch (this) {
            case PARENTHESES:
                return opening ? "(" : ")";
            case BRACKETS:
                return opening ? "[" : "]";
            case BRACES:
                return opening ? "{" : "}";
            default:
                throw new RuntimeException("Not reachable");
        }
    }

    /**
     * Returns an example string for an opening parenthesis of this type.
     *
     * @return the symbol, e.g. "("
     */
    public String getOpeningText() {
        return getText(true);
    }

    /**
     * Returns an example string for a closing parenthesis of this type.
     *
     * @return the symbol, e.g. ")"
     */
    public String getClosingText() {
        return getText(false);
    }
}
