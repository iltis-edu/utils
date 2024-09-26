package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.MatchIterator;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.NegationTreeMatchIterator;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.QueueMatchIterator;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * Matches the complement of its child pattern, i.e. iff its child pattern does not match.
 *
 * @param <MatchedT> the type of tree to be matched
 * @see TreePattern
 */
public class ComplementPattern<MatchedT extends Tree<MatchedT>> extends TreePattern<MatchedT> {

    public class ComplementPatternMatchIterator extends QueueMatchIterator<MatchedT> {

        private Iterator<Match<MatchedT>> negChildMatchesIterator;

        public ComplementPatternMatchIterator(Match<MatchedT> match, List<MatchedT> forest) {
            super(match, forest);
        }

        @Override
        protected boolean initMatching() {
            if (!isConsistentWithThisPatternsName(match, forest)) return false;
            Set<Match<MatchedT>> childMatches = getChild(0).getAllMatches(forest);
            negChildMatchesIterator = new NegationTreeMatchIterator<>(childMatches);
            return true;
        }

        @Override
        protected Match<MatchedT> calculateNextMatch() {
            Optional<? extends Match<MatchedT>> opMergedMatch = Optional.empty();
            while (negChildMatchesIterator.hasNext() && !opMergedMatch.isPresent())
                opMergedMatch = match.withMatch(negChildMatchesIterator.next());
            return withThisPatternsDefinition(opMergedMatch.orElse(null), forest);
        }

        /** for GWT serialization */
        @SuppressWarnings("unused")
        private ComplementPatternMatchIterator() {}
    }

    public ComplementPattern(@NotNull final TreePattern<MatchedT> subpattern) {
        super(true, Arrays.asList(subpattern));
    }

    public ComplementPattern(
            @NotNull final IndexedSymbol name, @NotNull final TreePattern<MatchedT> subpattern) {
        super(true, name, Arrays.asList(subpattern));
    }

    public ComplementPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT> subpattern) {
        super(true, name, eqTester, Arrays.asList(subpattern));
    }

    @Override
    public PatternType getType() {
        return TreePatternType.ComplementPattern;
    }

    @Override
    public MatchIterator<MatchedT> matchIterator(
            @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
        return new ComplementPatternMatchIterator(match, forest);
    }

    @Override
    public List<MatchedT> createForest(@NotNull final Match<MatchedT> match)
            throws CreateException {
        return createForestByMatchAndName(match);
    }

    @Override
    public String toString() {
        return "ComplementPattern [id=" + name + ", subpattern=" + getChild(0) + "]";
    }

    @Override
    public ComplementPattern<MatchedT> clone() {
        return new ComplementPattern<>(name, eqTester, getClonedChildren().get(0));
    }

    @Override
    protected ComplementPattern<MatchedT> cloneWithIteratedName(int iteration) {
        return new ComplementPattern<>(
                iterateName(name, iteration),
                eqTester,
                getClonedChildrenWithIteratedNames(iteration).get(0));
    }

    /** for GWT serialization */
    private ComplementPattern() {
        super(true);
    }
}
