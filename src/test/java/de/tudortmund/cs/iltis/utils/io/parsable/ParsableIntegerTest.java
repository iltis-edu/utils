package de.tudortmund.cs.iltis.utils.io.parsable;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.test.AdvancedTest;
import org.junit.Test;

public class ParsableIntegerTest extends AdvancedTest {
    private static final int A = 10;
    private static final String A_STR = "10";

    private static final int B = 10;
    private static final String B_STR = "20";

    private static final int A_BIT = 1;
    private static final int MORE = 2;

    public static ParsableInteger pint(String input) {
        return new ParsableInteger(input);
    }

    @Test
    public void testRequiredOnPresent() {
        assertEquals(A, (int) pint(A_STR).required().value());
    }

    @Test
    public void testOptionalOnPresent() {
        assertEquals(A, (int) pint(A_STR).value());
        assertEquals(A, (int) pint(A_STR).optional().value());
        assertEquals(A, (int) pint(A_STR).withDefault(B).optional().value());
    }

    @Test
    public void testOptionalOnNull() {
        assertNull(pint(null).optional().value());
        assertEquals(B, (int) pint(null).withDefault(B).value());
    }

    @Test
    public void testExceptionOnRequiredNull() {
        assertThrows(ParsableMissingRequired.class, () -> pint(null).required().value());
        assertEquals(B, (int) pint(null).withDefault(B).value());
    }

    @Test
    public void testRequiredWithDefault() {
        assertEquals(B, (int) pint(null).withDefault(B).value());
        assertEquals(A, (int) pint(A_STR).withDefault(B).value());
    }

    @Test
    public void testExceptionNonNumeric() {
        assertThrows(ParsableInvalidValue.class, () -> pint("xyz").required().value());
    }

    @Test
    public void testInRange() {
        assertThrows(ParsableInvalidValue.class, () -> pint(A_STR).atLeast(A + A_BIT).value());
        assertThrows(ParsableInvalidValue.class, () -> pint(A_STR).atMost(A - A_BIT).value());
        assertThrows(
                ParsableInvalidValue.class, () -> pint(A_STR).between(A - MORE, A - A_BIT).value());
        assertThrows(
                ParsableInvalidValue.class, () -> pint(A_STR).between(A + A_BIT, A + MORE).value());
    }

    @Test
    public void testUntrimmedInput() {
        assertEquals(A, (int) pint(" " + A_STR).value());
        assertEquals(A, (int) pint(A_STR + " ").value());
        assertEquals(A, (int) pint(" " + A_STR + " ").value());
    }
}
