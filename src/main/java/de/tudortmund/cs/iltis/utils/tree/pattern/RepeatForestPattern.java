package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.ConjunctionTreeMatchIterator;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.MatchIterator;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.QueueMatchIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Pattern, to match a forest consisting of arbitrary many trees all matching the same pattern.
 * Corresponds to the Kleene star in regular expressions. Therefore this pattern has only one
 * subpattern, i.e. child, specified in the constructor.
 *
 * <p>If any descendant pattern (i.e. a sub-sub-...-subpattern) is <b>named</b>, this pattern
 * enforces that the (sub)tree matched by this descendant pattern is the same in each
 * iteration/repetition. Sometimes, that is what you want, sometimes it isn't. In the latter case
 * you can name a descendant pattern like <code>xyz[]^1_2</code>. Then, in each iteration this
 * pattern's name is iterated using {@link #cloneWithIteratedName(int)}, thus the names of the
 * patterns used for different iterations differ. Inside one iterations however, two previously
 * identically named patterns remain identically named. This process supports nested
 * RepeatForestPatterns when you specify multiple pairs of brackets (like for a multidimensional
 * array).
 *
 * @param <MatchedT> the type of tree to be matched
 * @see TreePattern
 */
public class RepeatForestPattern<MatchedT extends Tree<MatchedT>> extends TreePattern<MatchedT> {

    public class RepeatForestPatternMatchIterator extends QueueMatchIterator<MatchedT> {

        private Iterator<Match<MatchedT>> forestMatchIterator;

        public RepeatForestPatternMatchIterator(
                @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
            super(match, forest);
        }

        @Override
        protected boolean initMatching() {
            if (!isConsistentWithThisPatternsName(match, forest)) return false;
            List<Iterator<Match<MatchedT>>> matchIterators = new ArrayList<>(forest.size());
            for (int i = 0; i < forest.size(); i++) {
                matchIterators.add(
                        getChild(0).cloneWithIteratedName(i).matchIterator(match, forest.get(i)));
            }
            forestMatchIterator = ConjunctionTreeMatchIterator.fromIterators(matchIterators);
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
        private RepeatForestPatternMatchIterator() {}
    }

    public RepeatForestPattern(@NotNull final TreePattern<MatchedT> subpattern) {
        super(true, Arrays.asList(subpattern));
    }

    public RepeatForestPattern(
            @NotNull final IndexedSymbol name, @NotNull final TreePattern<MatchedT> subpattern) {
        super(true, name, Arrays.asList(subpattern));
    }

    public RepeatForestPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT> subpattern) {
        super(true, name, eqTester, Arrays.asList(subpattern));
    }

    @Override
    public PatternType getType() {
        return TreePatternType.RepeatForestPattern;
    }

    @Override
    public MatchIterator<MatchedT> matchIterator(
            @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
        return new RepeatForestPatternMatchIterator(match, forest);
    }

    @Override
    public List<MatchedT> createForest(@NotNull final Match<MatchedT> match)
            throws CreateException {
        return createForestByMatchAndName(match);
    }

    @Override
    public String toString() {
        return "RepeatForestPattern [id=" + name + ", subpattern=" + getChild(0) + "]";
    }

    @Override
    public RepeatForestPattern<MatchedT> clone() {
        return new RepeatForestPattern<>(name, eqTester, getClonedChildren().get(0));
    }

    @Override
    protected RepeatForestPattern<MatchedT> cloneWithIteratedName(int iteration) {
        return new RepeatForestPattern<>(
                iterateName(name, iteration),
                eqTester,
                getClonedChildrenWithIteratedNames(iteration).get(0));
    }

    /** for GWT serialization */
    private RepeatForestPattern() {
        super(true);
    }
}
