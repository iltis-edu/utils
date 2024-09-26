package de.tudortmund.cs.iltis.utils.collections;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;

/** Tests the class {@code Multiset} with type parameter {@code String}. */
public class MultisetTest {
    static String a, b, c;
    static Multiset<String> ms;
    static List<String> l, la, lb, lc, laa, lab, lba, lbb, laab, laba, lbaa, labc;

    @BeforeClass
    public static void setUpBeforeClass() {
        a = "a";
        b = "b";
        c = "c";

        l = list();
        la = list(a);
        lb = list(b);
        lc = list(c);
        laa = list(a, a);
        lab = list(a, b);
        lba = list(b, a);
        lbb = list(b, b);
        laab = list(a, a, b);
        laba = list(a, b, a);
        lbaa = list(b, a, a);
        labc = list(a, b, c);
    }

    @Before
    public void setUp() {
        // nothing to do
    }

    @Test
    public void testAddAndCount() {
        ms = new Multiset<>();

        assertTrue(ms.add(a));
        assertEquals(1, ms.count(a));

        assertTrue(ms.add(b, 2));
        assertEquals(2, ms.count(b));

        assertTrue(ms.add(a, 2));
        assertEquals(3, ms.count(a));

        assertTrue(ms.add(b));
        assertEquals(3, ms.count(b));

        assertEquals(0, ms.count(c));

        assertFalse(ms.add(c, -1));
        assertFalse(ms.add(c, 0));
        assertEquals(0, ms.count(c));
    }

    @Test
    public void testRemoveAndReduce() {
        ms = multiset();
        assertFalse(ms.remove(a));
        assertEquals(0, ms.reduce(a, 1));

        ms = multiset(a, a, b);
        assertTrue(ms.remove(a));
        assertEquals(0, ms.count(a));
        assertTrue(ms.remove(b));
        assertEquals(0, ms.count(b));
        assertFalse(ms.remove(c));
        assertEquals(0, ms.count(c));

        ms = multiset(a, a, b);
        assertEquals(1, ms.reduce(a, 1));
        assertEquals(1, ms.count(a));
        assertEquals(1, ms.reduce(b, 1));
        assertEquals(0, ms.count(b));
        assertEquals(0, ms.reduce(c, 1));
        assertEquals(0, ms.count(c));

        ms = multiset(a);
        assertEquals(1, ms.reduce(a, 2));
        assertEquals(0, ms.count(a));

        ms = multiset(a);
        assertEquals(0, ms.reduce(a, -1));
        assertEquals(0, ms.reduce(a, 0));
        assertEquals(1, ms.count(a));
        assertEquals(0, ms.reduce(b, -1));
        assertEquals(0, ms.reduce(b, 0));
        assertEquals(0, ms.count(b));
    }

    @Test
    public void testTotalCount() {
        assertEquals(0, multiset().totalCount());
        assertEquals(1, multiset(a).totalCount());
        assertEquals(2, multiset(a, a).totalCount());
        assertEquals(2, multiset(a, b).totalCount());
        assertEquals(3, multiset(a, b, b).totalCount());
        assertEquals(4, multiset(a, a, b, b).totalCount());
    }

    @Test
    public void testEquals() {
        assertMultisetEquals(ms, ms, true);

        assertMultisetEquals(null, null, true);
        assertMultisetEquals(multiset(), multiset(), true);
        assertMultisetEquals(multiset(a), multiset(a), true);
        assertMultisetEquals(multiset(a, a), multiset(a, a), true);
        assertMultisetEquals(multiset(a, b), multiset(b, a), true);
        assertMultisetEquals(multiset(a, a, b), multiset(a, a, b), true);
        assertMultisetEquals(multiset(a, a, b), multiset(a, b, a), true);
        assertMultisetEquals(multiset(a, a, b), multiset(b, a, a), true);
        assertMultisetEquals(multiset(a, b, b), multiset(a, b, b), true);
        assertMultisetEquals(multiset(a, b, b), multiset(b, a, b), true);
        assertMultisetEquals(multiset(a, b, b), multiset(b, b, a), true);

        assertMultisetEquals(multiset(), null, false);
        assertMultisetEquals(multiset(a), multiset(), false);
        assertMultisetEquals(multiset(a), multiset(b), false);
        assertMultisetEquals(multiset(a, a), multiset(a), false);
        assertMultisetEquals(multiset(a, a), multiset(b), false);
        assertMultisetEquals(multiset(a, b), multiset(b), false);
        assertMultisetEquals(multiset(b, a), multiset(b), false);
        assertMultisetEquals(multiset(a, a), multiset(a, b), false);
        assertMultisetEquals(multiset(a, a), multiset(b, a), false);
        assertMultisetEquals(multiset(a, a, b), multiset(a, b), false);
        assertMultisetEquals(multiset(a, a, b), multiset(b, a), false);
        assertMultisetEquals(multiset(a, b, b), multiset(a, b), false);
        assertMultisetEquals(multiset(a, b, b), multiset(b, a), false);
        assertMultisetEquals(multiset(a, a, b), multiset(a, a), false);
        assertMultisetEquals(multiset(a, b, a), multiset(a, a), false);
        assertMultisetEquals(multiset(b, a, a), multiset(a, a), false);
    }

