package de.tudortmund.cs.iltis.utils.collections;

import java.util.Iterator;

/**
 * An iterator that calculates its next element when needed for a call of {@link #next()} or {@link
 * #hasNext()}. The most recently calculated element is cashed until it is returned by {@link
 * #next()}.
 *
 * @param <T> the type to iterate over
 */
public abstract class PreCalcIterator<T> implements Iterator<T> {

    protected T nextElement;
    protected boolean nextIsCalculated;
    protected boolean isInited;

    public PreCalcIterator() {}

    /**
     * Can be overwritten in subclasses to initialize this iterator.
     *
     * <p>When false is returned this iterator iterates over no elements.
     *
     * @return true iff initialization succeeded
     */
    protected boolean init() {
        return true;
    }

    @Override
    public boolean hasNext() {
        initIfNotInited();
        setNextIfNotSet();
        return nextElement != null;
    }

    @Override
    public T next() {
        initIfNotInited();
        setNextIfNotSet();
        T elementToReturn = nextElement;
        if (nextElement != null) nextIsCalculated = false;
        return elementToReturn;
    }

    /**
     * Should be overwritten in subclasses to calculate the next element of this iterator.
     *
     * @return the next element or null if there is no further element
     */
    protected abstract T calculateNext();

    /**
     * Can be overwritten in subclasses to calculate the first element of this iterator. Defaults to
     * {@link #calculateNext()} if not overwritten.
     *
     * @return the first element or null if there is no element
     */
    protected T calculateFirst() {
        return calculateNext();
    }

    protected void reset() {
        isInited = false;
    }

    protected void setNextIfNotSet() {
        if (!nextIsCalculated) {
            if (nextElement == null) // i.e. there is no previous next element
            nextElement = calculateFirst();
            else nextElement = calculateNext();
            nextIsCalculated = true;
        }
    }

    private void initIfNotInited() {
        if (isInited) return;

        nextElement = null;
        boolean hasElement = init();
        nextIsCalculated = !hasElement;
        isInited = true;
    }
}
