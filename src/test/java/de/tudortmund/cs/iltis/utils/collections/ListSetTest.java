package de.tudortmund.cs.iltis.utils.collections;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

public class ListSetTest {
    @Test
    public void testEmptyLists() {
        ListSet<Integer> list = new ListSet<>();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertEquals(new ListSet<Integer>(), list);
        assertEquals(setOf(), list);
    }

    @Test
    public void testSingletonLists() {
        ListSet<Integer> list = new ListSet<>();
        list.add(10);

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(new ListSet<Integer>(10), list);
        assertEquals(setOf(10), list);
        list.add(10);
        assertEquals(setOf(10), list);
        list.add(11);
        assertNotEquals(setOf(10), list);
        list.remove(11);
        assertEquals(setOf(10), list);
    }

    @Test
    public void testLongerLists() {
        ListSet<Integer> list = new ListSet<>();

        list.add(10);
        list.add(20, 0, 10, 40);
        assertFalse(list.isEmpty());
        assertEquals(4, list.size());
        // preserve order:
        assertEquals("{10,20,0,40}", list.toString());
        assertEquals(setOf(0, 10, 20, 40), list);
    }

    // TODO: Remove -- if `refactor-Sets` will be part of `development`
    private static Set<Integer> setOf(int... values) {
        return IntStream.of(values).boxed().collect(Collectors.toSet());
    }
}
