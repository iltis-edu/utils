package de.tudortmund.cs.iltis.utils.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that iterates over no elements.
 *
 * <p>Can be used as a shortcut for creating an iterator of an empty collection.
 *
 * @param <E> the type to iterate over
 */
public class EmptyIterator<E> implements Iterator<E> {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public E next() {
        throw new NoSuchElementException();
    }
}
