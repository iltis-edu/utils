package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.collections.SequencePartitionIterator;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.ConjunctionTreeMatchIterator;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.QueueMatchIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Matches a forest with its subpatterns in a flexible way.
 *
 * <p>Tries to find an n-partition of the forest, so that the i-th of the n subpatterns matches the
 * i-th part of the partition. The n-partitions are created by the {@link
 * SequencePartitionIterator}.
 *
 * @param <MatchedT> the type of tree to be matched
 * @see TreePattern
 */
public class FlexibleArityForestPattern<MatchedT extends Tree<MatchedT>>
        extends TreePattern<MatchedT> {

    public class FlexibleArityForestPatternMatchIterator extends QueueMatchIterator<MatchedT> {

        private Iterator<List<List<MatchedT>>> seqParIt;
        private Iterator<Match<MatchedT>> conjIt;

        public FlexibleArityForestPatternMatchIterator(
                @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
            super(match, forest);
        }

        @Override
        protected boolean initMatching() {
            if (!isConsistentWithThisPatternsName(match, forest)) return false;
            conjIt = null;
            seqParIt = new SequencePartitionIterator<>(forest, getNumberOfChildren());
            return true;
        }

        @Override
        protected Match<MatchedT> calculateNextMatch() {
            while ((conjIt != null && conjIt.hasNext()) || seqParIt.hasNext()) {
                if (conjIt != null && conjIt.hasNext()) {
                    Match<MatchedT> result = conjIt.next();
                    return withThisPatternsDefinition(result, forest);
                }

                List<List<MatchedT>> forestPartition = seqParIt.next();
                List<Iterator<Match<MatchedT>>> matchIterators =
                        new ArrayList<>(getNumberOfChildren());
                for (int i = 0; i < getNumberOfChildren(); i++) {
                    matchIterators.add(getChild(i).matchIterator(match, forestPartition.get(i)));
                }
                conjIt = ConjunctionTreeMatchIterator.fromIterators(matchIterators);
            }

            return null;
        }

        /** for GWT serialization */
        @SuppressWarnings("unused")
        private FlexibleArityForestPatternMatchIterator() {}
    }

    @SafeVarargs
    public FlexibleArityForestPattern(@NotNull final TreePattern<MatchedT>... subpatterns) {
        super(false, subpatterns);
    }

    public FlexibleArityForestPattern(
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, subpatterns);
    }

    public FlexibleArityForestPattern(
            @NotNull final TreePattern<MatchedT> subpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, subpattern, subpatterns);
    }

    @SafeVarargs
    public FlexibleArityForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final TreePattern<MatchedT>... subpatterns) {
        super(false, name, subpatterns);
    }

    public FlexibleArityForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, name, subpatterns);
    }

    public FlexibleArityForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final TreePattern<MatchedT> subpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, name, subpattern, subpatterns);
    }

    @SafeVarargs
    public FlexibleArityForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT>... subpatterns) {
        super(false, name, eqTester, subpatterns);
    }

    public FlexibleArityForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, name, eqTester, subpatterns);
    }

    public FlexibleArityForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT> subpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, name, eqTester, subpattern, subpatterns);
    }

    @Override
    public PatternType getType() {
        return TreePatternType.FlexibleArityForestPattern;
    }

    @Override
    public Iterator<Match<MatchedT>> matchIterator(
            @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
        return new FlexibleArityForestPatternMatchIterator(match, forest);
    }

    @Override
    public List<MatchedT> createForest(@NotNull final Match<MatchedT> match)
            throws CreateException {
        return createForestByMatchAndNameOrSubpatterns(match);
    }

    @Override
    public String toString() {
        return "FlexibleArityForestPattern [id=" + name + ", subpatterns=" + children + "]";
    }

    @Override
    public FlexibleArityForestPattern<MatchedT> clone() {
        return new FlexibleArityForestPattern<>(name, eqTester, getClonedChildren());
    }

    @Override
    protected FlexibleArityForestPattern<MatchedT> cloneWithIteratedName(int iteration) {
        return new FlexibleArityForestPattern<>(
                iterateName(name, iteration),
                eqTester,
                getClonedChildrenWithIteratedNames(iteration));
    }

    /** for GWT serialization */
    private FlexibleArityForestPattern() {
        super(false);
    }
}
