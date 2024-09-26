package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.function.SerializablePredicate;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Matches any forest.
 *
 * @param <MatchedT> the type of tree to be matched
 * @see TreePattern
 */
public class AnyPattern<MatchedT extends Tree<MatchedT>> extends PredicatePattern<MatchedT> {

    private static SerializablePredicate<List<?>> PREDICATE = forest -> true;

    public AnyPattern() {
        super((SerializablePredicate<List<MatchedT>>) (Object) PREDICATE);
    }

    public AnyPattern(@NotNull final IndexedSymbol name) {
        super(name, (SerializablePredicate<List<MatchedT>>) (Object) PREDICATE);
    }

    public AnyPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>
                            eqTester) {
        super(name, eqTester, (SerializablePredicate<List<MatchedT>>) (Object) PREDICATE);
    }

    @Override
    public PatternType getType() {
        return TreePatternType.AnyPattern;
    }

    @Override
    public String toString() {
        return "AnyPattern [id=" + name + "]";
    }

    @Override
    public AnyPattern<MatchedT> clone() {
        return new AnyPattern<>(name, eqTester);
    }

    @Override
    protected AnyPattern<MatchedT> cloneWithIteratedName(int iteration) {
        return new AnyPattern<>(iterateName(name, iteration), eqTester);
    }
}
