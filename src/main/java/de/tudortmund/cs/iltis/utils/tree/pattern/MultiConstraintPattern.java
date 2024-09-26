package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.ConjunctionTreeMatchIterator;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.MatchIterator;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.QueueMatchIterator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

/**
 * Pattern, that matches forests and trees if multiple constraints are fulfilled. These constaints
 * are given by the children of this pattern and specified in the constructor.
 *
 * @param <MatchedT> the type of tree to be matched
 * @see TreePattern
 */
public class MultiConstraintPattern<MatchedT extends Tree<MatchedT>> extends TreePattern<MatchedT> {

    public class MultiConstraintPatternMatchIterator extends QueueMatchIterator<MatchedT> {

        private Iterator<Match<MatchedT>> conjIt;

        public MultiConstraintPatternMatchIterator(
                @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
            super(match, forest);
        }

        @Override
        protected boolean initMatching() {
            if (!isConsistentWithThisPatternsName(match, forest)) return false;
            if (isLeaf()) return true;

            List<Iterator<Match<MatchedT>>> matchIterators =
                    children.stream()
                            .map(childPattern -> childPattern.matchIterator(match, forest))
                            .collect(Collectors.toList());
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
        private MultiConstraintPatternMatchIterator() {}
    }

    public MultiConstraintPattern() {
        super(false);
    }

    @SafeVarargs
    public MultiConstraintPattern(@NotNull final TreePattern<MatchedT>... subpatterns) {
        super(false, subpatterns);
    }

    public MultiConstraintPattern(
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, subpatterns);
    }

    public MultiConstraintPattern(
            @NotNull final TreePattern<MatchedT> subpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, subpattern, subpatterns);
    }

    public MultiConstraintPattern(@NotNull final IndexedSymbol name) {
        super(false, name);
    }

    @SafeVarargs
    public MultiConstraintPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final TreePattern<MatchedT>... subpatterns) {
        super(false, name, subpatterns);
    }

    public MultiConstraintPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, name, subpatterns);
    }

    public MultiConstraintPattern(
            @NotNull final IndexedSymbol name,
            TreePattern<MatchedT> subpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, name, subpattern, subpatterns);
    }

    public MultiConstraintPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>
                            eqTester) {
        super(false, name, eqTester);
    }

    @SafeVarargs
    public MultiConstraintPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT>... subpatterns) {
        super(false, name, eqTester, subpatterns);
    }

    public MultiConstraintPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, name, eqTester, subpatterns);
    }

    public MultiConstraintPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final TreePattern<MatchedT> subpattern,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, name, eqTester, subpattern, subpatterns);
    }

    @Override
    public PatternType getType() {
        return TreePatternType.MultiConstraintPattern;
    }

    @Override
    public MatchIterator<MatchedT> matchIterator(
            @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
        return new MultiConstraintPatternMatchIterator(match, forest);
    }

    /**
     * Tries to create a forest, first by name, and after that by calling {@link
     * #createForest(Match)} for each child. Only fails when none of its subpatterns can create a
     * forest.
     */
    @Override
    public List<MatchedT> createForest(@NotNull final Match<MatchedT> match)
            throws CreateException {
        try {
            return createForestByMatchAndName(match);
        } catch (CreateException e1) {
            for (TreePattern<MatchedT> childPattern : children) {
                try {
                    return childPattern.createForest(match);
                } catch (CreateException e2) {
                }
            }
        }
        throw new CreateException("No pattern of a multi constraint pattern could create forest");
    }

    @Override
    public String toString() {
        return "MultiConstraintPattern [id=" + name + ", subpatterns=" + children + "]";
    }

    @Override
    public MultiConstraintPattern<MatchedT> clone() {
        return new MultiConstraintPattern<>(name, eqTester, getClonedChildren());
    }

    @Override
    protected MultiConstraintPattern<MatchedT> cloneWithIteratedName(int iteration) {
        return new MultiConstraintPattern<>(
                iterateName(name, iteration),
                eqTester,
                getClonedChildrenWithIteratedNames(iteration));
    }
}
