package de.tudortmund.cs.iltis.utils.collections.relations;

import de.tudortmund.cs.iltis.utils.collections.ListSet;
import de.tudortmund.cs.iltis.utils.collections.Tuple;
import java.util.Iterator;

public class FiniteBinaryRelation<T> implements Relation<T>, Iterable<Tuple<T>> {

    private ListSet<Tuple<T>> elements;

    public FiniteBinaryRelation() {
        this.elements = new ListSet<>();
    }

    /**
     * @throws IllegalArgumentException if elements does not contain only 2-tuple
     */
    public FiniteBinaryRelation(Tuple<T>... elements) {
        this.elements = new ListSet<>();
        for (Tuple<T> element : elements) this.add(element);
    }

    public ListSet<Tuple<T>> getElements() {
        return this.elements;
    }

    /**
     * @throws IllegalArgumentException if element is not 2-tuple
     */
    public void add(Tuple<T> element) {
        if (element.getSize() != 2) {
            throw new IllegalArgumentException("Element " + element + " is not a pair!");
        }
        this.elements.add(element);
    }

    public void add(T first, T second) {
        this.add(new Tuple<T>(first, second));
    }

    public void remove(Tuple<T> element) {
        this.elements.remove(element);
    }

    public boolean contains(Tuple<T> element) {
        return this.elements.contains(element);
    }

    public boolean contains(T first, T second) {
        return this.contains(new Tuple<T>(first, second));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof FiniteBinaryRelation)) return false;
        if (this == o) return true;

        FiniteBinaryRelation<T> relation = (FiniteBinaryRelation<T>) o;
        return this.getElements().equals(relation.getElements());
    }

    public int arity() {
        return 2;
    }

    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    public int size() {
        return this.elements.size();
    }

    public Iterator<Tuple<T>> iterator() {
        return this.elements.iterator();
    }

    public String toString() {
        return this.elements.toString();
    }

    public FiniteBinaryRelation<T> clone() {
        FiniteBinaryRelation<T> relation = new FiniteBinaryRelation<>();
        for (Tuple<T> tuple : this.getElements()) {
            relation.add(tuple);
        }
        return relation;
    }
}