    @Test
    public void testClone() {
        ms = multiset();
        assertClone(ms, ms.clone());

        ms = multiset(a);
        assertClone(ms, ms.clone());

        ms = multiset(a, a);
        assertClone(ms, ms.clone());

        ms = multiset(a, b);
        assertClone(ms, ms.clone());

        ms = multiset(a, a, b);
        assertClone(ms, ms.clone());
    }

    @Test
    public void testContainsAll() {
        // scheme: assertContainsAll(Multiset, Collection, testContainsAll);

        assertContainsAll(multiset(), multiset(), true);
        assertContainsAll(multiset(a), multiset(), true);
        assertContainsAll(multiset(a, a), multiset(), true);
        assertContainsAll(multiset(a, b), multiset(), true);

        assertContainsAll(multiset(a), multiset(a), true);
        assertContainsAll(multiset(a, a), multiset(a), true);
        assertContainsAll(multiset(a, b), multiset(a), true);
        assertContainsAll(multiset(a, a, b), multiset(a), true);

        assertContainsAll(multiset(a, b), multiset(b), true);
        assertContainsAll(multiset(a, a, b), multiset(b), true);
        assertContainsAll(multiset(a, b, b), multiset(b), true);

        assertContainsAll(multiset(a, b), multiset(a, b), true);
        assertContainsAll(multiset(b, a), multiset(a, b), true);
        assertContainsAll(multiset(a, a, b), multiset(a, b), true);
        assertContainsAll(multiset(a, b, a), multiset(a, b), true);
        assertContainsAll(multiset(b, a, a), multiset(a, b), true);
        assertContainsAll(multiset(a, b, b), multiset(a, b), true);
        assertContainsAll(multiset(b, a, b), multiset(a, b), true);
        assertContainsAll(multiset(b, b, a), multiset(a, b), true);

        assertContainsAll(multiset(), multiset(a), false);

        assertContainsAll(multiset(a), multiset(b), false);
        assertContainsAll(multiset(a), multiset(a, b), false);

        assertContainsAll(multiset(a, b), multiset(c), false);
        assertContainsAll(multiset(a, b), multiset(a, c), false);
        assertContainsAll(multiset(a, b), multiset(b, c), false);
        assertContainsAll(multiset(a, b), multiset(a, b, c), false);

        assertContainsAll(multiset(a, a, b), multiset(a, b, c), false);
    }

    @Test
    public void testAddAll() {
        // scheme: assertAddAll(original Multiset, Collection, expected Multiset, testModified);

        assertAddAll(multiset(), l, multiset(), false);
        assertAddAll(multiset(a), l, multiset(a), false);

        assertAddAll(multiset(), la, multiset(a), true);
        assertAddAll(multiset(), laa, multiset(a, a), true);
        assertAddAll(multiset(), lab, multiset(a, b), true);
        assertAddAll(multiset(), laab, multiset(a, a, b), true);
        assertAddAll(multiset(), laba, multiset(a, a, b), true);
        assertAddAll(multiset(), lbaa, multiset(a, a, b), true);

        assertAddAll(multiset(a), la, multiset(a, a), true);
        assertAddAll(multiset(a), lb, multiset(a, b), true);
        assertAddAll(multiset(a), laa, multiset(a, a, a), true);

        assertAddAll(multiset(a, b), lb, multiset(a, b, b), true);
        assertAddAll(multiset(a, b), lab, multiset(a, a, b, b), true);
        assertAddAll(multiset(a, b), lba, multiset(a, a, b, b), true);
    }

