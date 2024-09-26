package de.tudortmund.cs.iltis.utils.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

/**
 * General superclass for JUnit test classes, offering additional functionality:
 *
 * <ul>
 *   <li>assertNotEqual
 *   <li>>assertEqual (for Iterables)
 *   <li>serialization (Java/GWT)
 * </ul>
 */
public abstract class AdvancedTest {
    protected void assertNotEqual(Object expected, Object actual) {
        if (expected == null) assertFalse(actual == null);
        assertFalse(expected.equals(actual));
    }

    /**
     * Fails if and only if both iterables have a different number of elements or elements on
     * corresponding positions fail to satisfy assertEquals.
     *
     * @param expected
     * @param actual
     */
    protected void assertEqual(Iterable expected, Iterable actual) {
        Iterator itExpected = expected.iterator();
        Iterator itActual = actual.iterator();

        do {
            assertEquals(itExpected.hasNext(), itActual.hasNext());
            assertEquals(itExpected.next(), itActual.next());
        } while (itExpected.hasNext() || itActual.hasNext());
    }

    protected void assertLessThan(Comparable left, Comparable right) {
        assertTrue(left.compareTo(right) < 0);
    }

    protected void assertGreaterThan(Comparable left, Comparable right) {
        assertTrue(left.compareTo(right) < 0);
    }

    /**
     * Fails if deserialization of serialized objects does not satisfy assertEquals.
     *
     * @param object
     */
    protected void sedeserializeJava(Object object) {
        try {
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            ObjectOutputStream outObject = new ObjectOutputStream(outBytes);

            outObject.writeObject(object);
            ByteArrayInputStream inBytes = new ByteArrayInputStream(outBytes.toByteArray());
            ObjectInputStream inObject = new ObjectInputStream(inBytes);
            Object result = inObject.readObject();

            inObject.close();
            inBytes.close();
            outObject.close();
            outBytes.close();

            assertEquals(object, result);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    // GG: Currently does not work. Extend GWTTestCase???
    /*
    protected void sedeserializeGwt(Object object, Class clazz) {
    	try {
    		SerializationStreamFactory ssf = (SerializationStreamFactory) GWT.create(clazz);
    		SerializationStreamWriter ssw = ssf.createStreamWriter();
    		ssw.writeObject(object);

    		SerializationStreamReader ssr = ssf.createStreamReader(ssw.toString());
    		assertEquals(object, ssr.readObject());
    	}
    	catch(Exception e) {
    		assertTrue(false);
    	}
    }*/

    /**
     * Execute the <tt>Runnable func</tt> and succeed if this causes an instance of the
     * <tt>throwableClazz</tt> or a super class to be thrown. Otherwise, an assertion fails.
     *
     * @param throwableClazz class or super class of the expected <tt>Throwable</tt>
     * @param func the code to be executed
     */
    public static void assertThrows(Class<? extends Throwable> throwableClazz, Runnable func) {
        boolean thrown = false;
        try {
            func.run();
        } catch (Exception e) {
            if (throwableClazz.isAssignableFrom(e.getClass())) {
                thrown = true;
            }
        }
        assertTrue(thrown);
    }

    /**
     * Execute the <tt>Runnable func</tt> and succeed if this causes an instance of the
     * <tt>throwableClazz</tt> or a super class to be thrown. Otherwise, an assertion fails.
     *
     * @param throwableClazz class or super class of the expected <tt>Throwable</tt>
     * @param func the code to be executed
     */
    public static void assertThrowsVerbose(
            Class<? extends Throwable> throwableClazz, Runnable func) {
        boolean thrown = false;
        System.out.println("Expecting exception '" + throwableClazz.getCanonicalName() + "'");
        try {
            func.run();
        } catch (Exception e) {
            if (throwableClazz.isAssignableFrom(e.getClass())) {
                thrown = true;
                System.out.println("Caught expected exception: " + e.getMessage());
            } else {
                System.out.println("Caught other exception: " + e.getMessage());
            }
        }
        assertTrue(thrown);
    }
}
