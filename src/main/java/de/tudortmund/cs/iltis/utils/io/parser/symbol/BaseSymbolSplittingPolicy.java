package de.tudortmund.cs.iltis.utils.io.parser.symbol;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.collections.Pair;
import de.tudortmund.cs.iltis.utils.function.SerializableFunction;
import de.tudortmund.cs.iltis.utils.io.reader.general.Reader;
import de.tudortmund.cs.iltis.utils.io.reader.general.SafeTextIndexedSymbolReader;
import de.tudortmund.cs.iltis.utils.io.writer.general.SafeTextIndexedSymbolWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A basic symbol splitting policy that does not impose any restrictions and therefore does not
 * split any given symbol. For converting from strings to indexed symbols and vice versa, two
 * default functions are defined that delegate to {@link SafeTextIndexedSymbolReader} and {@link
 * SafeTextIndexedSymbolWriter} respectively.
 *
 * <p>This class is suitable as a base class for more advanced policies by overwriting {@link
 * #splitSymbolsToStrings(String)}.
 */
public class BaseSymbolSplittingPolicy implements SymbolSplittingPolicy {

    protected static final SerializableFunction<String, IndexedSymbol> DEFAULT_TO_INDEXED_SYMBOL =
            str -> new SafeTextIndexedSymbolReader().read(str);
    protected static final SerializableFunction<IndexedSymbol, String> DEFAULT_TO_STRING =
            symbol -> new SafeTextIndexedSymbolWriter().write(symbol);

    protected SerializableFunction<String, IndexedSymbol> toIndexedSymbol;
    protected SerializableFunction<IndexedSymbol, String> toString;

    /**
     * Creates a splitting policy that does not impose any restrictions and uses the constructor
     * {@link IndexedSymbol#IndexedSymbol(String)} and {@link IndexedSymbol#toString()} to convert
     * between strings and indexed symbols.
     */
    public BaseSymbolSplittingPolicy() {
        this(DEFAULT_TO_INDEXED_SYMBOL, DEFAULT_TO_STRING);
    }

    /**
     * Creates a splitting policy that does not impose any restrictions and uses the given functions
     * to convert between strings and indexed symbols.
     */
    public BaseSymbolSplittingPolicy(
            SerializableFunction<String, IndexedSymbol> toIndexedSymbol,
            SerializableFunction<IndexedSymbol, String> toString) {
        Objects.requireNonNull(toIndexedSymbol);
        Objects.requireNonNull(toString);
        this.toIndexedSymbol = toIndexedSymbol;
        this.toString = toString;
    }

    /**
     * Creates a splitting policy that does not impose any restrictions and uses the given reader
     * and writer to convert between strings and indexed symbols.
     */
    public BaseSymbolSplittingPolicy(
            Reader<IndexedSymbol> toIndexedSymbol, Writer<IndexedSymbol> toString) {
        Objects.requireNonNull(toIndexedSymbol);
        Objects.requireNonNull(toString);
        this.toIndexedSymbol = toIndexedSymbol::read;
        this.toString = toString::write;
    }

    @Override
    public List<Pair<Integer, IndexedSymbol>> splitSymbols(String input)
            throws SymbolsNotSplittableException {
        List<Pair<Integer, String>> oldResult = splitSymbolsToStrings(input);
        List<Pair<Integer, IndexedSymbol>> newResult = new ArrayList<>();

        try {
            for (Pair<Integer, String> oldPair : oldResult) {
                newResult.add(new Pair<>(oldPair.first(), toIndexedSymbol.apply(oldPair.second())));
            }
        } catch (Exception e) {
            throw new SymbolsNotSplittableException();
        }

        return newResult;
    }

    protected List<Pair<Integer, String>> splitSymbolsToStrings(String input)
            throws SymbolsNotSplittableException {
        List<Pair<Integer, String>> result = new ArrayList<>(1);
        result.add(new Pair<>(0, input));
        return result;
    }

    @Override
    public boolean isExpressible(IndexedSymbol symbol) {
        try {
            return isSingleSymbol(toString.apply(symbol));
        } catch (Exception e) {
            return false;
        }
    }
}
