package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.MatchIterator;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.QueueMatchIterator;
import java.util.Iterator;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Matches iff any of its subpatterns matches.
 *
 * @param <MatchedT> the type of tree to be matched
 * @see TreePattern
 */
public class AlternativePattern<MatchedT extends Tree<MatchedT>> extends TreePattern<MatchedT> {

    public class AlternativePatternMatchIterator extends QueueMatchIterator<MatchedT> {

        private Iterator<Match<MatchedT>> childIterator;
        private int childCounter;

        public AlternativePatternMatchIterator(
                @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
            super(match, forest);
        }

        @Override
        protected boolean initMatching() {
            if (!isConsistentWithThisPatternsName(match, forest)) return false;
            if (isLeaf()) return false;
            childCounter = 0;
            return true;
        }

        @Override
        protected Match<MatchedT> calculateNextMatch() {
            while (childCounter < getNumberOfChildren()) {
                if (childIterator == null) {
                    childIterator = getChild(childCounter).matchIterator(match, forest);
                }
                if (!childIterator.hasNext()) {
                    childIterator = null;
                    childCounter++;
                } else {
                    Match<MatchedT> childMatch = childIterator.next();
                    return withThisPatternsDefinition(childMatch, forest);
                }
            }
            return null;
        }

        /** for GWT serialization */
        @SuppressWarnings("unused")
        private AlternativePatternMatchIterator() {}
    }

    // WITHOUT NAME

    public AlternativePattern() {
        super(false);
    }

    @SafeVarargs
    public AlternativePattern(@NotNull final TreePattern<MatchedT>... subpatterns) {
        super(false, subpatterns);
    }

    public AlternativePattern(
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, subpatterns);
    }

    public AlternativePattern(
            @NotNull final TreePattern<MatchedT> subpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, subpattern, subpatterns);
    }

    // WITH NAME, WITHOUT EQTESTER

    public AlternativePattern(@NotNull final IndexedSymbol name) {
        super(false, name);
    }

    @SafeVarargs
    public AlternativePattern(
            @NotNull final IndexedSymbol name,
            @NotNull final TreePattern<MatchedT>... subpatterns) {
        super(false, name, subpatterns);
    }

    public AlternativePattern(
            @NotNull final IndexedSymbol name,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, name, subpatterns);
    }

    public AlternativePattern(
            @NotNull final IndexedSymbol name,
            @NotNull final TreePattern<MatchedT> subpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, name, subpattern, subpatterns);
    }

    // WITH NAME, WITHOUT EQTESTER

    public AlternativePattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>
                            eqTester) {
        super(false, name, eqTester);
    }

    @SafeVarargs
    public AlternativePattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT>... subpatterns) {
        super(false, name, eqTester, subpatterns);
    }

    public AlternativePattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, name, eqTester, subpatterns);
    }

    public AlternativePattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT> subpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(false, name, eqTester, subpattern, subpatterns);
    }

    @Override
    public PatternType getType() {
        return TreePatternType.AlternativePattern;
    }

    @Override
    public MatchIterator<MatchedT> matchIterator(
            @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
        return new AlternativePatternMatchIterator(match, forest);
    }

    @Override
    public List<MatchedT> createForest(@NotNull final Match<MatchedT> match)
            throws CreateException {
        return createForestByMatchAndName(match);
    }

    @Override
    public AlternativePattern<MatchedT> clone() {
        return new AlternativePattern<>(name, eqTester, getClonedChildren());
    }

    @Override
    protected AlternativePattern<MatchedT> cloneWithIteratedName(int iteration) {
        return new AlternativePattern<>(
                iterateName(name, iteration),
                eqTester,
                getClonedChildrenWithIteratedNames(iteration));
    }
}
