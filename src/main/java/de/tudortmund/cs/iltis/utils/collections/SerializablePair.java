package de.tudortmund.cs.iltis.utils.collections;

import java.io.Serializable;

/**
 * A non-immutable, serializable pair, consisting of two elements of serializable types. Equality is
 * ensured among all PairLike implementing classes.
 *
 * @see PairLike
 */
public class SerializablePair<A extends Serializable, B extends Serializable>
        extends ImmutableSerializablePair<A, B> {

    /** For GWT serialization */
    protected SerializablePair() {}

    public SerializablePair(A first, B second) {
        super(first, second);
    }

    public SerializablePair(PairLike<A, B> pair) {
        super(pair);
    }

    public void setFirst(A first) {
        super.setFirst(first);
    }

    public void setSecond(B second) {
        super.setSecond(second);
    }

    /** Makes a shallow copy, i.e. not clones its elements. */
    public SerializablePair<A, B> clone() {
        return new SerializablePair<>(this);
    }
}
