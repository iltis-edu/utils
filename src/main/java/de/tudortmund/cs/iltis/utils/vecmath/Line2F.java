package de.tudortmund.cs.iltis.utils.vecmath;

import java.util.Optional;

/** A {@link Line} implementation using 2D float vectors. */
public class Line2F extends Line<Float, Vector2F> {
    public Line2F(Vector2F base, Vector2F direction) {
        super(base, direction);
    }

    @Override
    public Line<Float, Vector2F> clone() {
        return new Line2F(getBase().copy(), getDirection().copy());
    }

    @Override
    public Optional<Vector2F> getIntersectionWith(Line<Float, Vector2F> other) {
        if (!this.isParallel(other)) {
            // Calculate the coordinate form of this and the other line.
            // a1 * x + b1 * x = c1
            // TODO: Move these calculations into separate methods?
            float a1 = -this.getDirection().y;
            float b1 = this.getDirection().x;
            float c1 = a1 * this.getBase().x + this.getBase().y * b1;

            // a2 * x + b2 * x = c2
            float a2 = -other.getDirection().y;
            float b2 = other.getDirection().x;
            float c2 = a2 * other.getBase().x + other.getBase().y * b2;

            // Use Cramer's rule to solve the equation system
            float xi = (c1 * b2 - c2 * b1) / (a1 * b2 - a2 * b1);
            float yi = (a1 * c2 - a2 * c1) / (a1 * b2 - a2 * b1);

            return Optional.of(new Vector2F(xi, yi));
        } else return Optional.empty();
    }
}
