package de.tudortmund.cs.iltis.utils.io.parser.symbol;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.collections.Pair;
import de.tudortmund.cs.iltis.utils.function.SerializableFunction;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This splitting policy splits the given string based on a predefined and fixed set of allowed
 * {@link IndexedSymbol}s.
 */
public class FiniteSetSymbolSplittingPolicy extends BaseSymbolSplittingPolicy {

    private RegularSymbolSplittingPolicy policy;

    private static final SerializableFunction<IndexedSymbol, String> SUBSCRIPT_FIRST =
            is -> {
                String res = is.getName();
                res += is.hasSubscript() ? "_" + is.getSubscript() : "";
                res += is.hasSuperscript() ? "^" + is.getSuperscript() : "";
                return res;
            };

    private static final SerializableFunction<IndexedSymbol, String> SUPERSCRIPT_FIRST =
            is -> {
                String res = is.getName();
                res += is.hasSuperscript() ? "^" + is.getSuperscript() : "";
                res += is.hasSubscript() ? "_" + is.getSubscript() : "";
                return res;
            };

    public FiniteSetSymbolSplittingPolicy(Set<IndexedSymbol> allowedSymbols) {
        this(allowedSymbols, Arrays.asList(SUBSCRIPT_FIRST, SUPERSCRIPT_FIRST));
    }

    public FiniteSetSymbolSplittingPolicy(
            Set<IndexedSymbol> allowedSymbols,
            Collection<Function<IndexedSymbol, String>> allToStringVariants) {
        Objects.requireNonNull(allowedSymbols);
        Objects.requireNonNull(allToStringVariants);
        Function<IndexedSymbol, Stream<String>> f =
                is -> allToStringVariants.stream().map(toString -> toString.apply(is));
        Collection<String> possibleStringRepresentations =
                allowedSymbols.stream().flatMap(f).collect(Collectors.toList());
        policy = RegularSymbolSplittingPolicy.CREATE_OF_SYMBOLS(possibleStringRepresentations);
    }

    public RegularSymbolSplittingPolicy toRegularSymbolSplittingPolicy() {
        return policy;
    }

    @Override
    public List<Pair<Integer, IndexedSymbol>> splitSymbols(String input)
            throws SymbolsNotSplittableException {
        return input.isEmpty() ? new LinkedList<>() : policy.splitSymbols(input);
    }

    /** For serialization */
    @SuppressWarnings("unused")
    private FiniteSetSymbolSplittingPolicy() {}
}
