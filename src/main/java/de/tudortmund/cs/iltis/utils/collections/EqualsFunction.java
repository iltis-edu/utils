package de.tudortmund.cs.iltis.utils.collections;

import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;

/**
 * A serializable function that returns true iff its two arguments are equal.
 *
 * @param <T> the type of the two arguments of this function
 */
public class EqualsFunction<T> implements SerializableBiFunction<T, T, Boolean> {

    public EqualsFunction() {}

    @Override
    public Boolean apply(T t, T u) {
        return t.equals(u);
    }

    public EqualsFunction<T> clone() {
        return new EqualsFunction<T>();
    }
}
