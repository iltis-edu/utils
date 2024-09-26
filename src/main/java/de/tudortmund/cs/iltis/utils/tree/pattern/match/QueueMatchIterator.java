package de.tudortmund.cs.iltis.utils.tree.pattern.match;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.validation.constraints.NotNull;

/**
 * A match iterator that iterates over arbitrary many matches. A new match is calculated in the
 * method {@link #calculateNextMatch()} which is to be overridden by subclasses.
 *
 * <p>These iterators save the previously returned matches and do not return a match object twice by
 * {@link #next()}, even if {@link #calculateNextMatch()} returns the same match twice.
 *
 * @param <MatchedT> the type of tree to be matched
 */
public abstract class QueueMatchIterator<MatchedT extends Tree<MatchedT>>
        extends MatchIterator<MatchedT> {

    protected Deque<Match<MatchedT>> previousElements;
    protected Queue<Match<MatchedT>> nextElements;
    protected boolean nextIsCalculated;
    protected boolean isInited;

    /**
     * Creates a new queue match iterator.
     *
     * @param match the given match object as basis of the match objects to iterate over
     * @param forest the forest to match
     */
    public QueueMatchIterator(@NotNull Match<MatchedT> match, @NotNull List<MatchedT> forest) {
        super(match, forest);
    }

    /**
     * Creates a new queue match iterator.
     *
     * @param match the given match object as basis of the match objects to iterate over
     * @param tree the tree to match
     */
    public QueueMatchIterator(@NotNull Match<MatchedT> match, @NotNull MatchedT tree) {
        super(match, tree);
    }

    private void initIfNotInited() {
        if (isInited) return;

        nextElements = new LinkedList<>();
        previousElements = new LinkedList<>();
        boolean inited = initMatching();
        if (inited) nextIsCalculated = false;
        else nextIsCalculated = true;
        isInited = true;
    }

    /**
     * Initializes this iterator.
     *
     * @return true iff initialization succeeded and {@link #calculateNextMatch()} shall be called
     *     to calculate the first match
     */
    protected boolean initMatching() {
        return true;
    }

    /**
     * If any further element exists and {@link #nextElements} is not empty, adds at least one
     * (previously not added) element per call to {@link #nextElements}. Therefore {@link
     * #calculateNextMatch()} can be called multiple times.
     */
    protected void addNextMatchesIfNecessary() {
        if (nextIsCalculated || !nextElements.isEmpty()) return;

        Match<MatchedT> newMatch;
        do {
            newMatch = calculateNextMatch();
        } while (newMatch != null
                && (previousElements.contains(newMatch) || nextElements.contains(newMatch)));
        if (newMatch != null) nextElements.add(newMatch);
        nextIsCalculated = true;
    }

    /**
     * Calculates one element unequal to null per call if any further element exists. Is to be
     * overridden in subclasses.
     *
     * @return the match object or null if no further match object exsists
     */
    protected abstract Match<MatchedT> calculateNextMatch();

    @Override
    public boolean hasNext() {
        initIfNotInited();
        addNextMatchesIfNecessary();
        return !nextElements.isEmpty();
    }

    @Override
    public Match<MatchedT> next() {
        initIfNotInited();
        addNextMatchesIfNecessary();
        Match<MatchedT> result = nextElements.poll();
        if (result != null) {
            previousElements.add(result);
            nextIsCalculated = false;
        }
        return result;
    }

    /** for serialization */
    protected QueueMatchIterator() {}
}
