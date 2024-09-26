package de.tudortmund.cs.iltis.utils.collections;

/**
 * A non-immutable, non-serializable pair, consisting of two elements of specified types. Equality
 * is ensured among all PairLike implementing classes.
 *
 * @see PairLike
 */
public class Pair<A, B> extends ImmutablePair<A, B> {
    public Pair(A first, B second) {
        super(first, second);
    }

    public Pair(PairLike<A, B> pair) {
        super(pair);
    }

    public void setFirst(A first) {
        super.setFirst(first);
    }

    public void setSecond(B second) {
        super.setSecond(second);
    }

    /** Makes a shallow copy, i.e. not clones its elements. */
    public Pair<A, B> clone() {
        return new Pair<>(this);
    }
}
