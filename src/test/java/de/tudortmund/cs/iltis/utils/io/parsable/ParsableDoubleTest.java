package de.tudortmund.cs.iltis.utils.io.parsable;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.test.AdvancedTest;
import org.junit.Test;

public class ParsableDoubleTest extends AdvancedTest {
    private static final double A = 10.5432;
    private static final String A_STR = "10.5432";

    private static final double B = 20.9876;
    private static final String B_STR = "20.9876";

    private static final double A_BIT = 0.1;
    private static final double MORE = 0.2;

    public static ParsableDouble pdbl(String input) {
        return new ParsableDouble(input);
    }

    @Test
    public void testRequiredOnPresent() {
        _assertEquals(10, (double) pdbl("10").required().value());
        _assertEquals(A, (double) pdbl(A_STR).required().value());
    }

    @Test
    public void testOptionalOnPresent() {
        _assertEquals(A, (double) pdbl(A_STR).value());
        _assertEquals(A, (double) pdbl(A_STR).optional().value());
        _assertEquals(A, (double) pdbl(A_STR).withDefault(B).optional().value());
    }

    @Test
    public void testOptionalOnNull() {
        assertNull(pdbl(null).optional().value());
        _assertEquals(B, (double) pdbl(null).withDefault(B).value());
    }

    @Test
    public void testExceptionOnRequiredNull() {
        assertThrows(ParsableMissingRequired.class, () -> pdbl(null).required().value());
        _assertEquals(B, (double) pdbl(null).withDefault(B).value());
    }

    @Test
    public void testRequiredWithDefault() {
        _assertEquals(B, (double) pdbl(null).withDefault(B).value());
        _assertEquals(A, (double) pdbl(A_STR).withDefault(B).value());
    }

    @Test
    public void testExceptionNonNumeric() {
        assertThrows(ParsableInvalidValue.class, () -> pdbl("xyz").required().value());
    }

    @Test
    public void testInRange() {
        assertThrows(ParsableInvalidValue.class, () -> pdbl(A_STR).atLeast(A + A_BIT).value());
        assertThrows(ParsableInvalidValue.class, () -> pdbl(A_STR).atMost(A - A_BIT).value());
        assertThrows(
                ParsableInvalidValue.class, () -> pdbl(A_STR).between(A - MORE, A - A_BIT).value());
        assertThrows(
                ParsableInvalidValue.class, () -> pdbl(A_STR).between(A + A_BIT, A + MORE).value());
    }

    @Test
    public void testUntrimmedInput() {
        _assertEquals(A, pdbl(" " + A_STR).value());
        _assertEquals(A, pdbl(A_STR + " ").value());
        _assertEquals(A, pdbl(" " + A_STR + " ").value());
    }

    private static final double EQUALS_DELTA = 0.0001;

    private void _assertEquals(double expected, double actual) {
        assertEquals(expected, actual, EQUALS_DELTA);
    }
}
