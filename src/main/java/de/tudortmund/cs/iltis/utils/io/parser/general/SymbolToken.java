package de.tudortmund.cs.iltis.utils.io.parser.general;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import java.util.Objects;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Pair;

/** A subclass of CommonToken which includes an {@link IndexedSymbol}. */
public class SymbolToken extends CommonToken {

    private static final long serialVersionUID = 1L;

    private IndexedSymbol symbol;

    /** for serialization */
    private SymbolToken() {
        super(0);
    }

    /** Constructs a new symbol token with the given values. */
    public SymbolToken(
            @NotNull Pair<TokenSource, CharStream> source,
            int type,
            int channel,
            int start,
            int stop,
            IndexedSymbol symbol) {
        super(source, type, channel, start, stop);
        Objects.requireNonNull(symbol);
        this.symbol = symbol;
    }

    /**
     * Constructs a new symbol token by transferring all properties from the given token and adding
     * the given symbol.
     */
    public SymbolToken(Token token, IndexedSymbol symbol) {
        super(token);
        Objects.requireNonNull(symbol);
        this.symbol = symbol;
    }

    /**
     * Constructs a new symbol token, taking the given type and symbol and transferring all other
     * properties from the given token. To the positionInLine and to the start and stop positions
     * the given offset is added.
     */
    public SymbolToken(
            Token token, int newType, int newStartOffset, int newEndOffset, IndexedSymbol symbol) {
        super(token);
        Objects.requireNonNull(symbol);
        this.symbol = symbol;

        type = newType;
        start += newStartOffset;
        stop = start + newEndOffset - newStartOffset;
        text = text.substring(newStartOffset, newEndOffset);
        charPositionInLine += newStartOffset;
    }

    public IndexedSymbol getSymbol() {
        return symbol;
    }
}
