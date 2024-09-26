package de.tudortmund.cs.iltis.utils.io.parser.general;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultReason;

/** Enum for fault reasons which can appear for multiple grammars. */
public enum GeneralParsingFaultReason implements ParsingFaultReason {
    /** default reason if an unexpected (= not separately handled) fault occurs */
    VARIOUS,
    /**
     * default reason, if the lexer (as opposed to parser) complains; will appear when a substring
     * is encountered that neither is an operator nor can be parsed as indexed symbol
     */
    INVALID_SYMBOL,
    /** different operators are mixed on one level */
    PARENTHESES_MISSING,
    /** there are two operators (e.g. and, or, ...) without any operand (e.g. formula) between */
    OPERAND_MISSING,
    /** there are two operands (e.g. formulae) without any operator between */
    OPERATOR_MISSING;

    /** {@inheritDoc} */
    @Override
    public int getGroup() {
        switch (this) {
            case INVALID_SYMBOL:
                return 100;
            case VARIOUS:
                return 300;
            case PARENTHESES_MISSING:
            case OPERATOR_MISSING:
            case OPERAND_MISSING:
                return 500;
            default:
                return 0;
        }
    }
}
