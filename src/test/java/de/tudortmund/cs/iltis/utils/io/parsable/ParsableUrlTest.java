package de.tudortmund.cs.iltis.utils.io.parsable;

import static org.junit.Assert.assertEquals;

import de.tudortmund.cs.iltis.utils.test.AdvancedTest;
import java.net.URL;
import org.junit.Test;

public class ParsableUrlTest extends AdvancedTest {
    public static ParsableUrl purl(String input) {
        return new ParsableUrl(input);
    }

    @Test
    public void testProperUrl() throws Throwable {
        assertEquals(
                new URL("https://iltis.cs.tu-dortmund.de"),
                purl("https://iltis.cs.tu-dortmund.de").value());
    }

    @Test(expected = ParsableInvalidValue.class)
    public void testInvalidUrl() throws Throwable {
        purl("BasilFawlty").value();
    }

    @Test
    public void testUntrimmedInput() throws Throwable {
        assertEquals(
                new URL("https://iltis.cs.tu-dortmund.de"),
                purl(" " + "https://iltis.cs.tu-dortmund.de").value());
        assertEquals(
                new URL("https://iltis.cs.tu-dortmund.de"),
                purl(" " + "https://iltis.cs.tu-dortmund.de" + " ").value());
        assertEquals(
                new URL("https://iltis.cs.tu-dortmund.de"),
                purl("https://iltis.cs.tu-dortmund.de" + " ").value());

        assertEquals(
                new URL("https://iltis.cs.tu-dortmund.de").toString(),
                purl(" " + "https://iltis.cs.tu-dortmund.de").value().toString());
        assertEquals(
                new URL("https://iltis.cs.tu-dortmund.de").toString(),
                purl(" " + "https://iltis.cs.tu-dortmund.de" + " ").value().toString());
        assertEquals(
                new URL("https://iltis.cs.tu-dortmund.de").toString(),
                purl("https://iltis.cs.tu-dortmund.de" + " ").value().toString());
    }
}
