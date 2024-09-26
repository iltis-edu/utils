package de.tudortmund.cs.iltis.utils.vecmath;

import static org.junit.Assert.*;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class LineSegment2FTest {
    LineSegment2F seg1, seg2, seg3;
    LineSegment2F[] allSegments;

    @Before
    public void prepare() {
        seg1 = new LineSegment2F(new Vector2F(0, 0), new Vector2F(0, 10));
        seg2 = new LineSegment2F(new Vector2F(-10, 5), new Vector2F(5, 5));
        seg3 = new LineSegment2F(new Vector2F(5, 5), new Vector2F(4, 4));
        allSegments = new LineSegment2F[] {seg1, seg2, seg3};
    }

    @Test
    public void lengths() {
        assertEquals(10F, (float) seg1.getLength(), AssertionUtilities.PREC);
        assertEquals(15F, (float) seg2.getLength(), AssertionUtilities.PREC);
        assertEquals(Math.sqrt(2), (float) seg3.getLength(), AssertionUtilities.PREC);
    }

    @Test
    public void intersect12Correct() {
        Optional<Vector2F> intersection = seg1.getIntersectionWith(seg2);
        assertTrue("Intersection was not detected! (False negative!)", intersection.isPresent());
        AssertionUtilities.assertAlmostEquals("", new Vector2F(0, 5), intersection.get());
    }

    @Test
    public void intersect13Correct() {
        Optional<Vector2F> intersection = seg1.getIntersectionWith(seg3);
        assertFalse(intersection.isPresent());
    }

    @Test
    public void intersect23Correct() {
        Optional<Vector2F> intersection = seg2.getIntersectionWith(seg3);
        assertTrue("Intersection was not detected! (False negative!)", intersection.isPresent());
        AssertionUtilities.assertAlmostEquals("", new Vector2F(5, 5), intersection.get());
    }

    @Test
    public void intersectSymmetrical() {
        for (int i = 0; i < allSegments.length; i++) {
            for (int j = i + 1; j < allSegments.length; j++) {
                AssertionUtilities.assertAlmostEquals(
                        "Intersection between "
                                + (i + 1)
                                + " and "
                                + (j + 1)
                                + " is not symmetrical.",
                        allSegments[i].getIntersectionWith(allSegments[j]),
                        allSegments[j].getIntersectionWith(allSegments[i]));
            }
        }
    }
}
