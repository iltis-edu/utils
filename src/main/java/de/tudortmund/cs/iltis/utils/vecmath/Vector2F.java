package de.tudortmund.cs.iltis.utils.vecmath;

/** An implementation of a 2-dimensional Vector based on floats. */
public class Vector2F implements Vector<Vector2F, Float> {

    // This value is set arbitrarily and might need changing
    public static final Float PRECISION = 1E-5F;

    public float x, y;

    /**
     * Create a new vector using the given coordinates.
     *
     * @param x The x coordinate of the new vector.
     * @param y The y coordinate of the new vector.
     */
    public Vector2F(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Float sqlen() {
        return x * x + y * y;
    }

    @Override
    public Vector2F copy() {
        return new Vector2F(x, y);
    }

    @Override
    public Vector2F addI(Vector2F o) {
        x += o.x;
        y += o.y;
        return this;
    }

    @Override
    public Vector2F subtractI(Vector2F o) {
        x -= o.x;
        y -= o.y;
        return this;
    }

    @Override
    public Vector2F scaleI(Float factor) {
        x *= factor;
        y *= factor;
        return this;
    }

    @Override
    public Float dot(Vector2F o) {
        return x * o.x + y * o.y;
    }

    /**
     * Calculates the angle between this and another vector. The angle is calculated in degrees in
     * the range [0, 360).
     *
     * <p>(0, 1).angle((1, 0)) should return 90 (The vector (0, 1) is rotated 90° counter-clockwise
     * from (1, 0))
     *
     * @param o The vector to base the angle on.
     * @return The angle in degrees.
     */
    public double angle(Vector2F o) {
        double angle = (-(180 / Math.PI) * Math.atan2(x * o.y - y * o.x, x * o.x + y * o.y)) % 360;
        while (angle < 0) angle += 360;
        return angle;
    }

    @Override
    public int dim() {
        return 2;
    }

    @Override
    public Float get(int i) {
        switch (i) {
            case 0:
                return x;
            case 1:
                return y;
            default:
                throw new IllegalArgumentException("Index " + i + " out of bounds.");
        }
    }

    @Override
    public Float len() {
        return (float) Math.sqrt(sqlen());
    }

    @Override
    public Vector2F normalizeI() {
        return scaleI(1 / len());
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    @Override
    public Vector2F inverseI() {
        return scaleI(-1F);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Vector2F other = (Vector2F) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) return false;
        return true;
    }

    public Vector2F rotI(float degrees) {
        float rad, cos, sin, x, y;
        rad = (float) Math.toRadians(degrees);
        cos = (float) Math.cos(rad);
        sin = (float) Math.sin(rad);
        x = this.x;
        y = this.y;

        this.x = x * cos - y * sin;
        this.y = x * sin + y * cos;

        return this;
    }

    public Vector2F rot(float degrees) {
        return copy().rotI(degrees);
    }

    @Override
    public boolean isZero() {
        return len() == 0F || len() == -0F;
    }

    private static boolean almostEqual(float x1, float x2) {
        return Math.abs(x1 - x2) <= PRECISION;
    }

    @Override
    public boolean almostEqual(Vector2F other) {
        return almostEqual(x, other.x) && almostEqual(y, other.y);
    }

    // Required for serialization
    @SuppressWarnings("unused")
    private Vector2F() {}
}
