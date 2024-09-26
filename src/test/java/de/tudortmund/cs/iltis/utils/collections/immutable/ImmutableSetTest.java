package de.tudortmund.cs.iltis.utils.collections.immutable;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import org.junit.Test;

public class ImmutableSetTest {

    ImmutableSet<Number> noNumbers = new ImmutableSet<>();
    ImmutableSet<Number> myNumbers = new ImmutableSet<>(0.0, 0.1);
    ImmutableSet<Double> myDoubles = new ImmutableSet<>(0.0, 0.1, 0.2, 0.3, 0.4, 0.5);
    ImmutableSet<Number> myHalves = new ImmutableSet<>(0.0, 0.5, 0.25, 0.125, 0.0625);
    ImmutableSet<Number> myPrimes = new ImmutableSet<>(2, 3, 5, 7, 11, 13);
    ImmutableSet<Integer> myPowersOfTwo = new ImmutableSet<>(1, 2, 4, 8, 16);
    ImmutableSet<Integer> myInts = new ImmutableSet<>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
    ImmutableSet<Number> myBits = new ImmutableSet<>(0, 1);

    Function<Number, Number> timesTen = n -> (int) Math.round(10 * n.doubleValue());
    Function<Integer, Double> fancyRecip = n -> n.equals(1) ? 0.0 : 1.0 / n;
    Predicate<Integer> isEven = n -> n % 2 == 0;
    Predicate<Number> constFalse = n -> false;

    @Test
    public void testSize() {
        assertEquals(2, myBits.size());
        assertEquals(5, myPowersOfTwo.size());
        assertEquals(6, myDoubles.size());
        assertEquals(14, myInts.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(noNumbers.isEmpty());
        assertFalse(myNumbers.isEmpty());
        assertFalse(myHalves.isEmpty());
        assertFalse(myPrimes.isEmpty());
    }

    @Test
    public void testContains() {
        assertTrue(myNumbers.contains(0.0));
        assertTrue(myDoubles.contains(0.4));
        assertFalse(myPrimes.contains(4));
        assertFalse(myInts.contains(-2));
    }

    @Test
    public void testContainsAll() {
        assertTrue(myNumbers.containsAll(new ArrayList<>()));
        assertTrue(myPrimes.containsAll(Arrays.asList(2, 3, 7)));
        assertFalse(myDoubles.containsAll(Arrays.asList(0.0, 1.0, 2.0)));
    }

    @Test
    public void testIterator() {
        int i = 0;
        // although myInts is a set, iteration order is identical to insertion order
        for (int j : myInts) {
            assertEquals(i++, j);
        }
    }

    @Test
    public void testUnion() {
        assertEquals(myInts, noNumbers.unionWith(myInts));
        assertEquals(myInts, myPrimes.unionWith(myInts));
        assertEquals(new ImmutableSet<>(0, 1, 2, 4, 8, 16), myBits.unionWith(myPowersOfTwo));
    }

    @Test
    public void testIntersection() {
        assertEquals(noNumbers, myHalves.intersectionWith(noNumbers));
        assertEquals(new ImmutableSet<>(0.0, 0.5), myHalves.intersectionWith(myDoubles));
    }

    @Test
    public void testDifference() {
        assertEquals(noNumbers, myPrimes.differenceWith(myInts));
        assertEquals(myPrimes, myPrimes.differenceWith(myBits));
    }

    @Test
    public void testComplement() {
        assertEquals(new ImmutableSet<>(0, 1, 4, 6, 8, 9, 10, 12), myPrimes.complementWith(myInts));
        assertEquals(new ImmutableSet<>(2, 4, 8, 16), myBits.complementWith(myPowersOfTwo));
    }

    @Test
    public void testIsSubset() {
        assertTrue(myBits.isSubsetOf(myInts));
        assertTrue(noNumbers.isSubsetOf(myDoubles));
        assertTrue(myPrimes.isSubsetOf(myInts));
        assertFalse(myInts.isSubsetOf(myPowersOfTwo));
        assertFalse(myNumbers.isSubsetOf(noNumbers));
    }

    @Test
    public void testIsSuperset() {
        assertTrue(myInts.isSupersetOf(myInts));
        assertTrue(myBits.isSupersetOf(noNumbers));
        assertFalse(myPrimes.isSupersetOf(myInts));
    }

    @Test
    public void testIsDisjoint() {
        assertTrue(noNumbers.isDisjointWith(myHalves));
        assertTrue(myHalves.isDisjointWith(myPrimes));
        assertFalse(myPrimes.isDisjointWith(myInts));
    }

    @Test
    public void testMap() {
        assertEquals(myBits, myNumbers.map(timesTen));
        assertEquals(myHalves, myPowersOfTwo.map(fancyRecip));
    }

    @Test
    public void testFoldl() {
        assertEquals(Integer.valueOf(0), noNumbers.foldl(0, (a, e) -> a + e.intValue()));
        assertEquals(Double.valueOf(0.0625), myHalves.foldl(-1.0, (a, e) -> e.doubleValue()));
        assertEquals(Integer.valueOf(13), myInts.foldl(-10, Math::max));
    }

    @Test
    public void testFilter() {
        assertEquals(new ImmutableSet<>(2, 4, 8, 16), myPowersOfTwo.filter(isEven));
        assertEquals(noNumbers, myDoubles.filter(constFalse));
    }
}
