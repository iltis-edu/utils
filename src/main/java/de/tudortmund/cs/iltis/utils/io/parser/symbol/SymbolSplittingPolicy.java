package de.tudortmund.cs.iltis.utils.io.parser.symbol;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.collections.Pair;
import java.io.Serializable;
import java.util.List;

/**
 * An interface describing a policy to split an input string into multiple indexed symbols. In which
 * way and according to which criteria the splitting process runs has to be specified in
 * implementing classes.
 */
public interface SymbolSplittingPolicy extends Serializable {

    /**
     * Splits the given input string into multiple indexed symbols.
     *
     * <p><b>Important hint:</b> The concatenation of {@link IndexedSymbol#toString()} does not
     * necessarily produce the input string.
     *
     * @return A list with every element being a pair of an indexed symbol and the position in the
     *     input string (starting with 0) the parsing of this specific indexed symbol started.
     * @throws SymbolsNotSplittableException if input is not splittable in indexed symbols
     */
    List<Pair<Integer, IndexedSymbol>> splitSymbols(String input)
            throws SymbolsNotSplittableException;

    /**
     * Splits the given input string into multiple indexed symbols.
     *
     * <p><b>Important hint:</b> The concatenation of {@link IndexedSymbol#toString()} does not
     * necessarily produce the input string.
     *
     * @return An array with the splitted indexed symbols
     * @throws SymbolsNotSplittableException if input is not splittable in indexed symbols
     */
    default IndexedSymbol[] splitSymbolsToArray(String input) throws SymbolsNotSplittableException {
        List<Pair<Integer, IndexedSymbol>> result = splitSymbols(input);
        IndexedSymbol[] symbols = new IndexedSymbol[result.size()];
        for (int i = 0; i < symbols.length; i++) symbols[i] = result.get(i).second();
        return symbols;
    }

    /** Checks whether the given string can be converted in exactly one indexed symbol. */
    default boolean isSingleSymbol(String input) {
        try {
            IndexedSymbol[] symbols = splitSymbolsToArray(input);
            return symbols.length == 1;
        } catch (SymbolsNotSplittableException e) {
            return false;
        }
    }

    /**
     * Checks whether there is an input string that can be converted into the given indexed symbol.
     */
    boolean isExpressible(IndexedSymbol symbol);
}
