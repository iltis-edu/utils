package de.tudortmund.cs.iltis.utils.collections.immutable;

import de.tudortmund.cs.iltis.utils.collections.PairLike;
import de.tudortmund.cs.iltis.utils.function.SerializableFunction;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The fundamental purpose of this class is to provide an immutable map implementation which can be
 * used to store map of key-value pairs inside immutable objects or return long-living results from
 * algorithms. It aims at providing strong compile-time correctness, avoiding surprising
 * runtime-exceptions and being efficient.
 *
 * <p>This class <b>intentionally</b> does <b>not</b> implement {@code Collection} or {@code Map},
 * because these interfaces impose methods like {@code put} or {@code clear}. To interact with
 * Java-Collection API one should use {@link ImmutableMap#toUnmodifiableMap()}. Note that this
 * method returns an <b>unmodifiable view</b>, i.e. all methods that would structurally change the
 * collection will result in {@code UnsupportedOperationException}. Also, since a view is returned,
 * this method is constant time and <b>not</b> linear in the size of this map.
 *
 * <p>If you need a mutable map object, you should create one explicitly. If you need an immutable
 * copy, you don't need a copy. You can simply pass this object directly, that's the point after
 * all. To further enforce this point, there is no constructor that allows you to create an {@code
 * ImmutableMap} from another {@code ImmutableMap} directly.
 *
 * <p>Since this class is immutable it is not suited to be used as local variables which are
 * updated. Instead, use a default Java Collection which is mutable and meets your performance
 * requirements. The final result of the method can then be put into an {@code ImmutableMap} to
 * safely store it without worrying that it may be altered. To illustrate this fact {@code
 * ImmutableMap} does <b>not</b> have immutable versions of methods like {@code put} which return a
 * copy. This would simply lead to a terrible runtime performance.
 *
 * <p>The implementation of this map guarantees a consistent order of its elements. In particular,
 * the order in which the key-value pairs are given to the constructor is maintained throughout and
 * passed on to all results from methods like {@code filter} or {@code map}. If you want to subclass
 * this class, please note that all subclasses must be immutable as well. You must <b>never</b>
 * modify {@code elements} yourself, since it may be shared between instances for performance
 * optimization. Use {@code super(...)} instead to pass data during the construction. Also, please
 * note the utility method {@link ImmutableMap#shareConstructInto} to covariantly override some
 * methods without creating unnecessary copies.
 *
 * @param <K> the type of keys contained in this map
 * @param <V> the type of values contained in this map
 */
public class ImmutableMap<K extends Serializable, V extends Serializable>
        implements Serializable, Iterable<ImmutableMap.Entry<K, V>>, SerializableFunction<K, V> {

    /* Should be final, but must not be marked `final`, otherwise GWT serialization will break :( */
    @SuppressWarnings("FieldMayBeFinal")
    private LinkedHashMap<K, V> hashMap = new LinkedHashMap<>();

    /* For serialization */
    @SuppressWarnings("unused")
    protected ImmutableMap() {}

    /**
     * Create a new ImmutableMap from the given key-value pairs <br>
     * If two pairs have the same key, the latter overwrites/shadows the former
     */
    @SafeVarargs
    public ImmutableMap(PairLike<? extends K, ? extends V>... pairs) {
        this(Arrays.asList(pairs));
    }

    /**
     * Create a new ImmutableMap from the given collection of key-value pairs <br>
     * If two pairs have the same key, the latter overwrites/shadows the former
     */
    public ImmutableMap(Collection<? extends PairLike<? extends K, ? extends V>> collection) {
        collection.forEach(p -> hashMap.put(p.first(), p.second()));
    }

    /**
     * Create a new ImmutableMap based on key-set (domain) and a function to map each key to its
     * value <br>
     * If the {@code domain} contains two equal keys, the latter overwrites/shadows the former
     *
     * @param domain the set of keys
     * @param map the function to map each key to its associated value
     */
    public ImmutableMap(Collection<? extends K> domain, Function<? super K, V> map) {
        domain.forEach(key -> hashMap.put(key, map.apply(key)));
    }

    /**
     * Utility method to construct a new instance of this class (or a subclass) which shares the
     * internal data with an existing one.
     *
     * <p><b>Only for internal use! Only use if you know what you are doing! The source object will
     * be invalid!</b> The idea of this method is to allow subclasses to efficiently and covariantly
     * override methods that return instance of {@code ImmutableMap}. For example, {@link
     * ImmutableMap#map} returns a new {@code ImmutableMap}. However, a subclass may want to return
     * instances of that subclass instead. To do this, one could<br>
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
     * An example might be useful: assume we have a subclass {@code MyMap<K, V> extends
     * ImmutableMap<K, V>} and we want {@code MyMap<K, V>::map} to return an instance of {@code
     * MyMap} again. Then we can overwrite {@code map} like this:
     *
     * <pre>
     * public MyMap&lt;K, V> map(...) {
     *     return shareConstructInto(MyMap::new, super.map(f));
     * }
     * </pre>
     *
     * Obviously, this only works if {@code MyMap} offers a constructor with 0 arguments, which is a
     * reasonable assumption for a map type. <br>
     * <br>
     * This method is safe for any {@code source} (temporary or not), but returning a non-temporary
     * instance from {@code targetSupplier} is not, so please follow the example above!
     *
     * @param targetSupplier a function to create a new instance of the return type
     * @param source the temporary object from which we "adopt" the internal data
     * @return a new instance of type Coll with the data from source
     * @param <K> the type of keys contained in the subclass
     * @param <V> the type of values contained in the subclass
     * @param <Coll> the concrete subtype of ImmutableMap of which the result shall be an instance
     */
    protected static <
                    K extends Serializable, V extends Serializable, Coll extends ImmutableMap<K, V>>
            Coll shareConstructInto(Supplier<Coll> targetSupplier, ImmutableMap<K, V> source) {
        Coll target = targetSupplier.get();
        ((ImmutableMap<K, V>) target).hashMap = source.hashMap;
        return target;
    }

    /**
     * Create a new ImmutableMap based on the given map
     *
     * @param map the map from which the entries are taken
     */
    public ImmutableMap(Map<? extends K, ? extends V> map) {
        hashMap.putAll(map);
    }

    /**
     * Returns the number of key-value pairs in this map
     *
     * @return the number of key-value pairs in this map
     */
    public int size() {
        return hashMap.size();
    }

    /**
     * Tests if this map is empty
     *
     * @return {@code true}, iff there are not elements in this map
     */
    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    /**
     * Tests whether the given key is registered in the map
     *
     * @param key the key to look for
     * @return {@code true}, iff there is a value associated to the given key in this map
     */
    public boolean containsKey(K key) {
        return hashMap.containsKey(key);
    }

    /**
     * Retrieves the value associated with the given key
     *
     * @param key the look-up key
     * @return the value associated with {@code key} or {@code null}, if no such key exists in this
     *     map
     */
    public V get(K key) {
        return hashMap.get(key);
    }

    /**
     * Retrieves the value associated with the given key or the backup value, if no such key is
     * registered
     *
     * @param key the look-up key
     * @param backup the backup value to provide if {@code key} is not registered
     * @return the value associated with {@code key} or {@code backup}, if no such key exists in
     *     this map
     */
    public V getOrDefault(K key, V backup) {
        return hashMap.getOrDefault(key, backup);
    }

    /**
     * Gets the set of all keys in this map
     *
     * @return the set of all registered keys
     */
    public Set<K> keySet() {
        return Collections.unmodifiableSet(hashMap.keySet());
    }

    /**
     * Gets a collection of all values stored in this map
     *
     * @return all values in this map
     */
    public Collection<V> values() {
        return Collections.unmodifiableCollection(hashMap.values());
    }

    /**
     * Builds a stream over all key-value pairs in this map
     *
     * @return a stream over all key-value pairs in this map
     */
    public Stream<Entry<K, V>> stream() {
        return hashMap.entrySet().stream().map(e -> new Entry<>(e.getKey(), e.getValue()));
    }

    /**
     * Builds an iterator over all elements in this map
     *
     * @return an iterator over the key-value pairs in this map
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return stream().iterator();
    }

    /**
     * Creates an "unmodifiable (map) view" of this map
     *
     * <p>This method should be used when interacting with the native Java Collections API, since
     * this class does not implement {@code Map} or {@code Collection} (intentionally).
     *
     * @return an unmodifiable view of this map
     */
    public Map<K, V> toUnmodifiableMap() {
        return Collections.unmodifiableMap(hashMap);
    }

    /**
     * Interprets this {@code ImmutableMap} as a function f: K -> V and applies it to the argument
     *
     * @param k the function argument
     * @return the value associated with {@code k} in this map of {@code null}, if {@code k} is not
     *     registered
     */
    @Override
    public V apply(K k) {
        return hashMap.get(k);
    }

    /**
     * Map all key-value pairs using the separate maps for keys and values <br>
     * The order in which key-value pairs are processed and stored in the new map is guaranteed to
     * be identical to the order in which key-value pairs are yielded from {@code stream}.
     *
     * @param keyMap the function to map each key
     * @param valueMap the function to map each value
     * @return a new {@code ImmutableMap} which contains {@code (keyMap(k), valueMap(v))} for all
     *     {@code (k, v)} in this map
     * @param <J> the new type of keys
     * @param <S> the new type of values
     */
    public <J extends Serializable, S extends Serializable> ImmutableMap<J, S> map(
            Function<? super K, J> keyMap, Function<? super V, S> valueMap) {
        return map(entry -> new Entry<>(keyMap.apply(entry.key), valueMap.apply(entry.value)));
    }

    public <J extends Serializable, S extends Serializable> ImmutableMap<J, S> map(
            Function<? super Entry<? extends K, ? extends V>, Entry<J, S>> f) {
        ImmutableMap<J, S> result = new ImmutableMap<>();
        for (Entry<K, V> entry : this) {
            Entry<J, S> newEntry = f.apply(entry);
            result.hashMap.put(newEntry.key, newEntry.value);
        }
        return result;
    }

    /**
     * Folds this maps using the function {@code f} to update the {@code accumulator} once with each
     * key-value pair <br>
     * The order in which key-value pairs are processed is guaranteed to be the same as the order in
     * which key-value pairs are yielded from {@code stream()} or {@code iterator}.
     *
     * @param accumulator the initial result value
     * @param f the binary function to update the accumulator once with each key-value pair
     * @return the accumulator after updating it once per key-value pair in this map
     * @param <S> the type of the accumulator and result
     */
    public <S extends Serializable> S foldl(
            S accumulator, BiFunction<? super S, ? super Entry<? extends K, ? extends V>, S> f) {
        S result = accumulator;
        for (Entry<K, V> entry : this) {
            result = f.apply(result, entry);
        }
        return result;
    }

    /**
     * Filters out all key-value-pairs of this map that do not satisfy the given predicate <br>
     * The order of key-value pairs in the result map is the same as they occur in this map
     *
     * @param p the predicate used to decide which values to keep
     * @return a new {@code ImmutableMap} with all elements from {@code this} that satisfy {@code p}
     */
    public ImmutableMap<K, V> filter(Predicate<? super Entry<K, V>> p) {
        ImmutableMap<K, V> result = new ImmutableMap<>();
        for (Entry<K, V> entry : this) {
            if (p.test(entry)) {
                result.hashMap.put(entry.key, entry.value);
            }
        }
        return result;
    }

    /**
     * Merges two {@code ImmtableMap}s into one, where identical keys are shadowed by the latter map
     *
     * @param other the other map from which the key-value pairs should be merged into this one
     * @return a new {@code ImmutableMap}, which contains the "union of keys" of {@code this} and
     *     {@code other}
     */
    public ImmutableMap<K, V> merge(ImmutableMap<? extends K, ? extends V> other) {
        if (this == other) return this;
        ImmutableMap<K, V> result = new ImmutableMap<>();
        forEach(e -> result.hashMap.put(e.key, e.value));
        other.forEach(e -> result.hashMap.put(e.key, e.value));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ImmutableMap)) return false;
        return Objects.equals(hashMap, ((ImmutableMap<?, ?>) obj).hashMap);
    }

    @Override
    public int hashCode() {
        return hashMap.hashCode();
    }

    @Override
    public String toString() {
        return "{" + stream().map(Object::toString).collect(Collectors.joining(",")) + "}";
    }

    /**
     * A utility class to encapsulate key-value pairs which do not write structural changes through
     * to the ImmutableMap from which they originate.
     *
     * @param <K> the type of keys used in the map
     * @param <V> the type of values used in the map
     */
    public static final class Entry<K extends Serializable, V extends Serializable>
            implements Serializable {
        public K key;
        public V value;

        /* For serialization */
        @SuppressWarnings("unused")
        private Entry() {}

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Entry)) return false;
            Entry<?, ?> o = (Entry<?, ?>) obj;
            return Objects.equals(key, o.key) && Objects.equals(value, o.value);
        }

        @Override
        public String toString() {
            return "(" + key + "," + value + ")";
        }
    }
}
