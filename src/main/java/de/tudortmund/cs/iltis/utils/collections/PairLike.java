package de.tudortmund.cs.iltis.utils.collections;

/**
 * An interface for pairs.
 *
 * <p><b>Note:</b> This interface is used to give a unified interface for SerializablePair and
 * (non-serializable) Pair. SerializablePair cannot just override Pair, because the attributes of a
 * non-serializable super-class will not be serialized.
 */
public interface PairLike<A, B> {

    A first();

    B second();

    default String getPairLikeString() {
        return "(" + first() + "," + second() + ")";
    }

    /** Used to implement equality across PairLike instances */
    default boolean isPairLikeEqual(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof PairLike)) return false;
        PairLike<A, B> other = (PairLike<A, B>) obj;
        if (this.first() == null) {
            if (other.first() != null) return false;
        } else if (!this.first().equals(other.first())) return false;
        if (this.second() == null) {
            if (other.second() != null) return false;
        } else if (!this.second().equals(other.second())) return false;
        return true;
    }

    /** Used to implement equality across PairLike instances */
    default int getPairLikeHashCode() {
        final int prime = 31;
        int result = 38145;
        result = prime * result + ((first() == null) ? 0 : first().hashCode());
        result = prime * result + ((second() == null) ? 0 : second().hashCode());
        return result;
    }
}