    @Test
    public void testRetainAll() {
        // scheme: assertRetainAll(original Multiset, Collection, expected Multiset, testModified);

        assertRetainAll(multiset(), l, multiset(), false);
        assertRetainAll(multiset(), la, multiset(), false);

        assertRetainAll(multiset(a), l, multiset(), true);
        assertRetainAll(multiset(a), la, multiset(a), false);
        assertRetainAll(multiset(a), lb, multiset(), true);
        assertRetainAll(multiset(a), laa, multiset(a), false);
        assertRetainAll(multiset(a), lab, multiset(a), false);
        assertRetainAll(multiset(a), lba, multiset(a), false);

        assertRetainAll(multiset(a, a), l, multiset(), true);
        assertRetainAll(multiset(a, a), la, multiset(a, a), false);
        assertRetainAll(multiset(a, a), lb, multiset(), true);
        assertRetainAll(multiset(a, a), laa, multiset(a, a), false);
        assertRetainAll(multiset(a, a), lab, multiset(a, a), false);
        assertRetainAll(multiset(a, a), lba, multiset(a, a), false);

        assertRetainAll(multiset(a, b), l, multiset(), true);
        assertRetainAll(multiset(a, b), la, multiset(a), true);
        assertRetainAll(multiset(a, b), lb, multiset(b), true);
        assertRetainAll(multiset(a, b), laa, multiset(a), true);
        assertRetainAll(multiset(a, b), lab, multiset(a, b), false);
        assertRetainAll(multiset(a, b), lba, multiset(a, b), false);
        assertRetainAll(multiset(a, b), lbb, multiset(b), true);
        assertRetainAll(multiset(a, b), laab, multiset(a, b), false);
        assertRetainAll(multiset(a, b), laba, multiset(a, b), false);

        assertRetainAll(multiset(a, a, b), la, multiset(a, a), true);
        assertRetainAll(multiset(a, a, b), lb, multiset(b), true);
        assertRetainAll(multiset(a, a, b), laa, multiset(a, a), true);
        assertRetainAll(multiset(a, a, b), lab, multiset(a, a, b), false);
        assertRetainAll(multiset(a, a, b), lba, multiset(a, a, b), false);
        assertRetainAll(multiset(a, a, b), lbb, multiset(b), true);

        assertRetainAll(multiset(a, b, b), lab, multiset(a, b, b), false);
        assertRetainAll(multiset(a, b, b), lba, multiset(a, b, b), false);
    }

    @Test
    public void testRemoveAll() {
        // scheme: assertRemoveAll(original Multiset, Collection, expected Multiset, testModified);

        assertRemoveAll(multiset(), l, multiset(), false);
        assertRemoveAll(multiset(), la, multiset(), false);

        assertRemoveAll(multiset(a), l, multiset(a), false);
        assertRemoveAll(multiset(a), la, multiset(), true);
        assertRemoveAll(multiset(a), lb, multiset(a), false);
        assertRemoveAll(multiset(a), laa, multiset(), true);
        assertRemoveAll(multiset(a), lab, multiset(), true);
        assertRemoveAll(multiset(a), lba, multiset(), true);

        assertRemoveAll(multiset(a, a), l, multiset(a, a), false);
        assertRemoveAll(multiset(a, a), la, multiset(), true);
        assertRemoveAll(multiset(a, a), lb, multiset(a, a), false);
        assertRemoveAll(multiset(a, a), laa, multiset(), true);
        assertRemoveAll(multiset(a, a), lab, multiset(), true);
        assertRemoveAll(multiset(a, a), lba, multiset(), true);

        assertRemoveAll(multiset(a, b), l, multiset(a, b), false);
        assertRemoveAll(multiset(a, b), la, multiset(b), true);
        assertRemoveAll(multiset(a, b), lb, multiset(a), true);
        assertRemoveAll(multiset(a, b), lc, multiset(a, b), false);
        assertRemoveAll(multiset(a, b), laa, multiset(b), true);
        assertRemoveAll(multiset(a, b), lab, multiset(), true);
        assertRemoveAll(multiset(a, b), lba, multiset(), true);
        assertRemoveAll(multiset(a, b), lbb, multiset(a), true);
        assertRemoveAll(multiset(a, b), labc, multiset(), true);

        assertRemoveAll(multiset(a, a, b), l, multiset(a, a, b), false);
        assertRemoveAll(multiset(a, a, b), la, multiset(b), true);
        assertRemoveAll(multiset(a, a, b), lb, multiset(a, a), true);
        assertRemoveAll(multiset(a, a, b), laa, multiset(b), true);
        assertRemoveAll(multiset(a, a, b), lab, multiset(), true);
        assertRemoveAll(multiset(a, a, b), lba, multiset(), true);
        assertRemoveAll(multiset(a, a, b), lbb, multiset(a, a), true);
        assertRemoveAll(multiset(a, a, b), labc, multiset(), true);

        assertRemoveAll(multiset(a, b, b), l, multiset(a, b, b), false);
        assertRemoveAll(multiset(a, b, b), la, multiset(b, b), true);
        assertRemoveAll(multiset(a, b, b), lb, multiset(a), true);
        assertRemoveAll(multiset(a, b, b), laa, multiset(b, b), true);
        assertRemoveAll(multiset(a, b, b), lab, multiset(), true);
        assertRemoveAll(multiset(a, b, b), lba, multiset(), true);
        assertRemoveAll(multiset(a, b, b), lbb, multiset(a), true);
        assertRemoveAll(multiset(a, b, b), labc, multiset(), true);
    }

