package de.tudortmund.cs.iltis.utils.tree.pattern.match;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * A match iterator that only iterates over a single match. This match is calculated in the method
 * {@link #calculateMatch()} which is to be overridden by subclasses.
 *
 * @param <MatchedT> the type of tree to be matched
 */
public abstract class SingleMatchIterator<MatchedT extends Tree<MatchedT>>
        extends MatchIterator<MatchedT> {

    protected Match<MatchedT> singleMatch;
    protected boolean matchIsCalculated;

    /**
     * Creates a new single match iterator.
     *
     * @param match the given match object as basis of the match object to create
     * @param forest the forest to match
     */
    public SingleMatchIterator(@NotNull Match<MatchedT> match, @NotNull List<MatchedT> forest) {
        super(match, forest);
    }

    /**
     * Creates a new single match iterator.
     *
     * @param match the given match object as basis of the match object to create
     * @param tree the tree to match
     */
    public SingleMatchIterator(@NotNull Match<MatchedT> match, @NotNull MatchedT tree) {
        super(match, tree);
    }

    /**
     * Calculates the only match. Is to be overridden in subclasses.
     *
     * @return the match object
     */
    protected abstract Match<MatchedT> calculateMatch();

    @Override
    public boolean hasNext() {
        calculateMatchIfNotCalculated();
        return singleMatch != null;
    }

    @Override
    public Match<MatchedT> next() {
        calculateMatchIfNotCalculated();
        Match<MatchedT> result = singleMatch;
        singleMatch = null;
        return result;
    }

    private void calculateMatchIfNotCalculated() {
        if (matchIsCalculated) return;
        singleMatch = calculateMatch();
        matchIsCalculated = true;
    }

    /** for serialization */
    protected SingleMatchIterator() {
        super();
    }
}
