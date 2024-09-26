package de.tudortmund.cs.iltis.utils.io.parsable;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.test.AdvancedTest;
import org.junit.Test;

/** Test common functionality of {@code ParsableSource}. */
public class ParsableSourceTest extends AdvancedTest {
    private static final int A = 10;
    private static final String A_STR = "10";

    public static ParsableSource ps(String input) {
        // use a `ParsableInteger` as a concrete implementation of
        // `ParsableSource`. Other choices would be as good as this.
        return new ParsableInteger(input);
    }

    @Test
    public void testIsPresent() {
        assertTrue(ps(A_STR).isPresent());
        assertFalse(ps(null).isPresent());
    }

    @Test
    public void testIsMissing() {
        assertFalse(ps(A_STR).isMissing());
        assertTrue(ps(null).isMissing());
    }

    @Test
    public void testIsRequired() {
        assertEquals(A, ps(A_STR).required(true).value());
        assertEquals(A, ps(A_STR).required(false).value());
        assertThrows(ParsableMissingRequired.class, () -> ps(null).required(true).value());
        assertNull(ps(null).required(false).value());
    }

    @Test
    public void testIsOptional() {
        assertEquals(A, ps(A_STR).optional(true).value());
        assertEquals(A, ps(A_STR).optional(false).value());
        assertNull(ps(null).optional(true).value());
        assertNull(ps(null).optional(false).value());
    }
}
