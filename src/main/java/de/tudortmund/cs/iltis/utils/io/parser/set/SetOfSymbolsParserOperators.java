package de.tudortmund.cs.iltis.utils.io.parser.set;

import de.tudortmund.cs.iltis.utils.io.parser.general.ParsableSymbol;

public enum SetOfSymbolsParserOperators implements ParsableSymbol {
    OBRACE,
    CBRACE,
    COMMA,
    WHITESPACE,
    INDEXED_SYMBOL;

    @Override
    public int getTokenType() {
        switch (this) {
            case OBRACE:
                return SetOfSymbolsParser.OBRACE;
            case CBRACE:
                return SetOfSymbolsParser.CBRACE;
            case COMMA:
                return SetOfSymbolsParser.COMMA;
            case WHITESPACE:
                return SetOfSymbolsParser.WHITESPACE;
            case INDEXED_SYMBOL:
                return SetOfSymbolsParser.INDEXED_SYMBOL;
        }
        throw new RuntimeException("unreachable");
    }
}
