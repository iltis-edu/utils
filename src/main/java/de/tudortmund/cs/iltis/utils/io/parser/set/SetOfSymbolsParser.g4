parser grammar SetOfSymbolsParser;

options { superClass=AbstractParser; }
tokens {
    OBRACE, CBRACE,
    COMMA,
    WHITESPACE,
    INDEXED_SYMBOL
}

@header {
import de.tudortmund.cs.iltis.utils.io.parser.general.AbstractParser;
}

initSet : set EOF;

set: OBRACE content CBRACE;

content: (symbol COMMA)* symbol | ;

symbol: symb=INDEXED_SYMBOL;


