package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.ConjunctionTreeMatchIterator;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.MatchIterator;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.QueueMatchIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Matches a forest iff every i-th child pattern matches the i-th tree of the forest.
 *
 * @param <MatchedT> the type of tree to be matched
 * @see TreePattern
 */
public class FixedArityForestPattern<MatchedT extends Tree<MatchedT>>
        extends TreePattern<MatchedT> {

    public class FixedArityForestPatternMatchIterator extends QueueMatchIterator<MatchedT> {

        private Iterator<Match<MatchedT>> conjIt;

        public FixedArityForestPatternMatchIterator(
                @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
            super(match, forest);
        }

        @Override
        protected boolean initMatching() {
            if (!isConsistentWithThisPatternsName(match, forest)) return false;
            if (getNumberOfChildren() != forest.size()) return false;

            List<Iterator<Match<MatchedT>>> matchIterators = new ArrayList<>(getNumberOfChildren());
            for (int i = 0; i < getNumberOfChildren(); i++) {
                matchIterators.add(getChild(i).matchIterator(match, forest.get(i)));
            }
            conjIt = ConjunctionTreeMatchIterator.fromIterators(matchIterators);
            return true;
        }

        @Override
        protected Match<MatchedT> calculateNextMatch() {
            if (conjIt.hasNext()) return withThisPatternsDefinition(conjIt.next(), forest);
            return null;
        }

        /** for GWT serialization */
        @SuppressWarnings("unused")
        private FixedArityForestPatternMatchIterator() {}
    }

    @SafeVarargs
    public FixedArityForestPattern(@NotNull final TreePattern<MatchedT>... childpatterns) {
        super(false, childpatterns);
    }

    public FixedArityForestPattern(
            @NotNull final Iterable<? extends TreePattern<MatchedT>> childpatterns) {
        super(false, childpatterns);
    }

    public FixedArityForestPattern(
            @NotNull final TreePattern<MatchedT> childpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> childpatterns) {
        super(false, childpattern, childpatterns);
    }

    @SafeVarargs
    public FixedArityForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final TreePattern<MatchedT>... childpatterns) {
        super(false, name, childpatterns);
    }

    public FixedArityForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> childpatterns) {
        super(false, name, childpatterns);
    }

    public FixedArityForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final TreePattern<MatchedT> childpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> childpatterns) {
        super(false, name, childpattern, childpatterns);
    }

    @SafeVarargs
    public FixedArityForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT>... childpatterns) {
        super(false, name, eqTester, childpatterns);
    }

    public FixedArityForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> childpatterns) {
        super(false, name, eqTester, childpatterns);
    }

    public FixedArityForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT> childpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> childpatterns) {
        super(false, name, eqTester, childpattern, childpatterns);
    }

    @Override
    public PatternType getType() {
        return TreePatternType.FixedArityForestPattern;
    }

    @Override
    public MatchIterator<MatchedT> matchIterator(
            @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
        return new FixedArityForestPatternMatchIterator(match, forest);
    }

    @Override
    public List<MatchedT> createForest(@NotNull final Match<MatchedT> match)
            throws CreateException {
        return createForestByMatchAndNameOrSubpatterns(match);
    }

    @Override
    public String toString() {
        return "FixedArityForestPattern [id=" + name + ", subpatterns=" + children + "]";
    }

    @Override
    public FixedArityForestPattern<MatchedT> clone() {
        return new FixedArityForestPattern<>(name, eqTester, getClonedChildren());
    }

    @Override
    protected FixedArityForestPattern<MatchedT> cloneWithIteratedName(int iteration) {
        return new FixedArityForestPattern<>(
                iterateName(name, iteration),
                eqTester,
                getClonedChildrenWithIteratedNames(iteration));
    }

    /** for GWT serialization */
    private FixedArityForestPattern() {
        super(false);
    }
}
