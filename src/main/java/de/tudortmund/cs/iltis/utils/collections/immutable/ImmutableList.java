package de.tudortmund.cs.iltis.utils.collections.immutable;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The fundamental purpose of this class is to provide an immutable list implementation which can be
 * used to store lists of elements inside immutable objects or return long-living results from
 * algorithms. It aims at providing strong compile-time correctness, avoiding surprising
 * runtime-exceptions and being efficient.
 *
 * <p>This class <b>intentionally</b> does <b>not</b> implement {@code Collection} or {@code List},
 * because these interfaces impose methods like {@code add} or {@code clear}. To interact with
 * Java-Collection API one should use {@link ImmutableList#toUnmodifiableList()}. Note that this
 * method returns an <b>unmodifiable view</b>, i.e. all methods that would structurally change the
 * collection will result in {@code UnsupportedOperationException}. Also, since views are returned,
 * these methods are constant time and <b>not</b> linear in the size of this list.
 *
 * <p>If you need a mutable list object, you should create one explicitly. If you need an immutable
 * copy, you don't need a copy. You can simply pass this object directly, that's the point after
 * all. To further enforce this point, there is no constructor that allows you to create an {@code
 * ImmutableList} from another {@code ImmutableList} directly.
 *
 * <p>Since this class is immutable it is not suited to be used as local variables which are
 * updated. Instead, use a default Java Collection which is mutable and meets your performance
 * requirements. The final result of the method can then be put into an {@code ImmutableList} to
 * safely store it without worrying that it may be altered. To illustrate this fact {@code
 * ImmutableList} does <b>not</b> have immutable versions of methods like {@code add} which return a
 * copy. This would simply lead to a terrible runtime performance.
 *
 * <p>If you want to subclass this class, please note that all subclasses must be immutable as well.
 * You must <b>never</b> modify {@code elements} yourself, since it may be shared between instances
 * for performance optimization. Use {@code super(...)} instead to pass data during the
 * construction. Also, please note the utility method {@link ImmutableList#shareConstructInto} to
 * covariantly override some methods without creating unnecessary copies.
 *
 * @param <T> the type of elements contained in this set
 */
public class ImmutableList<T extends Serializable> implements Serializable, Iterable<T> {

    /* Should be final, but must not be marked `final`, otherwise GWT serialization will break :( */
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<T> elements = new ArrayList<>();

    /* For serialization */
    @SuppressWarnings("unused")
    protected ImmutableList() {}

    /** Create a new ImmutableList containing the given elements */
    @SafeVarargs
    public ImmutableList(T... elements) {
        this(Arrays.asList(elements));
    }

    /** Creates a new ImmutableList from the elements of the given collection */
    public ImmutableList(Collection<? extends T> collection) {
        elements.addAll(collection);
    }

    /**
     * Utility method to construct a new instance of this class (or a subclass) which shares the
     * internal data with an existing one.
     *
     * <p><b>Only for internal use! Only use if you know what you are doing!</b> The idea of this
     * method is to allow subclasses to efficiently and covariantly override methods that return
     * instance of {@code ImmutableList}. For example, {@link ImmutableList#map} returns a new
     * {@code ImmutableList}. However, a subclass may want to return instances of that subclass
     * instead. To do this, one could<br>
     * 1) reimplement the entire method (horrible software design)<br>
     * 2) call super and construct a new instance of the subclass using the result from {@code
     * super} and {@code toUnmodifiableList} to obtain a Java collection which can be passed to the
     * constructor again (N useless copies where N is the number of (recursive) calls to {@code
     * super}).<br>
     * Both solutions are bad for various reasons. Casting is also not an option, because it will
     * always fail.<br>
     * <br>
     * This method offers a nice solution: it takes a lambda to construct an new instance of the
     * subclass and the result from super and reuses its internal data. This way we can completely
     * eliminate the copy while being perfectly typesafe. Also, the {@code source} remains valid,
     * because we can guarantee that the internal data of {@code source} and {@code target} never
     * change, so neither can be modified and "leak" modifications to the other.<br>
     * An example might be useful: assume we have a subclass {@code MyList<T> extends
     * ImmutableList<T>} and we want {@code MyList<T>::map} to return an instance of {@code MyList}
     * again. Then we can overwrite {@code map} like this:
     *
     * <pre>
     * public MyList&lt;T> map(...) {
     *     return shareConstructInto(MyList::new, super.map(f));
     * }
     * </pre>
     *
     * Obviously, this only works if {@code MyList} offers a constructor with 0 arguments, which is
     * a reasonable assumption for a list type. <br>
     * <br>
     * This method is safe for any {@code source} (temporary or not), but returning a non-temporary
     * instance from {@code targetSupplier} is not, so please follow the example above!
     *
     * @param targetSupplier a function to create a new instance of the return type
     * @param source the temporary object from which we "adopt" the internal data
     * @return a new instance of type Coll with the data from source
     * @param <T> the type of elements contained in the subclass
     * @param <Coll> the concrete subtype of ImmutableList of which the result shall be an instance
     */
    protected static <T extends Serializable, Coll extends ImmutableList<T>>
            Coll shareConstructInto(Supplier<Coll> targetSupplier, ImmutableList<T> source) {
        Coll target = targetSupplier.get();
        ((ImmutableList<T>) target).elements = source.elements;
        return target;
    }

    /**
     * Returns the number of elements in this list
     *
     * @return the number of elements in this list
     */
    public int size() {
        return elements.size();
    }

    /**
     * Tests if this list is empty
     *
     * @return {@code true}, iff there are no elements in this list
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Tests whether the given object is contained in this list
     *
     * @param t the object to test for in this list
     * @return {@code true}, iff {@code t} is an element of this list
     */
    public boolean contains(T t) {
        return elements.contains(t);
    }

    /**
     * Tests whether all elements {@code collection} are contained in this list
     *
     * @param collection the collection to be checked for containment in this list
     * @return {@code true}, iff <b>all</b> elements of {@code collection} are elements of this list
     *     as well
     */
    public boolean containsAll(Collection<? extends T> collection) {
        return elements.containsAll(collection);
    }

    /**
     * Gets the elements at position {@code idx}
     *
     * @param idx the position from where the element should be retrieved
     * @throws IndexOutOfBoundsException if, {@code idx} is not in [0, this.size())
     * @return the element at position {@code idx}
     */
    public T get(int idx) {
        return elements.get(idx);
    }

    /**
     * Builds a stream over all elements in this list
     *
     * @return a stream over all elements in this list
     */
    public Stream<T> stream() {
        return elements.stream();
    }

    /**
     * Returns an iterator over all elements in this list
     *
     * <p><b>Note:</b> the iterator does not allow mutation of the collection, i.e.{@code
     * iterator.remove()} will throw an exception.
     *
     * @return an iterator over the elements of this list
     */
    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(elements).iterator();
    }

    /**
     * Creates an "unmodifiable (list) view" of this list
     *
     * <p>This method should be used when interacting with the native Java Collections API, since
     * this class does not implement {@code List} or {@code Collection} (intentionally).
     *
     * @return an unmodifiable view of this list
     */
    public List<T> toUnmodifiableList() {
        return Collections.unmodifiableList(elements);
    }

    /**
     * Maps all elements of this list using the given function {@code f}
     *
     * @param f the function to apply to each element of this list
     * @return a new {@code Immutable} with the mapped elements
     * @param <S> the type of elements in the resulting list
     */
    public <S extends Serializable> ImmutableList<S> map(Function<? super T, S> f) {
        return new ImmutableList<>(elements.stream().map(f).collect(Collectors.toList()));
    }

    /**
     * Folds this list using the given function {@code f} to update the {@code accumulator} once
     * with each element <br>
     * The order in which elements are processed is guaranteed to be the same as the order in which
     * elements are yielded from {@code stream()} or {@code iterator()}.
     *
     * @param accumulator the initial result value
     * @param f the binary function to update the accumulator once with every element of this list
     * @return the accumulator after updating it once per element in this collection
     * @param <S> the type of the accumulator and result
     */
    public <S extends Serializable> S foldl(S accumulator, BiFunction<? super S, ? super T, S> f) {
        S result = accumulator;
        for (T e : elements) result = f.apply(result, e);
        return result;
    }

    /**
     * Filters out all elements of this list that do not satisfy the given predicate <br>
     * The order of elements in the resulting list is the same as the order in which those elements
     * occur in this list.
     *
     * @param p the predicate used to decide which values to keep
     * @return a new {@code ImmutableList} with all elements from {@code this} that satisfy {@code
     *     p}
     */
    public ImmutableList<T> filter(Predicate<? super T> p) {
        return new ImmutableList<>(elements.stream().filter(p).collect(Collectors.toList()));
    }

    /**
     * Tests if this list is the beginning (a prefix) of {@code other} <br>
     * If {@code this.equals(other)}, {@code this} is still considered a prefix.
     *
     * @param other the other list of which to test the beginning
     * @return {@code true}, iff {@code this} is a prefix of {@code other}
     */
    public boolean isPrefixOf(ImmutableList<? extends T> other) {
        if (this == other) return true;
        if (this.size() > other.size()) return false;
        Iterator<T> here = this.elements.iterator();
        Iterator<T> there = (Iterator<T>) other.elements.iterator();
        while (here.hasNext()) {
            T symbolHere = here.next();
            T symbolThere = there.next();
            if (!symbolHere.equals(symbolThere)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests if this list is the end (a suffix) of {@code other} <br>
     * If {@code this.equals(other)}, {@code this} is still considered a suffix.
     *
     * @param other the other list of which to test the end
     * @return {@code true}, iff {@code this} is a suffix of {@code other}
     */
    public boolean isSuffixOf(ImmutableList<? extends T> other) {
        if (this == other) return true;
        return isPrefixOf(other.drop(other.size() - size()));
    }

    /**
     * Appends the elements of {@code other} to the elements from this list
     *
     * @param other a list of elements to append
     * @return a new {@code ImmutableList} with elements from {@code this} and {@code other} (in
     *     this order).
     */
    public ImmutableList<T> append(ImmutableList<? extends T> other) {
        if (other.isEmpty()) return this;
        ImmutableList<T> result = new ImmutableList<>(elements);
        result.elements.addAll(other.elements);
        return result;
    }

    /**
     * Prepends the elements of {@code other} to the elements from this list
     *
     * @param other a list of elements to prepend
     * @return a new {@code ImmutableList} with elements from {@code other} and {@code this} (in
     *     this order).
     */
    public ImmutableList<T> prepend(ImmutableList<? extends T> other) {
        if (other.isEmpty()) return this;
        ImmutableList<T> result = new ImmutableList<>(other.elements);
        result.elements.addAll(elements);
        return result;
    }

    /**
     * Drops the first {@code prefixSize} elements from this list <br>
     * This function is safe to use with arbitrary sizes and an empty or complete list is returned
     * in these cases.
     *
     * @param prefixSize the number of elements to drop
     * @return a new {@code ImmutableList} with the same elements as this one but the first {@code
     *     prefixSize} elements are removed
     */
    public ImmutableList<T> drop(int prefixSize) {
        if (prefixSize <= 0) return this;
        if (prefixSize > size()) return new ImmutableList<>();
        return new ImmutableList<>(elements.subList(prefixSize, size()));
    }

    /**
     * Takes only the first {@code prefixSize} elements from this list and discards all remaining
     * ones <br>
     * This function is safe to use with arbitrary sizes and an empty or complete list is returned
     * in these cases.
     *
     * @param prefixSize the number of elements to keep
     * @return a new {@code ImmutableList} with the first {@code prefixSize} elements of this list
     */
    public ImmutableList<T> take(int prefixSize) {
        if (prefixSize <= 0) return new ImmutableList<>();
        if (prefixSize > size()) return this;
        return new ImmutableList<>(elements.subList(0, prefixSize));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ImmutableList)) return false;
        return Objects.equals(elements, ((ImmutableList<?>) obj).elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    @Override
    public String toString() {
        return "[" + stream().map(Object::toString).collect(Collectors.joining(",")) + "]";
    }
}
