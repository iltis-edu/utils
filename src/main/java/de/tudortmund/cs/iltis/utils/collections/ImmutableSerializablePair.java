package de.tudortmund.cs.iltis.utils.collections;

import java.io.Serializable;

/**
 * An immutable, serializable pair, consisting of two elements of serializable types. Equality is
 * ensured among all PairLike implementing classes.
 *
 * @see PairLike
 */
public class ImmutableSerializablePair<A extends Serializable, B extends Serializable>
        implements PairLike<A, B>, Serializable, Cloneable {

    private A first;
    private B second;

    /** For GWT serialization */
    protected ImmutableSerializablePair() {}

    public ImmutableSerializablePair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public ImmutableSerializablePair(PairLike<A, B> pair) {
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
    public ImmutableSerializablePair<A, B> clone() {
        return new ImmutableSerializablePair<>(this);
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
