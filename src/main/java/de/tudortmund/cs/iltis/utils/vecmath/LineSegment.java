package de.tudortmund.cs.iltis.utils.vecmath;

import java.util.Optional;

/**
 * A line-segment is a subset of a line. Every line segment is bounded by two points, which are the
 * endpoints of the segment.
 *
 * @param <V> The basic (usually numeric) datatype.
 * @param <T> The {@link Vector} type to store information about this line.
 */
public abstract class LineSegment<D, V extends Vector<V, D>> {
    Line<D, V> baseLine;
    D length;

    public LineSegment(Line<D, V> baseLine, D length) {
        this.baseLine = baseLine;
        this.length = length;
    }

    public LineSegment(V from, V to) {
        V direction = to.subtract(from);
        D length = direction.len();
        Line<D, V> base = constructLine(from, direction);

        this.baseLine = base;
        this.length = length;
    }

    public Line<D, V> getBaseLine() {
        return baseLine;
    }

    public D getLength() {
        return length;
    }

    public boolean contains(V point) {
        V delta = point.subtract(getBaseLine().getBase());
        if (!delta.isZero() && !delta.isSameDirection(getBaseLine().getDirection())) return false;

        D lambda = getBaseLine().getLambda(point);
        return isGreaterEq(getLength(), lambda);
    }

    public Optional<V> getIntersectionWith(LineSegment<D, V> other) {
        // We just use the intersection between the base lines and check if it is contained in both
        // line segments.
        Optional<V> linei = getBaseLine().getIntersectionWith(other.getBaseLine());
        return linei.filter(
                v -> {
                    return this.contains(v) && other.contains(v);
                });
    }

    protected abstract Line<D, V> constructLine(V base, V direction);

    protected abstract boolean isGreaterEq(D it, D then);
}
