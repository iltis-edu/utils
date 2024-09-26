package de.tudortmund.cs.iltis.utils;

import java.util.Arrays;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void testEscapeUnescapeCSV() {
        String nothingToEscape = "test";
        String withSimpleComma = "test,test";
        String withCommaAndQuotationMarks = "test,\"test\",test";
        String withOtherDelimiter = "test;test;test";
        String withFourQuotationMarks = " \"\"\"\"test\"\"\"\"";

        Assert.assertEquals(
                nothingToEscape, StringUtils.unescapeCSV(StringUtils.escapeCSV(nothingToEscape)));
        Assert.assertEquals(nothingToEscape, StringUtils.escapeCSV(nothingToEscape, ";"));
        Assert.assertEquals(
                withSimpleComma, StringUtils.unescapeCSV(StringUtils.escapeCSV(withSimpleComma)));
        Assert.assertEquals(
                withCommaAndQuotationMarks,
                StringUtils.unescapeCSV(StringUtils.escapeCSV(withCommaAndQuotationMarks)));
        Assert.assertEquals(
                withOtherDelimiter,
                StringUtils.unescapeCSV(StringUtils.escapeCSV(withOtherDelimiter)));
        Assert.assertEquals(withOtherDelimiter, StringUtils.escapeCSV(withOtherDelimiter));
        Assert.assertEquals(
                withOtherDelimiter,
                StringUtils.unescapeCSV(StringUtils.escapeCSV(withOtherDelimiter, ";")));
        Assert.assertEquals(
                withFourQuotationMarks,
                StringUtils.unescapeCSV(StringUtils.escapeCSV(withFourQuotationMarks)));
    }

    @Test
    public void testFormat() {
        Assert.assertEquals("Hallo, Welt!", StringUtils.format("%s, %s!", "Hallo", "Welt"));
        Assert.assertEquals("Test", StringUtils.format("%s", "Test"));
        Assert.assertEquals("%", StringUtils.format("%", "Test"));
        Assert.assertEquals("Hallo, !", StringUtils.format("%s, %s!", "Hallo"));
        Assert.assertEquals("Hallo, Welt!", StringUtils.format("%s, %s!", "Hallo", "Welt", "Test"));
        Assert.assertEquals("Mambo No. 5!", StringUtils.format("Mambo No. %s!", 5));
        Assert.assertEquals("1 2", StringUtils.format("%s %s", 1, 2));
    }

    @Test
    public void testIsBlank() {
        Assert.assertTrue(StringUtils.isBlank(""));
        Assert.assertTrue(StringUtils.isBlank("\u200F"));
        Assert.assertTrue(StringUtils.isBlank("\u00A0\n\r"));
        Assert.assertTrue(StringUtils.isBlank("\u000C\uFEFF\u2008"));
        Assert.assertFalse(StringUtils.isBlank(" a "));
        Assert.assertFalse(StringUtils.isBlank("b\n"));
        Assert.assertFalse(StringUtils.isBlank("\u3000c"));
        Assert.assertFalse(StringUtils.isBlank("\u000Bd\u205F"));
    }

    @Test
    public void testTrimBlanks() {
        Assert.assertEquals("test", StringUtils.trimBlanks("test"));
        Assert.assertEquals("test", StringUtils.trimBlanks(" \r test"));
        Assert.assertEquals("test", StringUtils.trimBlanks("test \u0009\n"));
        Assert.assertEquals("test", StringUtils.trimBlanks("\u0085test \u1680 "));
        Assert.assertEquals("te\u2002st", StringUtils.trimBlanks("\u2001te\u2002st\u2003"));
    }

    @Test
    public void testSplitOnBlanks() {
        Assert.assertEquals(Collections.emptyList(), StringUtils.splitOnBlanks(""));
        Assert.assertEquals(
                Collections.emptyList(), StringUtils.splitOnBlanks("\u000C\uFEFF \t   \u2008"));
        Assert.assertEquals(
                Arrays.asList("test", "passed"), StringUtils.splitOnBlanks("  test passed"));
        Assert.assertEquals(
                Arrays.asList("test", "passed"),
                StringUtils.splitOnBlanks("test  \t passed \u3000   "));
        Assert.assertEquals(
                Arrays.asList("this", "test", "_&@!?", "p4553D"),
                StringUtils.splitOnBlanks("this test _&@!? p4553D"));
        Assert.assertEquals(
                Arrays.asList("¶→|:)"), StringUtils.splitOnBlanks("\n\n\n \t\t\t  ¶→|:)\u0009"));
    }

    @Test
    public void testRemoveBlanks() {
        Assert.assertEquals("test", StringUtils.removeBlanks("test"));
        Assert.assertEquals("test", StringUtils.removeBlanks("\rtest"));
        Assert.assertEquals("test", StringUtils.removeBlanks("test\u200D"));
        Assert.assertEquals("test", StringUtils.removeBlanks("\u202F test\n \u2028"));
        Assert.assertEquals("test", StringUtils.removeBlanks("\r te \u2006 st \n\n\n"));
    }
}
