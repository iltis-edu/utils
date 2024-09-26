package de.tudortmund.cs.iltis.utils.collections;

import java.util.Iterator;

/**
 * A simple interface that extends an {@link Iterator} by a reset() method.
 *
 * @param <E> the type to iterate over
 */
public interface ResettableIterator<E> extends Iterator<E> {

    void reset();
}
