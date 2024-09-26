package de.tudortmund.cs.iltis.utils.io.parsable;

import static de.tudortmund.cs.iltis.utils.test.AdvancedTest.assertThrows;
import static org.junit.Assert.*;

import org.junit.Test;

public class ParsableStringTest {
    public static ParsableString pstr(String input) {
        return new ParsableString(input);
    }

    @Test
    public void testRequiredOnPresent() {
        assertEquals("foo", pstr("foo").required().value());
    }

    @Test
    public void testOptionalOnPresent() {
        assertEquals("foo", pstr("foo").value());
        assertEquals("foo", pstr("foo").optional().value());
    }

    @Test
    public void testOptionalOnNull() {
        assertNull(pstr(null).optional().value());
        assertEquals("foo", pstr(null).withDefault("foo").value());
    }

    @Test
    public void testTrimByDefault() {
        assertEquals("foo", pstr(" foo  ").value());
    }

    @Test
    public void testTrimExplicitTrue() {
        assertEquals("foo", pstr(" foo  ").trim().value());
        assertEquals("foo", pstr(" foo  ").trim(true).value());
    }

    @Test
    public void testNoTrimExplicitFalse() {
        assertEquals(" foo  ", pstr(" foo  ").trim(false).value());
    }

    @Test
    public void testLengthAtLeast() {
        assertEquals("foo", pstr("foo").lengthAtLeast(3).value());
        assertThrows(ParsableInvalidValue.class, () -> pstr("foo").lengthAtLeast(4).value());
    }

    @Test
    public void testLengthAtMost() {
        assertEquals("foo", pstr("foo").lengthAtMost(3).value());
        assertThrows(ParsableInvalidValue.class, () -> pstr("foo").lengthAtMost(2).value());
    }

    @Test
    public void testLengthBetween() {
        assertEquals("foo", pstr("foo").length(3).value());
        assertEquals("foo", pstr("foo").length(2, 3).value());
        assertEquals("foo", pstr("foo").length(3, 4).value());
        assertThrows(ParsableInvalidValue.class, () -> pstr("foo").length(1, 2).value());
        assertThrows(ParsableInvalidValue.class, () -> pstr("foo").length(4, 5).value());
    }

    @Test
    public void testExceptionOnRequiredNull() {
        assertThrows(ParsableMissingRequired.class, () -> pstr(null).required().value());
    }
}
