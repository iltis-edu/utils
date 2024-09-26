package de.tudortmund.cs.iltis.utils.tree.pattern.match;

import de.tudortmund.cs.iltis.utils.collections.SetCombinationsIterator;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * An iterator that extends {@link AbstractConjunctionMatchIterator} to implement the conjunction of
 * a set of matches. It creates new {@link TreeMatch}es and iterates over them.
 *
 * @param <MatchedT> the type of tree to be matched
 */
public class ConjunctionTreeMatchIterator<MatchedT extends Tree<MatchedT>>
        extends AbstractConjunctionMatchIterator<MatchedT> {

    /**
     * Creates a new conjunction tree match iterator which iterates over the matches that arise of
     * forming the conjunction of the given set of matches.
     *
     * @param matches the set of matches to form the conjunction with
     */
    public ConjunctionTreeMatchIterator(@NotNull Set<Set<Match<MatchedT>>> matches) {
        super(matches);
    }

    public static <MatchedT extends Tree<MatchedT>>
            ConjunctionTreeMatchIterator<MatchedT> fromIterators(
                    @NotNull Collection<Iterator<Match<MatchedT>>> iterators) {
        ConjunctionTreeMatchIterator<MatchedT> conjIt = new ConjunctionTreeMatchIterator<>();
        conjIt.it = SetCombinationsIterator.fromIterators(iterators);
        return conjIt;
    }

    @Override
    protected Match<MatchedT> createEmptyMatch() {
        return new TreeMatch<>();
    }

    /** for serialization */
    private ConjunctionTreeMatchIterator() {}
}
