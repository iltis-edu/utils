package de.tudortmund.cs.iltis.utils.function;

import java.io.Serializable;
import java.util.function.BiPredicate;

/** A bipredicate that is serializable, nothing more. */
public interface SerializableBiPredicate<T, U> extends BiPredicate<T, U>, Serializable {

    @Override
    default SerializableBiPredicate<T, U> and(BiPredicate<? super T, ? super U> other) {
        return (t, u) -> test(t, u) && other.test(t, u);
    }

    @Override
    default SerializableBiPredicate<T, U> or(BiPredicate<? super T, ? super U> other) {
        return (t, u) -> test(t, u) || other.test(t, u);
    }
}
