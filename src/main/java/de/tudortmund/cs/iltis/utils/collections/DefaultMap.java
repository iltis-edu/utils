package de.tudortmund.cs.iltis.utils.collections;

import de.tudortmund.cs.iltis.utils.function.SerializableFunction;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class implements a {@link Map} with defaults. A function mapping keys to default values is
 * used to allow for flexible default values.
 *
 * @param <K> The key type of the map.
 * @param <V> The values type of the map.
 */
public class DefaultMap<K, V> implements Map<K, V>, Serializable {

    private HashMap<K, V> backend;
    private SerializableFunction<K, V> defaultSupplier;

    private DefaultMap() {}

    /**
     * Creates a DefaultMap using a standart {@link HashMap} for its backend.
     *
     * @param defaultSupplier The function for supplying defaults.
     */
    public DefaultMap(SerializableFunction<K, V> defaultSupplier) {
        this(new HashMap<>(), defaultSupplier);
    }

    /**
     * Creates a DefaultMap using the specified backend as its base.
     *
     * @param backend The backend map to store the key-value associations in.
     * @param defaultSupplier
     */
    public DefaultMap(HashMap<K, V> backend, SerializableFunction<K, V> defaultSupplier) {
        this.backend = backend;
        this.defaultSupplier = defaultSupplier;
    }

    @Override
    public void clear() {
        backend.clear();
    }

    /**
     * This method will return true, if a. the given key was requested previously using {@link
     * #get(Object)} or b. the given key has an associated value which was set using {@link
     * #put(Object, Object)} or {@link #putAll(Map)}
     */
    @Override
    public boolean containsKey(Object key) {
        return backend.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return backend.containsValue(value);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return backend.entrySet();
    }

    /**
     * Returns the value associated with the key. If no such value exists, a default is stored in
     * the map and returned.
     */
    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        if (!backend.containsKey(key)) {
            // TODO: This will cause an exception if key is not an instance of K.
            // 		 We could check key instanceof K and throw an IllegalArgument exception if it is
            // false.
            V def = defaultSupplier.apply((K) key);
            put((K) key, def);
            return def;
        } else return backend.get(key);
    }

    @Override
    public boolean isEmpty() {
        return backend.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return backend.keySet();
    }

    @Override
    public V put(K key, V value) {
        return backend.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> other) {
        backend.putAll(other);
    }

    @Override
    public V remove(Object key) {
        return backend.remove(key);
    }

    @Override
    public int size() {
        return backend.size();
    }

    @Override
    public Collection<V> values() {
        return backend.values();
    }

    public DefaultMap<K, V> clone() {
        return new DefaultMap<>(backend, defaultSupplier);
    }
}
