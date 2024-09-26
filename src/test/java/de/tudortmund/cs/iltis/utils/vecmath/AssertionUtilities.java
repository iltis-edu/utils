package de.tudortmund.cs.iltis.utils.vecmath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import org.junit.ComparisonFailure;

public class AssertionUtilities {
    public static final float PREC = 1E-5F;

    public static <V extends Vector<V, Float>> void assertAlmostEquals(
            String message, Vector<V, Float> expected, Vector<V, Float> actual) {
        assertEquals("Dimensions of vectors do not match.", expected.dim(), actual.dim());
        for (int i = 0; i < expected.dim(); i++) {
            if (Math.abs(expected.get(i) - actual.get(i)) > PREC)
                throw new ComparisonFailure(message, expected.toString(), actual.toString());
        }
    }

    public static <V extends Vector<V, Float>> void assertAlmostEquals(
            String message, Optional<Vector2F> expected, Optional<Vector2F> actual) {
        if (!expected.isPresent()) {
            assertFalse(message, actual.isPresent());
        } else {
            assertTrue(message, actual.isPresent());
            assertAlmostEquals(message, expected.get(), actual.get());
        }
    }
}