    @Test
    public void testToArray() {
        // scheme: assertToArray(original Multiset, expected Array);

        assertToArray(multiset(), array());
        assertToArray(multiset(a), array(a));
        assertToArray(multiset(a, a), array(a, a));
        assertToArray(multiset(a, b), array(a, b));
        assertToArray(multiset(b, a), array(a, b));
        assertToArray(multiset(a, a, b), array(a, a, b));
        assertToArray(multiset(a, b, a), array(a, a, b));
        assertToArray(multiset(b, a, a), array(a, a, b));
    }

    @Test
    public void testIterator() {
        ms = multiset(a, a, c, c, b, c);
        Iterator<String> it = ms.iterator();

        assertIteratorStep(it, a, false);
        assertIteratorStep(it, b, false);
        assertIteratorStep(it, c, true);
        assertFalse(it.hasNext());

        assertMultisetEquals(multiset(a, a, b), ms, true);
    }

    @Test
    public void testFullIterator() {
        ms = multiset(a, a, c, c, b, c);
        Iterator<String> fit = ms.fullIterator();

        assertIteratorStep(fit, a, true);
        assertIteratorStep(fit, a, true);
        assertIteratorStep(fit, b, false);
        assertIteratorStep(fit, c, true);
        assertIteratorStep(fit, c, false);
        assertIteratorStep(fit, c, true);
        assertFalse(fit.hasNext());

        assertMultisetEquals(multiset(b, c), ms, true);
    }

    // HELPERS ................................................................

    public static Multiset<String> multiset(String... strings) {
        return new Multiset<>(list(strings));
    }

    public static List<String> list(String... strings) {
        return new ArrayList<>(Arrays.asList(strings));
    }

    public static String[] array(String... strings) {
        return strings;
    }

    public void assertMultisetEquals(
            Multiset<String> expected, Multiset<String> actual, boolean testEquals) {
        boolean equals =
                (expected == null && actual == null)
                        || (Objects.equals(expected, actual) && actual.equals(expected));

        if (equals != testEquals) {
            throw new AssertionError();
        }
    }

    public void assertClone(Multiset<String> left, Multiset<String> right) {
        assertNotSame(left, right);
        assertMultisetEquals(left, right, true);
    }

    public void assertContainsAll(Multiset<String> ms, Collection<?> co, boolean testContainsAll) {
        if (testContainsAll) {
            assertTrue(ms.containsAll(co));
        } else {
            assertFalse(ms.containsAll(co));
        }
    }

    public void assertAddAll(
            Multiset<String> original,
            Collection<? extends String> co,
            Multiset<String> expected,
            boolean testModified) {
        if (testModified) {
            assertTrue(original.addAll(co));
        } else {
            assertFalse(original.addAll(co));
        }
        assertMultisetEquals(original, expected, true);
    }

    public void assertRetainAll(
            Multiset<String> original,
            Collection<?> co,
            Multiset<String> expected,
            boolean testModified) {
        if (testModified) {
            assertTrue(original.retainAll(co));
        } else {
            assertFalse(original.retainAll(co));
        }
        assertMultisetEquals(original, expected, true);
    }

    public void assertRemoveAll(
            Multiset<String> original,
            Collection<?> co,
            Multiset<String> expected,
            boolean testModified) {
        if (testModified) {
            assertTrue(original.removeAll(co));
        } else {
            assertFalse(original.removeAll(co));
        }
        assertMultisetEquals(original, expected, true);
    }

    public void assertToArray(Multiset<String> original, Object[] expected) {
        Object[] array = original.toArray();
        for (int i = 0; i < array.length; i++) {
            if (!(array[i] instanceof String && expected[i] instanceof String)) {
                throw new AssertionError();
            }
            String a = (String) array[i];
            String e = (String) expected[i];
            assertEquals(a, e);
        }
    }

    public void assertIteratorStep(
            Iterator<String> it, String nextElement, boolean removeLastElement) {
        assertTrue(it.hasNext());
        assertEquals(nextElement, it.next());
        if (removeLastElement) {
            it.remove();
        }
    }
}
