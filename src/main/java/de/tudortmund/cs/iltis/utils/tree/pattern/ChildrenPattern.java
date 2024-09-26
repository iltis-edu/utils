package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.MatchIterator;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.QueueMatchIterator;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Matches a tree by its child patterns. The single subpattern specified in the constructor should
 * match the forest of all children of the tree to be matched.
 *
 * @param <MatchedT> the type of tree to be matched
 * @see TreePattern
 */
public class ChildrenPattern<MatchedT extends Tree<MatchedT>> extends TreePattern<MatchedT> {

    public class ChildrenPatternMatchIterator extends QueueMatchIterator<MatchedT> {

        private Iterator<Match<MatchedT>> forestMatchIterator;

        public ChildrenPatternMatchIterator(
                @NotNull final Match<MatchedT> match, @NotNull final MatchedT tree) {
            super(match, tree);
        }

        @Override
        protected boolean initMatching() {
            if (!isConsistentWithThisPatternsName(match, forest)) return false;
            forestMatchIterator = getChild(0).matchIterator(match, getTree().getChildren());
            return true;
        }

        @Override
        protected Match<MatchedT> calculateNextMatch() {
            if (forestMatchIterator.hasNext())
                return withThisPatternsDefinition(forestMatchIterator.next(), forest);
            return null;
        }

        /** for GWT serialization */
        @SuppressWarnings("unused")
        private ChildrenPatternMatchIterator() {}
    }

    public ChildrenPattern(@NotNull final TreePattern<MatchedT> subpattern) {
        super(true, Arrays.asList(subpattern));
    }

    public ChildrenPattern(
            @NotNull final IndexedSymbol name, @NotNull final TreePattern<MatchedT> subpattern) {
        super(true, name, Arrays.asList(subpattern));
    }

    public ChildrenPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT> subpattern) {
        super(true, name, eqTester, Arrays.asList(subpattern));
    }

    @Override
    public PatternType getType() {
        return TreePatternType.ChildrenPattern;
    }

    @Override
    public MatchIterator<MatchedT> matchIterator(
            @NotNull final Match<MatchedT> match, @NotNull final MatchedT tree) {
        return new ChildrenPatternMatchIterator(match, tree);
    }

    @Override
    public MatchedT createTree(@NotNull final Match<MatchedT> match) throws CreateException {
        return createTreeByMatchAndName(match);
    }

    @Override
    public String toString() {
        return "ChildrenPattern [id=" + name + ", subpattern=" + getChild(0) + "]";
    }

    @Override
    public ChildrenPattern<MatchedT> clone() {
        return new ChildrenPattern<>(name, eqTester, getClonedChildren().get(0));
    }

    @Override
    protected ChildrenPattern<MatchedT> cloneWithIteratedName(int iteration) {
        return new ChildrenPattern<>(
                iterateName(name, iteration),
                eqTester,
                getClonedChildrenWithIteratedNames(iteration).get(0));
    }

    /** for GWT serialization */
    private ChildrenPattern() {
        super(true);
    }
}
