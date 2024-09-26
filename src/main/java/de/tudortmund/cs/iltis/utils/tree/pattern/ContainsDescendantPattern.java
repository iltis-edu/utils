package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.QueueMatchIterator;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

/**
 * Matches if any descendant of one of the trees of the forest to be matched matches the given child
 * pattern.
 *
 * @param <MatchedT> the type of tree to be matched
 * @see TreePattern
 */
public class ContainsDescendantPattern<MatchedT extends Tree<MatchedT>>
        extends TreePattern<MatchedT> {

    public class ContainsDescendantPatternMatchIterator extends QueueMatchIterator<MatchedT> {

        private Iterator<MatchedT> descendantIterator;
        private Iterator<Match<MatchedT>> matchIterator;

        public ContainsDescendantPatternMatchIterator(
                @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
            super(match, forest);
        }

        @Override
        protected boolean initMatching() {
            if (!isConsistentWithThisPatternsName(match, forest)) return false;
            Set<MatchedT> descendants =
                    forest.stream()
                            .flatMap(tree -> tree.getDescendants().stream())
                            .collect(Collectors.toSet());
            descendantIterator = descendants.iterator();
            return true;
        }

        @Override
        protected Match<MatchedT> calculateNextMatch() {
            while (true) {
                if (matchIterator == null) {
                    if (descendantIterator.hasNext()) {
                        matchIterator = getChild(0).matchIterator(match, descendantIterator.next());
                    } else {
                        return null;
                    }
                }
                if (!matchIterator.hasNext()) {
                    matchIterator = null;
                } else {
                    return withThisPatternsDefinition(matchIterator.next(), forest);
                }
            }
        }

        /** for GWT serialization */
        @SuppressWarnings("unused")
        private ContainsDescendantPatternMatchIterator() {}
    }

    public ContainsDescendantPattern(@NotNull final TreePattern<MatchedT> subpattern) {
        super(true, Arrays.asList(subpattern));
    }

    public ContainsDescendantPattern(
            @NotNull final IndexedSymbol name, @NotNull final TreePattern<MatchedT> subpattern) {
        super(true, name, Arrays.asList(subpattern));
    }

    public ContainsDescendantPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT> subpattern) {
        super(true, name, eqTester, Arrays.asList(subpattern));
    }

    @Override
    public PatternType getType() {
        return TreePatternType.ContainsDescendantPattern;
    }

    @Override
    public Iterator<Match<MatchedT>> matchIterator(
            @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
        return new ContainsDescendantPatternMatchIterator(match, forest);
    }

    @Override
    public List<MatchedT> createForest(@NotNull final Match<MatchedT> match)
            throws CreateException {
        return createForestByMatchAndName(match);
    }

    @Override
    public String toString() {
        return "ContainsDescendantPattern [id=" + name + ", subpattern=" + getChild(0) + "]";
    }

    @Override
    public ContainsDescendantPattern<MatchedT> clone() {
        return new ContainsDescendantPattern<>(name, eqTester, getClonedChildren().get(0));
    }

    @Override
    protected ContainsDescendantPattern<MatchedT> cloneWithIteratedName(int iteration) {
        return new ContainsDescendantPattern<>(
                iterateName(name, iteration),
                eqTester,
                getClonedChildrenWithIteratedNames(iteration).get(0));
    }

    /** for GWT serialization */
    private ContainsDescendantPattern() {
        super(true);
    }
}
