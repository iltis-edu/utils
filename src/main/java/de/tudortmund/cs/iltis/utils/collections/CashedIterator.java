package de.tudortmund.cs.iltis.utils.collections;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A resettable iterator that caches all its elements to reuse after a reset.
 *
 * @param <T> the type to iterate over
 */
public class CashedIterator<T> implements ResettableIterator<T> {

    private List<T> cache;
    private Iterator<T> base;
    boolean isResetted;

    public CashedIterator(Iterator<T> base) {
        Objects.requireNonNull(base);
        cache = new LinkedList<>();
        this.base = base;
        isResetted = false;
    }

    @Override
    public T next() {
        T next = base.next();
        if (!isResetted) cache.add(next);
        return next;
    }

    @Override
    public boolean hasNext() {
        return base.hasNext();
    }

    /** <b>Implementation Notice:</b> iterates over all remaining elements to fill the cache. */
    @Override
    public void reset() {
        while (!isResetted && hasNext()) {
            next();
        }
        isResetted = true;
        base = cache.iterator();
    }
}
