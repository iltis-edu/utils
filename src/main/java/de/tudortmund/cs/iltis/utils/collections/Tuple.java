package de.tudortmund.cs.iltis.utils.collections;

import de.tudortmund.cs.iltis.utils.StringUtils;
import de.tudortmund.cs.iltis.utils.general.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A shallow interface above an ArrayList. Tuples are immutable.
 *
 * @param <T> the type of the tuple elements
 */
public class Tuple<T> implements Serializable, Cloneable, Iterable<T> {

    /** for serialization */
    private static final long serialVersionUID = 1L;

    private List<T> elements;

    @SafeVarargs
    public Tuple(T... elements) {
        this.elements = Data.newArrayList(elements);
    }

    public Tuple(Iterable<T> elements) {
        this.elements = Data.newArrayList(elements);
    }

    public int getSize() {
        return elements.size();
    }

    /**
     * Gets the element at the specified position.
     *
     * @throws IndexOutOfBoundsException if position is lower than zero or larger or equal to the
     *     tuple size as determined by {@link #getSize()}.
     */
    public T getElementAtPosition(int position) {
        return elements.get(position);
    }

    public T first() {
        return elements.get(0);
    }

    public T second() {
        return elements.get(1);
    }

    public T third() {
        return elements.get(2);
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    public Tuple<T> getReverse() {
        Tuple<T> reverseTuple = clone();
        Collections.reverse(reverseTuple.elements);
        return reverseTuple;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((elements == null) ? 0 : elements.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Tuple<?> other = (Tuple<?>) obj;
        if (elements == null) {
            if (other.elements != null) return false;
        } else if (!elements.equals(other.elements)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "(" + StringUtils.concatenate(elements) + ")";
    }

    public Tuple<T> clone() {
        List<T> elementsClone = new ArrayList<>(elements.size());
        elementsClone.addAll(elements);
        return new Tuple<>(elementsClone);
    }

    private Tuple() {}
}
