package de.tudortmund.cs.iltis.utils.vecmath;

import java.io.Serializable;

/**
 * Common definitions for multi-dimensional Vectors implemented using an arbitrary datatype for the
 * coordinates.
 *
 * @param <V> The actual vector type.
 * @param <D> The Datatype of the coordinates.
 */
public interface Vector<V extends Vector<V, D>, D> extends Serializable {
    /**
     * @return The squared length of a vector.
     */
    D sqlen();

    /**
     * @return A copy of this vector
     */
    V copy();

    /**
     * Add another vector to this vector in-place
     *
     * @param other The vector to add
     * @return A reference to the vector this method was called on
     */
    V addI(V other);

    /**
     * Subtract another vector from this vector in-place
     *
     * @param other The vector to subtract
     * @return A reference to the vector this method was called on
     */
    V subtractI(V other);

    /**
     * Scale this vector by the given factor in-place.
     *
     * @param factor Is applied to every coordinate of this vector.
     * @return A reference to the vector this method was called on.
     */
    V scaleI(D factor);

    /**
     * @return The dimension of this Vector. Should be constant for every instance of the the V
     */
    int dim();

    /**
     * Gets the value this vector contains in its ith coordinate.
     *
     * @param i The index (starting at 0) of the coordinate
     * @return The value of the ith coordinate
     */
    D get(int i);

    /**
     * Calculates the dot product of two vectors.
     *
     * @param other The other vector.
     * @return The value of the dot product of this and the other vector.
     */
    D dot(V other);

    /**
     * @return The length of this vector. Usually the square-root of {@link #sqlen()}
     */
    D len();

    /**
     * Normalize this vector in-place.
     *
     * @return A reference to this vector.
     */
    V normalizeI();

    /**
     * Invert this vector, i.e. multiply it with -1.
     *
     * @return A reference to this vector
     */
    V inverseI();

    /**
     * @return true, if the length of this vector (and thus all coordinates) are equal to 0
     */
    boolean isZero();

    /**
     * @return true, if this and other are equal in each coordinate considering a certain precision
     *     threshold.
     */
    boolean almostEqual(V other);

    /**
     * Invert a copy of this vector, i.e. multiply it with -1.
     *
     * @return A reference to the new vector
     */
    default V inverse() {
        return copy().inverseI();
    }

    /**
     * Checks if two vectors have the same direction, i.e. a positive scaling factor between the two
     * exists.
     *
     * @param other the other vector
     * @return true, if the vectors have the same direction.
     */
    default boolean isSameDirection(V other) {
        V thisn = this.normalize();
        V othern = other.normalize();
        return thisn.almostEqual(othern);
    }

    /**
     * Checks if two vectors are parallel, i.e. any scaling factor between the two exists.
     *
     * @param other the other vector
     * @return true, if the vectors are parallel.
     */
    default boolean isParallel(V other) {
        V thisn = this.normalize();
        V othern = other.normalize();

        return thisn.almostEqual(othern) || thisn.almostEqual(othern.inverse());
    }

    /**
     * Add another vector to a copy of this vector.
     *
     * @param other The vector to add
     * @return A new Vector containing the sum of this and the other vector.
     */
    default V add(V other) {
        return copy().addI(other);
    }

    /**
     * Subtract another vector from a copy of this vector.
     *
     * @param other The vector to subtract
     * @return A new Vector containing the difference of this and the other vector.
     */
    default V subtract(V other) {
        return copy().subtractI(other);
    }

    /**
     * Scales a copy of this vector by the given factor.
     *
     * @param factor The factor to use for scaling.
     * @return A new scaled vector.
     */
    default V scale(D factor) {
        return copy().scaleI(factor);
    }

    /**
     * Normalize a copy of this vector.
     *
     * @return A new normalized vector.
     */
    default V normalize() {
        return copy().normalizeI();
    }
}
