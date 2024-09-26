package de.tudortmund.cs.iltis.utils.collections;

/**
 * An immutable, non-serializable pair, consisting of two elements of specified types. Equality is
 * ensured among all PairLike implementing classes.
 *
 * @see PairLike
 */
public class ImmutablePair<A, B> implements PairLike<A, B>, Cloneable {

    private A first;
    private B second;

    public ImmutablePair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public ImmutablePair(PairLike<A, B> pair) {
        this.first = pair.first();
        this.second = pair.second();
    }

    @Override
    public A first() {
        return this.first;
    }

    @Override
    public B second() {
        return this.second;
    }

    protected void setFirst(A first) {
        this.first = first;
    }

    protected void setSecond(B second) {
        this.second = second;
    }

    /** Makes a shallow copy, i.e. not clones its elements. */
    public ImmutablePair<A, B> clone() {
        return new ImmutablePair<>(this);
    }

    @Override
    public int hashCode() {
        return getPairLikeHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return isPairLikeEqual(obj);
    }

    @Override
    public String toString() {
        return getPairLikeString();
    }
}
