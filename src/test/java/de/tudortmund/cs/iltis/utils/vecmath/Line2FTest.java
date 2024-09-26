package de.tudortmund.cs.iltis.utils.vecmath;

import static org.junit.Assert.*;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class Line2FTest {

    Line2F line1, line2, line3, line4, line5;
    Line2F[] allLines;

    @Before
    public void prepare() {
        line1 = new Line2F(new Vector2F(0, 0), new Vector2F(0, 1));
        line2 = new Line2F(new Vector2F(-10, 0), new Vector2F(1, 10));
        line3 = new Line2F(new Vector2F(42, 1), new Vector2F(0, 20));

        line4 = (Line2F) new LineSegment2F(new Vector2F(-10, 5), new Vector2F(5, 5)).getBaseLine();
        line5 = (Line2F) new LineSegment2F(new Vector2F(5, 5), new Vector2F(4, 4)).getBaseLine();

        allLines = new Line2F[] {line1, line2, line3, line4, line5};
    }

    @Test
    public void intersect12correct() {
        Optional<Vector2F> intersection = line1.getIntersectionWith(line2);
        assertTrue(intersection.isPresent());
        AssertionUtilities.assertAlmostEquals("", new Vector2F(0, 100), intersection.get());
    }

    @Test
    public void intersect13correct() {
        Optional<Vector2F> intersection = line1.getIntersectionWith(line3);
        assertFalse(intersection.isPresent());
    }

    @Test
    public void intersect23correct() {
        Optional<Vector2F> intersection = line2.getIntersectionWith(line3);
        assertTrue(intersection.isPresent());
        AssertionUtilities.assertAlmostEquals(
                "", new Vector2F(42, 100 + 42 * 10), intersection.get());
    }

    @Test
    public void intersectSymmetrical() {
        for (int i = 0; i < allLines.length; i++) {
            for (int j = i + 1; j < allLines.length; j++) {
                AssertionUtilities.assertAlmostEquals(
                        "Intersection between " + i + " and " + j + " is not symmetrical.",
                        allLines[i].getIntersectionWith(allLines[j]),
                        allLines[j].getIntersectionWith(allLines[i]));
            }
        }
    }
}
