package de.tudortmund.cs.iltis.utils.io.parsable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.tudortmund.cs.iltis.utils.io.parsable.helpers.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public class XmlParsableReaderTest {
    @Test
    public void readSimpleComplete() {
        assertParseResultTestObject(new TestObject(11, "anton"), "io/parsable/simple-complete.xml");
    }

    @Test
    public void readSimpleTestObjectMissingOptionalNumber() {
        assertParseResultTestObject(
                new TestObject(123, "anton"), "io/parsable/simple-testObject-missingNumber.xml");
    }

    @Test
    public void readSimpleTestObjectOptionalNumberNotNumeric() {
        expectException(
                ParsableInvalidValue.class, "io/parsable/simple-testObject-numberNotNumeric.xml");
    }

    @Test
    public void readSimpleTestObjectMissingRequiredName() {
        expectException(
                ParsableMissingRequired.class, "io/parsable/simple-TestObject-missingName.xml");
    }

    @Test
    public void readSimpleMissingRequiredElement() {
        expectException(ParsableMissingRequired.class, "io/parsable/simple-missing-required.xml");
    }

    @Test
    public void readReferencesComplete() {
        List<TestReference> refs =
                Stream.of(new TestReference("alpha", "blue"), new TestReference("beta", null))
                        .collect(Collectors.toList());
        assertParseResultTestObject(
                new TestObject(11, "anton", refs), "io/parsable/ref-complete.xml");
    }

    @Test
    public void readReferencesMissingRequiredName() {
        expectException(ParsableInvalidValue.class, "io/parsable/ref-missing-name.xml");
    }

    @Test
    public void readElementsComplete() {
        TestObject result = readTestObjectFromFile("io/parsable/elems-complete.xml");
        TestObject expected = new TestObject(11, "anton");
        expected.setList(
                new ListOfRequiredElements(
                        new RequiredElement("here1", "there1"),
                        new RequiredElement("here2", "there2")));
        assertEquals(expected, result);
    }

    @Test
    public void readElementsWithMissingRequiredInElement() {
        expectException(
                ParsableMissingRequired.class, "io/parsable/elems-missingRequiredInElement.xml");
    }

    @Test
    public void readInvalidSpecXmlAttributeRequired() {
        expectException(
                ParsableInvalidSpecification.class,
                "io/parsable/onlyAttributeNumber.xml",
                OnlyAttributeNumber.class,
                InvalidSpecXmlAttributeRequiredSpec.class);
    }

    @Test
    public void readInvalidSpecXmlElementRequired() {
        expectException(
                ParsableInvalidSpecification.class,
                "io/parsable/onlyElementNumber.xml",
                OnlyAttributeNumber.class,
                InvalidSpecXmlElementRequiredSpec.class);
    }

    @Test
    public void readValidSpecXmlElementRequired() {
        OnlyAttributeNumber result =
                new XmlParsableReader<OnlyAttributeNumber, ValidSpecXmlElementRequiredSpec, Object>(
                                ValidSpecXmlElementRequiredSpec.class)
                        .read("io/parsable/onlyElementNumber.xml");
        assertEquals(new OnlyAttributeNumber(11), result);
    }

    // ===============================================================
    // HELPERS
    // ===============================================================
    public static void assertParseResultTestObject(TestObject expected, String path) {
        TestObject result = readTestObjectFromFile(path);
        assertEquals(expected, result);
    }

    private static <R, S extends Parsable<R, Object>, Object> void expectException(
            Class<?> clazz, String path, Class<R> resultClazz, Class<S> specClazz) {
        boolean thrown = false;
        System.out.println("Expect exception for '" + path + "'");
        try {
            new XmlParsableReader<R, S, Object>(specClazz).read(path);
        } catch (Exception e) {
            if (clazz.isAssignableFrom(e.getClass())) {
                thrown = true;
                System.out.println(e.toString());
            } else {
                System.err.println("UNEXPECTED exception: " + e);
            }
        }
        assertTrue(thrown);
    }

    private static void expectException(Class<?> clazz, String path) {
        expectException(clazz, path, TestObject.class, TestObjectSpecification.class);
    }

    private static TestObject readTestObjectFromFile(String path) {
        return new XmlParsableReader<TestObject, TestObjectSpecification, Object>(
                        TestObjectSpecification.class)
                .read(path);
    }
}
