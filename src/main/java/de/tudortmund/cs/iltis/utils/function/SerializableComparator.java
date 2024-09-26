package de.tudortmund.cs.iltis.utils.function;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A normal {@link Comparator} which also implements the {@link Serializable} interface (to satisfy
 * GWT).
 *
 * @param <T> The type of objects which shall be compared with each other
 */
@FunctionalInterface
public interface SerializableComparator<T> extends Comparator<T>, Serializable {

    /** Returns a SerializableComparator which uses the natural order of the objects. */
    static <T extends Comparable<? super T>> SerializableComparator<T> naturalOrder() {
        return new SerializableComparator<T>() {
            @Override
            public int compare(T t, T t1) {
                return t.compareTo(t1);
            }
        };
    }
}
