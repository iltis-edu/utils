package de.tudortmund.cs.iltis.utils.collections;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

/**
 * This class is a shallow wrapping around a map whose keys are the classes of the values. This
 * class is immutable and therefore <b>not</b> a superclass of {@link Map}. This class is used as
 * super class of FormulaFaultTypeMapping to bundle fault collections while parsing.
 *
 * @param <T> type for objects to be inserted in the underlying map
 */
public class TypeMapping<T> implements Cloneable, Serializable {

    protected Map<String, T> objects;

    /** Creates a new TypeMapping-object which contains no objects. */
    public TypeMapping() {
        objects = new TreeMap<>();
    }

    /**
     * Creates a new TypeMapping-object which contains all objects of {@code objects }.
     *
     * @throws NullPointerException if {@code objects} is null
     */
    public TypeMapping(Map<String, T> objects) {
        Objects.requireNonNull(objects);
        this.objects = new TreeMap<>(objects);
    }

    /**
     * Creates a new TypeMapping-object which contains all previously contained objects and
     * additionally the given object. If the type of {@code o} is already present in this mapping,
     * the existing object is replaced.
     *
     * @throws NullPointerException if {@code o} is null
     */
    public TypeMapping<T> with(T o) {
        Objects.requireNonNull(o);

        String classOfO = o.getClass().getName();
        TypeMapping<T> clone = this.clone();
        clone.objects.put(classOfO, o);
        return clone;
    }

    /**
     * Creates a new TypeMapping-object which contains all previously contained objects and
     * additionally all objects of the given mapping. If {@code mapping} contains objects of types
     * already present in this mapping, the existing objects are replaced.
     *
     * @throws NullPointerException if {@code mapping} is null
     */
    public TypeMapping<T> with(TypeMapping<? extends T> mapping) {
        Objects.requireNonNull(mapping);

        TypeMapping<T> clone = this.clone();
        for (Entry<String, ? extends T> entry : mapping.getAll().entrySet()) {
            T o = entry.getValue();
            String classOfO = entry.getKey();
            clone.objects.put(classOfO, o);
        }
        return clone;
    }

    /** Gets the object of the given type and null if none is present. */
    public T get(String type) {
        return objects.get(type);
    }

    /** Gets the object of the given type and null if none is present. */
    public T get(Class<? extends T> type) {
        return objects.get(type.getName());
    }

    /** Gets a copy of the internal HashMap. */
    public Map<String, T> getAll() {
        return new TreeMap<>(objects);
    }

    /** Checks whether any object is contained at all. */
    public boolean containsAny() {
        return !objects.isEmpty();
    }

    /** Checks whether an object for the given type is contained. */
    public boolean contains(Class<? extends T> type) {
        return objects.containsKey(type.getName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((objects == null) ? 0 : objects.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TypeMapping<?> other = (TypeMapping<?>) obj;
        if (objects == null) {
            if (other.objects != null) return false;
        } else if (!objects.equals(other.objects)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "TypeMapping [objects = " + objects + "]";
    }

    public TypeMapping<T> clone() {
        return new TypeMapping<>(objects);
    }

    /** for serialization */
    private static final long serialVersionUID = 1L;
}
