package de.tudortmund.cs.iltis.utils.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BiHashMap<K, V> implements Map<K, V>, Serializable {

    private HashMap<K, V> map;
    private HashMap<V, K> inverse;

    public BiHashMap() {
        this.map = new HashMap<>();
        this.inverse = new HashMap<>();
    }

    public HashMap<V, K> inverse() {
        return inverse;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        if (map.containsValue(value)) {
            K oldKey = inverse.get(value);
            map.remove(oldKey);
        }
        V oldValue = map.put(key, value);

        if (oldValue != null) {
            inverse.remove(oldValue);
        }
        inverse.put(value, key);
        return oldValue;
    }

    @Override
    public V remove(Object key) {
        V value = map.remove(key);
        inverse.remove(value);
        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (K key : m.keySet()) {
            this.put(key, m.get(key));
        }
    }

    @Override
    public void clear() {
        map.clear();
        inverse.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    public BiHashMap<K, V> clone() {
        BiHashMap<K, V> clone = new BiHashMap<>();
        clone.inverse = (HashMap<V, K>) inverse.clone();
        clone.map = (HashMap<K, V>) map.clone();
        return clone;
    }
}
