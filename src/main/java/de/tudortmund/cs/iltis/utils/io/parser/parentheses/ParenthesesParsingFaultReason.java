package de.tudortmund.cs.iltis.utils.io.parser.parentheses;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultReason;

public enum ParenthesesParsingFaultReason implements ParsingFaultReason {
    OPENING_PAREN_MISSING,
    CLOSING_PAREN_MISSING,
    PARENTHESES_MISMATCH,
    PARENTHESES_GENERALLY_NOT_ALLOWED;

    /** {@inheritDoc} */
    @Override
    public int getGroup() {
        switch (this) {
            case PARENTHESES_GENERALLY_NOT_ALLOWED:
                return 190;
            case OPENING_PAREN_MISSING:
            case CLOSING_PAREN_MISSING:
            case PARENTHESES_MISMATCH:
                return 200;
            default:
                return 0;
        }
    }
}
