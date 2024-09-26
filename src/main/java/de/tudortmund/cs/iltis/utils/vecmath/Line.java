package de.tudortmund.cs.iltis.utils.vecmath;

import java.util.Optional;

/**
 * This class represents infinite lines in any dimension. It offers some basic functionality for
 * checking if two lines intersect etc.
 *
 * @param <D> The basic (usually numeric) datatype.
 * @param <V> The {@link Vector} type to store information about this line.
 */
public abstract class Line<D, V extends Vector<V, D>> {
    private V base, direction;

    public Line(V base, V direction) {
        this.base = base;
        this.direction = direction.normalize();
    }

    public V getBase() {
        return base;
    }

    public void setBase(V base) {
        this.base = base;
    }

    public V getDirection() {
        return direction;
    }

    public void setDirection(V direction) {
        this.direction = direction;
    }

    public V getPoint(D where) {
        return getBase().add(getDirection().scale(where));
    }

    public boolean isParallel(Line<D, V> other) {
        return this.getDirection().equals(other.getDirection());
    }

    public boolean isIdentical(Line<D, V> other) {
        return isParallel(other) && contains(other.getBase());
    }

    public boolean contains(V point) {
        V delta = point.subtract(getBase());
        return delta.isParallel(this.getDirection()) || delta.isZero();
    }

    public D getLambda(V point) {
        if (!contains(point))
            throw new IllegalArgumentException(
                    "The given point "
                            + point
                            + " is not part of the line defined by "
                            + this
                            + "!");
        V delta = point.subtract(getBase());
        if (!delta.isSameDirection(getDirection())) delta.inverseI();
        return delta.len();
    }

    @Override
    public String toString() {
        return getBase() + " + l*" + getDirection();
    }

    public abstract Line<D, V> clone();

    public abstract Optional<V> getIntersectionWith(Line<D, V> other);
}
