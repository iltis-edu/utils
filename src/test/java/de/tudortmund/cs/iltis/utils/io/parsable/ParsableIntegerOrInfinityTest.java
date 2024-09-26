package de.tudortmund.cs.iltis.utils.io.parsable;

import static de.tudortmund.cs.iltis.utils.test.AdvancedTest.assertThrows;
import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class ParsableIntegerOrInfinityTest {
    private static final int INF = Integer.MAX_VALUE;
    private static final String INF_STR1 = "infinity";
    private static final String INF_STR2 = "infinite";
    private static final String INF_STR3 = "inf";
    private static final String INF_STR4 = "Inf";
    private static final String INF_STR5 = "  infinity ";
    private static final String INF_STR6 = "INF";
    private static final String INF_STR7 = "infty";
    private static final String INF_STR8 = "Infinity";

    private static final int A = 10;
    private static final String A_STR = "10";

    private static final int B = 10;

    private static final int A_BIT = 1;
    private static final int MORE = 2;

    private static final List<String> customInfinityKeywords =
            new ArrayList<>(Collections.singletonList(INF_STR7));

    public static ParsableIntegerOrInfinity pioi(String input) {
        return new ParsableIntegerOrInfinity(input);
    }

    public static ParsableIntegerOrInfinity pioi(String input, Collection<String> keywords) {
        return new ParsableIntegerOrInfinity(input, keywords);
    }

    @Test
    public void testAllowedDefaultInfinityStrings() {
        assertEquals(INF, (int) pioi(INF_STR1).value());
        assertEquals(INF, (int) pioi(INF_STR2).value());
        assertEquals(INF, (int) pioi(INF_STR3).value());
        assertEquals(INF, (int) pioi(INF_STR4).value());
        assertEquals(INF, (int) pioi(INF_STR5).value());
        assertEquals(StringUtils.trimBlanks(INF_STR5), pioi(INF_STR5).getSource());
    }

    @Test
    public void testExceptionAlternativeVersionsOfAllowedDefaultInfinityString() {
        assertThrows(ParsableInvalidValue.class, () -> pioi(INF_STR6).value());
        assertThrows(ParsableInvalidValue.class, () -> pioi(INF_STR7).value());
        assertThrows(ParsableInvalidValue.class, () -> pioi(INF_STR8).value());
    }

    @Test
    public void testCustomInfinityStrings() {
        assertEquals(INF, (int) pioi(INF_STR7, customInfinityKeywords).value());

        assertThrows(
                ParsableInvalidValue.class, () -> pioi(INF_STR1, customInfinityKeywords).value());
    }

    @Test
    public void testBasicParsableIntegerFunctionality() { // see ParsableIntegerTest
        assertEquals(A, (int) pioi(A_STR).value());
        assertEquals(A, (int) pioi(A_STR).required().value());
        assertEquals(A, (int) pioi(A_STR).optional().value());
        assertEquals(A, (int) pioi(A_STR).withDefault(B).optional().value());
        assertEquals(A, (int) pioi(A_STR).withDefault(B).value());

        assertEquals(B, (int) pioi(null).withDefault(B).value());
        assertNull(pioi(null).optional().value());

        assertThrows(ParsableMissingRequired.class, () -> pioi(null).required().value());
        assertThrows(ParsableInvalidValue.class, () -> pioi("xyz").required().value());

        assertThrows(ParsableInvalidValue.class, () -> pioi(A_STR).atLeast(A + A_BIT).value());
        assertThrows(ParsableInvalidValue.class, () -> pioi(A_STR).atMost(A - A_BIT).value());
        assertThrows(
                ParsableInvalidValue.class, () -> pioi(A_STR).between(A - MORE, A - A_BIT).value());
        assertThrows(
                ParsableInvalidValue.class, () -> pioi(A_STR).between(A + A_BIT, A + MORE).value());

        assertEquals(A, (int) pioi(" " + A_STR + " ").value());
        assertEquals(A, (int) pioi(A_STR + " ").value());
        assertEquals(A, (int) pioi(" " + A_STR + " ").value());
    }
}
