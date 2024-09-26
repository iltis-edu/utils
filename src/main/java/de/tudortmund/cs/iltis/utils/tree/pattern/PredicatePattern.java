package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.function.SerializablePredicate;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.SingleMatchIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * Matches any forest which satisfies the given predicate.
 *
 * @param <MatchedT> the type of tree to be matched
 * @see TreePattern
 */
public class PredicatePattern<MatchedT extends Tree<MatchedT>> extends TreePattern<MatchedT> {

    protected SerializablePredicate<List<MatchedT>> predicate;

    public class PredicatePatternMatchIterator extends SingleMatchIterator<MatchedT> {

        public PredicatePatternMatchIterator(
                @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
            super(match, forest);
        }

        @Override
        protected Match<MatchedT> calculateMatch() {
            if (!predicate.test(forest)) return null;
            return withThisPatternsDefinition(match, forest);
        }
    }

    public PredicatePattern(@NotNull final SerializablePredicate<List<MatchedT>> predicate) {
        super(true);
        this.predicate = predicate;
    }

    public PredicatePattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializablePredicate<List<MatchedT>> predicate) {
        super(true, name);
        this.predicate = predicate;
    }

    public PredicatePattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final SerializablePredicate<List<MatchedT>> predicate) {
        super(true, name, eqTester);
        this.predicate = predicate;
    }

    /**
     * Utility method to convert a predicate on a tree to a predicate which at first checks for a
     * tree and after that evaluates the given predicate.
     *
     * @param predicate the predicate
     * @return the new predicate as described above
     */
    public static <MatchedT extends Tree<MatchedT>>
            SerializablePredicate<List<MatchedT>> treeToForestPredicate(
                    @NotNull final SerializablePredicate<MatchedT> predicate) {
        return forest -> forest.size() == 1 && predicate.test(forest.get(0));
    }

    /**
     * Utility method that returns a predicate accepting every forest, i.e. everything.
     *
     * @return the constant-true predicate
     */
    public static <MatchedT extends Tree<MatchedT>>
            SerializablePredicate<List<MatchedT>> anyForestPredicate() {
        return forest -> true;
    }

    /**
     * Utility method that returns a predicate accepting every tree, i.e. every forest of size 1
     *
     * @return the predicate described above
     */
    public static <MatchedT extends Tree<MatchedT>>
            SerializablePredicate<List<MatchedT>> anyTreePredicate() {
        return forest -> forest.size() == 1;
    }

    @Override
    public PatternType getType() {
        return TreePatternType.PredicatePattern;
    }

    @Override
    public Iterator<Match<MatchedT>> matchIterator(
            @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
        return new PredicatePatternMatchIterator(match, forest);
    }

    @Override
    public List<MatchedT> createForest(@NotNull final Match<MatchedT> match)
            throws CreateException {
        return createForestByMatchAndName(match);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
        return result;
    }

    /**
     * Checks whether {@code this} and the given object are equal
     *
     * @param obj the object to compare {@code this} with
     * @return {@code true} if both objects are equal, {@code false} otherwise
     *     <p><b>Warning:</b> this method checks the equality of the {@code predicate} field which
     *     is a lambda. Equality checks for functions in general are impossible and thus Java
     *     compares the object identity of two lambdas, which may or may not be what you want.
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj)
                && Objects.equals(predicate, (((PredicatePattern<?>) obj).predicate));
    }

    @Override
    public String toString() {
        return "PredicatePattern [id=" + name + "]";
    }

    @Override
    public PredicatePattern<MatchedT> clone() {
        return new PredicatePattern<>(name, eqTester, predicate);
    }

    @Override
    protected PredicatePattern<MatchedT> cloneWithIteratedName(int iteration) {
        return new PredicatePattern<>(iterateName(name, iteration), eqTester, predicate);
    }

    /** for GWT serialization */
    protected PredicatePattern() {
        super(true);
    }
}
