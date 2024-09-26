package de.tudortmund.cs.iltis.utils.collections;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import javax.annotation.Nonnull;

/**
 * Specialized {@link Set} realization that keeps track of the number of times an element is added
 * to it.
 */
public class Multiset<T extends Serializable> implements Serializable, Set<T> {
    private Map<T, Integer> map;

    public Multiset() {
        this(new ArrayList<>());
    }

    public Multiset(Iterable<T> elements) {
        this.map = new HashMap<>();
        for (T t : elements) {
            add(t);
        }
    }

    public Multiset(Multiset<T> ms) {
        this.map = new HashMap<>();
        for (T t : ms.elementSet()) {
            add(t, ms.count(t));
        }
    }

    /**
     * @param o an Object
     * @return true if {@code o} is of type {@code T} and contained in this Multiset
     */
    @Override
    public boolean contains(Object o) {
        try {
            return map.containsKey(o);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Adds the specified element with a cardinality of 1 to this Multiset.
     *
     * @param t element which is to be added
     * @return always true
     */
    @Override
    public boolean add(T t) {
        return add(t, 1);
    }

    /**
     * Adds the specified element with a cardinality of {@code count} to this Multiset.
     *
     * @param t element which is to be added
     * @param count added cardinality of the specified element
     * @return whether {@code count} is positive
     */
    public boolean add(T t, int count) {
        if (count > 0) {
            if (contains(t)) {
                int freq = map.get(t);
                map.replace(t, freq + count);
            } else {
                map.put(t, count);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes {@code o} completely from this Multiset.
     *
     * @param o object which is to be removed
     * @return true if {@code o} was contained in this Multiset
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        if (contains(o)) {
            T t = (T) o;
            map.remove(t);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Reduces cardinality of {@code t} by {@code count} or to 0 if {@code t} occurs less than
     * {@code count} times in this Multiset.
     *
     * @param t element whose cardinality is to be reduced
     * @param count reduction number
     * @return the number of removed occurrences
     */
    public int reduce(T t, int count) {
        if (contains(t) && count > 0) {
            int freq = map.get(t);

            if (freq <= count) {
                remove(t);
                return freq;
            } else {
                map.replace(t, freq - count);
                return count;
            }
        } else {
            return 0;
        }
    }

    /**
     * @return the number of the elements contained in this Multiset
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * @return true if this Multiset contains no elements
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * @param t element which is to be counted
     * @return cardinality of {@code t} in this Multiset
     */
    public int count(T t) {
        if (contains(t)) {
            return map.get(t);
        } else {
            return 0;
        }
    }

    /**
     * @return the sum of the cardinalities of all elements in this Multiset
     */
    public int totalCount() {
        int count = 0;
        for (T t : elementSet()) {
            count += count(t);
        }
        return count;
    }

    /**
     * @param c a collection
     * @return whether this Multiset contains all of the elements of the specified collection
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds all elements of a specified collection with a cardinality of 1 to this Multiset.
     *
     * @param c the collection
     * @return true if this Multiset has been modified
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T t : c) {
            add(t);
        }
        return !c.isEmpty();
    }

    /**
     * Retains all elements of this Multiset which are specified in a collection and removes all
     * other elements.
     *
     * @param c the collection
     * @return true if this Multiset has been modified
     */
    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        boolean modified = false;

        Iterator<T> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }

        return modified;
    }

    /**
     * Removes all elements of this Multiset which are specified in a collection.
     *
     * @param c the collection
     * @return true if this Multiset has been modified
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;

        for (Object o : c) {
            modified |= remove(o);
        }

        return modified;
    }

    /** Removes all elements from this Multiset. */
    @Override
    public void clear() {
        map.clear();
    }

    /**
     * @return a set of the elements contained in this Multiset
     */
    public Set<T> elementSet() {
        return map.keySet();
    }

    /**
     * Adds each element as often to a list as is it contained in this Multiset. Equal elements are
     * grouped.
     *
     * @return a full list of the elements
     */
    public List<T> fullList() {
        List<T> list = new ArrayList<>();
        for (T t : elementSet()) {
            for (int i = 0; i < count(t); i++) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * @return a set of the mappings contained in this Multiset
     */
    public Set<Entry<T, Integer>> entrySet() {
        return map.entrySet();
    }

    /**
     * @return an iterator over the set of elements in this Multiset
     */
    @Override
    public Iterator<T> iterator() {
        return elementSet().iterator();
    }

    /**
     * @return an iterator over the full list of elements in this Multiset
     */
    public Iterator<T> fullIterator() {
        return new Iterator<T>() {
            private int currentIndex = 0;
            private boolean canRemove = false;

            public boolean hasNext() {
                return currentIndex < totalCount();
            }

            public T next() {
                if (hasNext()) {
                    canRemove = true;
                    return fullList().get(currentIndex++);
                } else {
                    throw new NoSuchElementException();
                }
            }

            public void remove() {
                if (canRemove) {
                    reduce(fullList().get(--currentIndex), 1);
                    canRemove = false;
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        };
    }

    @Override
    public Object[] toArray() {
        return fullList().toArray(new Object[0]);
    }

    @Override
    public <E> E[] toArray(@Nonnull E[] a) {
        return fullList().toArray(a);
    }

    public Multiset<T> clone() {
        return new Multiset<>(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Multiset)) {
            return false;
        }
        Multiset<?> ms = (Multiset<?>) obj;
        return Objects.equals(this.map, ms.map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
