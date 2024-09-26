package de.tudortmund.cs.iltis.utils.vecmath;

public class LineSegment2F extends LineSegment<Float, Vector2F> {
    public LineSegment2F(Line2F baseLine, Float length) {
        super(baseLine, length);
    }

    public LineSegment2F(Vector2F from, Vector2F to) {
        super(from, to);
    }

    @Override
    protected Line2F constructLine(Vector2F base, Vector2F direction) {
        return new Line2F(base, direction);
    }

    @Override
    protected boolean isGreaterEq(Float it, Float then) {
        return it >= then;
    }
}
