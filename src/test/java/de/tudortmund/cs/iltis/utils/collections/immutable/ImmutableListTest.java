package de.tudortmund.cs.iltis.utils.collections.immutable;

import static org.junit.Assert.*;

import java.util.function.Function;
import java.util.function.Predicate;
import org.junit.Test;

public class ImmutableListTest {

    ImmutableList<Boolean> emptyList = new ImmutableList<>();
    ImmutableList<Integer> primes = new ImmutableList<>(2, 3, 5, 7, 11, 13);
    ImmutableList<Number> ints =
            new ImmutableList<>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

    Function<Number, Integer> collatz =
            n -> n.intValue() % 2 == 0 ? n.intValue() / 2 : 3 * n.intValue() + 1;
    Predicate<Number> isSquare = n -> new ImmutableSet<>(0, 1, 2, 4, 9, 16).contains(n.intValue());

    @Test
    public void testSize() {
        assertEquals(0, emptyList.size());
        assertEquals(6, primes.size());
        assertEquals(17, ints.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(emptyList.isEmpty());
        assertFalse(primes.isEmpty());
        assertFalse(ints.isEmpty());
    }

    @Test
    public void testContains() {
        assertTrue(primes.contains(3));
        assertTrue(primes.contains(7));
        assertFalse(emptyList.contains(false));
        assertFalse(primes.contains(4));
        assertFalse(ints.contains(-99));
    }

    @Test
    public void testContainsAll() {
        assertTrue(primes.containsAll(new ImmutableList<Integer>().toUnmodifiableList()));
        assertTrue(ints.containsAll(primes.toUnmodifiableList()));
    }

    @Test
    public void testGet() {
        assertEquals(Integer.valueOf(7), primes.get(3));
        assertEquals(15, ints.get(15));
    }

    @Test
    public void testAppend() {
        ImmutableList<Number> appended = ints.append(primes);
        assertEquals(23, appended.size());
        assertEquals(11, appended.get(21));
    }

    @Test
    public void testPrepend() {
        ImmutableList<Number> prepended = ints.prepend(primes);
        assertEquals(23, prepended.size());
        assertEquals(15, prepended.get(21));
    }

    @Test
    public void testTake() {
        assertEquals(new ImmutableList<>(), primes.take(0));
        assertEquals(new ImmutableList<>(2, 3, 5), primes.take(3));
        assertEquals(primes, primes.take(primes.size()));
        assertEquals(ints, ints.take(10000));
        assertEquals(new ImmutableList<>(), ints.take(-99));
    }

    @Test
    public void testDrop() {
        assertEquals(emptyList, emptyList.drop(8));
        assertEquals(primes, primes.drop(0));
        assertEquals(primes, primes.drop(-99));
        assertEquals(new ImmutableList<>(14, 15, 16), ints.drop(14));
    }

    @Test
    public void testIsPrefixOf() {
        assertTrue(emptyList.isPrefixOf(emptyList));
        assertTrue(new ImmutableList<Number>(0, 1, 2).isPrefixOf(ints));
        assertFalse(new ImmutableList<>(0, 1, 2).isPrefixOf(primes));
    }

    @Test
    public void testIsSuffixOf() {
        assertTrue(primes.isSuffixOf(primes));
        assertTrue(new ImmutableList<>().isSuffixOf(primes));
        assertTrue(new ImmutableList<>(7, 11, 13).isSuffixOf(primes));
        assertFalse(new ImmutableList<>(14).isSuffixOf(primes));
    }

    @Test
    public void testMap() {
        assertEquals(emptyList, emptyList.map(b -> !b));
        assertEquals(new ImmutableList<>(1, 10, 16, 22, 34, 40), primes.map(collatz));
    }

    @Test
    public void testFoldl() {
        assertEquals(Integer.valueOf(13), primes.foldl(0, Math::max));
        assertEquals(Integer.valueOf(136), ints.foldl(0, (a, e) -> a + e.intValue()));
    }

    @Test
    public void testFilter() {
        assertEquals(new ImmutableList<>(0, 1, 2, 4, 9, 16), ints.filter(isSquare));
    }
}
