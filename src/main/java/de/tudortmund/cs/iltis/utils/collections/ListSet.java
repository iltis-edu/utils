package de.tudortmund.cs.iltis.utils.collections;

import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.general.Data;
import de.tudortmund.cs.iltis.utils.io.writer.collections.SetWriter;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

/**
 * Specialized {@link Set} realization that keeps track of the order in which elements are added to
 * it.
 */
public class ListSet<T> implements Set<T>, Serializable {
    private SerializableBiFunction<T, T, Boolean> equalityTester;
    private ArrayList<T> elements;
    private final transient SetWriter<T> writer = new SetWriter<>();

    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////
    public ListSet(Iterable<T> elements, SerializableBiFunction<T, T, Boolean> equalityTester) {
        this.equalityTester = equalityTester;
        this.elements = new ArrayList<>();
        for (T element : elements) this.add(element);
    }

    public ListSet(Iterable<T> elements) {
        this(elements, new EqualsFunction<>());
    }

    public ListSet(Stream<T> elements) {
        this.equalityTester = new EqualsFunction<>();
        this.elements = new ArrayList<>();
        Iterator<T> it = elements.iterator();
        while (it.hasNext()) this.add(it.next());
    }

    @SafeVarargs
    public ListSet(T... elements) {
        this.equalityTester = new EqualsFunction<>();
        this.elements = new ArrayList<>();
        this.addAll(Arrays.asList(elements));
    }

    public ListSet(SerializableBiFunction<T, T, Boolean> equalityTester) {
        this(new ArrayList<>(), equalityTester);
    }

    public ListSet() {
        this(new ArrayList<>());
    }

    ///////////////////////////////////////////////////////////////////////////
    // METHODS
    ///////////////////////////////////////////////////////////////////////////

    public <S extends Serializable> ListSet<S> castTo() {
        ListSet<S> casted = new ListSet<>();
        for (T item : this) casted.add((S) item);
        return casted;
    }

    public Set<T> toSet() {
        return new HashSet<>(elements);
    }

    public T one() {
        assert (this.size() == 1);
        return this.elements.get(0);
    }

    public Optional<T> one_or_none() {
        if (this.isEmpty()) return Optional.empty();
        return Optional.of(this.one());
    }

    @Override
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public boolean contains(Object object) {
        try {
            T element = (T) object;
            for (T elementInList : this.elements)
                if (this.equalityTester.apply(elementInList, element)) return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public <T> T[] toArray(T[] type) {
        return this.elements.toArray(type);
    }

    public List<T> toList() {
        return this.elements;
    }

    @Override
    public Object[] toArray() {
        return this.elements.toArray();
    }

    public boolean contains(ListSet<T> set) {
        for (T elementInOtherList : set.elements)
            if (!this.contains(elementInOtherList)) return false;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> elements) {
        for (Object elementInOtherList : elements)
            if (!this.contains(elementInOtherList)) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ListSet)) return false;
        if (this == o) return true;

        ListSet<T> set = (ListSet<T>) o;
        return this.size() == set.size() && this.contains(set);
    }

    @Override
    public void clear() {
        this.elements.clear();
    }

    @Override
    public boolean add(T newElement) {
        return this.addSingleElement(newElement);
    }

    @SuppressWarnings("unchecked")
    public void add(T... newElements) {
        for (T newElement : newElements) addSingleElement(newElement);
    }

    @Override
    public boolean addAll(Collection<? extends T> newElements) {
        boolean someNew = false;
        for (T newElement : newElements) someNew |= addSingleElement(newElement);
        return someNew;
    }

    @Override
    public boolean remove(Object oldElement) {
        return this.elements.remove(oldElement);
    }

    @SuppressWarnings("unchecked")
    public void remove(T... oldElements) {
        for (T oldElement : oldElements) this.elements.remove(oldElement);
    }

    @Override
    public boolean removeAll(Collection<?> oldElements) {
        return this.elements.removeAll(oldElements);
    }

    @Override
    public boolean retainAll(Collection<?> oldElements) {
        return this.elements.retainAll(oldElements);
    }

    public ListSet<T> intersect(ListSet<T> other) {
        return Data.filter(this, other::contains);
    }

    @Override
    public int hashCode() {
        int hashCode = 7145;
        for (T element : this.elements) hashCode += 133 * element.hashCode();
        return 23 * hashCode;
    }

    @Override
    public Iterator<T> iterator() {
        return this.elements.iterator();
    }

    @Override
    public String toString() {
        return this.writer.write(this);
    }

    public ListSet<T> clone() {
        return new ListSet<>(this.elements, equalityTester);
    }

    // PRIVATE ================================================================
    protected boolean addSingleElement(T newElement) {
        if (!this.contains(newElement)) {
            this.elements.add(newElement);
            return true;
        }
        return false;
    }
}
