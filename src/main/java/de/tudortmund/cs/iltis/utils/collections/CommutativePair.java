package de.tudortmund.cs.iltis.utils.collections;

import java.io.Serializable;

/**
 * A {@link SerializablePair} that is considered equal to another pair by its {@link
 * #equals(Object)}-method when they consist of the same elements, regardless of their order in the
 * pair.
 *
 * @param <A> the type of the elements of a pair
 */
public class CommutativePair<A extends Serializable> extends SerializablePair<A, A> {

    /** For GWT serialization */
    protected CommutativePair() {}

    public CommutativePair(A first, A second) {
        super(first, second);
    }

    public CommutativePair(PairLike<A, A> pair) {
        super(pair);
    }

    @Override
    public boolean equals(Object o) {
        CommutativePair<A> comPair = new CommutativePair<>(second(), first());
        return isPairLikeEqual(o) || comPair.isPairLikeEqual(o);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 38145;
        result += prime * ((first() == null) ? 0 : first().hashCode());
        result += prime * ((second() == null) ? 0 : second().hashCode());
        return result;
    }
}
