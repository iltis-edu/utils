package de.tudortmund.cs.iltis.utils.collections.immutable;

import de.tudortmund.cs.iltis.utils.io.writer.collections.SetWriter;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The fundamental purpose of this class is to provide an immutable set implementation which can be
 * used to store sets of elements inside immutable objects or return long-living results from
 * algorithms. It aims at providing strong compile-time correctness, avoiding surprising
 * runtime-exceptions and being efficient.
 *
 * <p>This class <b>intentionally</b> does <b>not</b> implement {@code Collection} or {@code Set},
 * because these interfaces impose methods like {@code add} or {@code clear}. To interact with
 * Java-Collection API one should use {@link ImmutableSet#toUnmodifiableSet()} and {@link
 * ImmutableSet#toUnmodifiableList()}. Note that these methods return <b>unmodifiable views</b>,
 * i.e. all methods that would structurally change the collection will result in {@code
 * UnsupportedOperationException}. Also, since views are returned, these methods are constant time
 * and <b>not</b> linear in the size of this set.
 *
 * <p>If you need a mutable set object, you should create one explicitly. If you need an immutable
 * copy, you don't need a copy. You can simply pass this object directly, that's the point after
 * all. To further enforce this point, there is no constructor that allows you to create an {@code
 * ImmutableSet} from another {@code ImmutableSet} directly.
 *
 * <p>Since this class is immutable it is not suited to be used as local variables which are
 * updated. Instead, use a default Java Collection which is mutable and meets your performance
 * requirements. The final result of the method can then be put into an {@code ImmutableSet} to
 * safely store it without worrying that it may be altered. To illustrate this fact {@code
 * ImmutableSet} does <b>not</b> have immutable versions of methods like {@code add} which return a
 * copy. This would simply lead to a terrible runtime performance.
 *
 * <p>The implementation of this set guarantees a consistent order of its elements. In particular,
 * the order in which the elements are given to the constructor is maintained throughout and passed
 * on to all results from methods like {@code filter} or {@code map}.
 *
 * <p>If you want to subclass this class, please note that all subclasses must be immutable as well.
 * You must <b>never</b> modify {@code elements} yourself, since it may be shared between instances
 * for performance optimization. Use {@code super(...)} instead to pass data during the
 * construction. Also, please note the utility method {@link ImmutableSet#shareConstructInto} to
 * covariantly override some methods without creating unnecessary copies.
 *
 * @param <T> the type of elements contained in this set
 */
public class ImmutableSet<T extends Serializable> implements Serializable, Iterable<T> {

    /* Should be final, but must not be marked `final`, otherwise GWT serialization will break :( */
    @SuppressWarnings("FieldMayBeFinal")
    private LinkedHashSet<T> elements = new LinkedHashSet<>();

    /* For serialization */
    @SuppressWarnings("unused")
    protected ImmutableSet() {}

    /** Create a new ImmutableSet containing the given elements */
    @SafeVarargs
    public ImmutableSet(T... elements) {
        this(Arrays.asList(elements));
    }

    /** Create a new ImmutableSet from the elements of the given collection */
    public ImmutableSet(Collection<? extends T> collection) {
        elements.addAll(collection);
    }

    /**
     * Utility method to construct a new instance of this class (or a subclass) which shares the
     * internal data with an existing one.
     *
     * <p><b>Only for internal use! Only use if you know what you are doing!</b> The idea of this
     * method is to allow subclasses to efficiently and covariantly override methods that return
     * instance of {@code ImmutableSet}. For example, {@link ImmutableSet#map} returns a new {@code
     * ImmutableSet}. However, a subclass may want to return instances of that subclass instead. To
     * do this, one could<br>
     * 1) reimplement the entire method (horrible software design)<br>
     * 2) call super and construct a new instance of the subclass using the result from {@code
     * super} and {@code toUnmodifiableSet} to obtain a Java collection which can be passed to the
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
     * An example might be useful: assume we have a subclass {@code MySet<T> extends
     * ImmutableSet<T>} and we want {@code MySet<T>::map} to return an instance of {@code MySet}
     * again. Then we can overwrite {@code map} like this:
     *
     * <pre>
     * public MySet&lt;T> map(...) {
     *     return shareConstructInto(MySet::new, super.map(f));
     * }
     * </pre>
     *
     * Obviously, this only works if {@code MySet} offers a constructor with 0 arguments, which is a
     * reasonable assumption for a set type. <br>
     * <br>
     * This method is safe for any {@code source} (temporary or not), but returning a non-temporary
     * instance from {@code targetSupplier} is not, so please follow the example above!
     *
     * @param targetSupplier a function to create a new instance of the return type
     * @param source the temporary object from which we "adopt" the internal data
     * @return a new instance of type Coll with the data from source
     * @param <T> the type of elements contained in the subclass
     * @param <Coll> the concrete subtype of ImmutableSet of which the result shall be an instance
     */
    protected static <T extends Serializable, Coll extends ImmutableSet<T>> Coll shareConstructInto(
            Supplier<Coll> targetSupplier, ImmutableSet<T> source) {
        Coll target = targetSupplier.get();
        ((ImmutableSet<T>) target).elements = source.elements;
        return target;
    }

    /**
     * Returns the number of elements in this set
     *
     * @return the number of elements in this set
     */
    public int size() {
        return elements.size();
    }

    /**
     * Tests if this set is empty
     *
     * @return {@code true}, iff there are no elements in this set
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Tests whether the given object is contained in this set
     *
     * @param t the object to test for in this set
     * @return {@code true}, iff {@code t} is an element of this set
     */
    public boolean contains(T t) {
        return elements.contains(t);
    }

    /**
     * Tests whether all elements in {@code collection} are contained in this set
     *
     * @param collection the collection to be checked for containment in this set
     * @return {@code true}, iff <b>all</b> elements of {@code collection} are elements of this set
     *     as well
     */
    public boolean containsAll(Collection<? extends T> collection) {
        return elements.containsAll(collection);
    }

    /**
     * Builds a stream over all elements in this set
     *
     * <p>The order of elements is guaranteed to be the same as the order in which elements were
     * added to this set during construction.
     *
     * @return a stream over all elements in this set
     */
    public Stream<T> stream() {
        return elements.stream();
    }

    /**
     * Returns an iterator over all elements in this set
     *
     * <p>The order of elements is guaranteed to be the same as the order in which elements were
     * added to this set during construction.
     *
     * <p><b>Note:</b> the iterator does not allow mutation of the collection, i.e.{@code
     * iterator.remove()} will throw an exception.
     *
     * @return an iterator over the elements of this set
     */
    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableSet(elements).iterator();
    }

    /**
     * Creates an "unmodifiable (set) view" of this set
     *
     * <p>This method should be used when interacting with the native Java Collections API, since
     * this class does not implement {@code Set} or {@code Collection} (intentionally).
     *
     * @return an unmodifiable view of this set
     */
    public Set<T> toUnmodifiableSet() {
        return Collections.unmodifiableSet(elements);
    }

    /**
     * Creates an "unmodifiable (list) view" of this set
     *
     * <p>This method should be used when interacting with the native Java Collections API, since
     * this class does not implement {@code Set} or {@code Collection} (intentionally).
     *
     * @return an unmodifiable view of this set
     */
    public List<T> toUnmodifiableList() {
        return Collections.unmodifiableList(new ArrayList<>(elements));
    }

    /**
     * Builds a new immutable set which is the union of this and the given set
     *
     * @param other the other set included in the union
     * @return the union of {@code this} and {@code other}
     */
    public ImmutableSet<T> unionWith(ImmutableSet<? extends T> other) {
        if (this == other) return this;
        ImmutableSet<T> result = new ImmutableSet<>(elements);
        result.elements.addAll(other.elements);
        return result;
    }

    /**
     * Builds a new immutable set which is the intersection of this and the given set
     *
     * @param other the other set involved in the intersection
     * @return the intersection of {@code this} and {@code other}
     */
    public ImmutableSet<T> intersectionWith(ImmutableSet<? extends T> other) {
        if (this == other) return this;
        ImmutableSet<T> result = new ImmutableSet<>(elements);
        result.elements.retainAll(other.toUnmodifiableSet());
        return result;
    }

    /**
     * Builds a new immutable set which has no elements which are also contained in other
     *
     * @param other the set of elements to exclude from this set
     * @return the difference of {@code this} and {@code other}
     */
    public ImmutableSet<T> differenceWith(ImmutableSet<? extends T> other) {
        if (this == other) return new ImmutableSet<>();
        ImmutableSet<T> result = new ImmutableSet<>(elements);
        result.elements.removeAll(other.toUnmodifiableSet());
        return result;
    }

    /**
     * Builds a new immutable set which is the complement of this w.r.t. the given universe
     *
     * @param universe the universe from which to take the complement of this set
     * @return the complement of {@code this} w.r.t. {@code universe}
     */
    public ImmutableSet<T> complementWith(ImmutableSet<? extends T> universe) {
        if (this == universe) return new ImmutableSet<>();
        ImmutableSet<T> result = new ImmutableSet<>();
        for (T t : universe) if (!contains(t)) result.elements.add(t);
        return result;
    }

    /**
     * Tests whether {@code this} is a subset of {@code other}
     *
     * @param other the set with which the subset relation should be tested
     * @return {@code true}, iff all elements in {@code this} are also contained in {@code other}
     */
    public boolean isSubsetOf(ImmutableSet<? extends T> other) {
        if (this == other) return true;
        try {
            return other.elements.containsAll(elements);
        } catch (ClassCastException exception) {
            // if this happens, at least one of our own elements cannot be cast to whatever the
            // ?-type is
            // and can thus not be contained in other.elements
            return false;
        }
    }

    /**
     * Tests whether {@code this} is a superset of {@code other}
     *
     * @param other the set with which the superset relation should be tested
     * @return {@code true}, iff all elements in {@code other} are also contained in {@code this}
     */
    public boolean isSupersetOf(ImmutableSet<? extends T> other) {
        if (this == other) return true;
        return containsAll(other.toUnmodifiableSet());
    }

    /**
     * Tests whether {@code this} and {@code other} are disjoint, i.e. have no common elements
     *
     * @param other the set with which disjointness should be tested
     * @return {@code true}, iff there is no element that is contained in {@code this} and {@code
     *     other}
     */
    public boolean isDisjointWith(ImmutableSet<? extends T> other) {
        if (this == other) return false;
        return intersectionWith(other).isEmpty();
    }

    /**
     * Maps all elements of this set using the given function {@code f} <br>
     * The order of elements is guaranteed to be unchanged, if {@code this.stream()} yields {@code
     * x1, x2, x3} then {@code map(f).stream()} will yield {@code f(x1), f(x2), f(x3)}.
     *
     * @param f the function to apply to each element of this set
     * @return a new {@code ImmutableSet} with the mapped elements
     * @param <S> the type of elements in the resulting set
     */
    public <S extends Serializable> ImmutableSet<S> map(Function<? super T, S> f) {
        return new ImmutableSet<>(elements.stream().map(f).collect(Collectors.toList()));
    }

    /**
     * Folds this set using the given function {@code f} to update the {@code accumulator} once with
     * each element <br>
     * The order in which elements are processed is guaranteed to be the same as the order in which
     * elements are yielded from {@code stream()} or {@code iterator()}.
     *
     * @param accumulator the initial result value
     * @param f the binary function to update the accumulator once with every element of this set
     * @return the accumulator after updating it once per element in this collection
     * @param <S> the type of the accumulator and result
     */
    public <S extends Serializable> S foldl(S accumulator, BiFunction<? super S, ? super T, S> f) {
        S result = accumulator;
        for (T e : elements) result = f.apply(result, e);
        return result;
    }

    /**
     * Filters out all elements of this set that do not satisfy the given predicate <br>
     * The order of elements in the resulting set is the same as the order in which those elements
     * occur in this set.
     *
     * @param p the predicate used to decide which values to keep
     * @return a new {@code ImmutableSet} with all elements from {@code this} that satisfy {@code p}
     */
    public ImmutableSet<T> filter(Predicate<? super T> p) {
        return new ImmutableSet<>(elements.stream().filter(p).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ImmutableSet)) return false;
        return Objects.equals(elements, ((ImmutableSet<?>) obj).elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    @Override
    public String toString() {
        return new SetWriter<T>().write(toUnmodifiableSet());
    }
}
