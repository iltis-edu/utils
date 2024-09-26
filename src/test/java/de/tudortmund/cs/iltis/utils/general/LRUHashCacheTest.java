package de.tudortmund.cs.iltis.utils.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LRUHashCacheTest {
    @Test
    public void simpleCacheTest() {
        LRUHashCache<Integer, Integer> cache;

        cache = new LRUHashCache<>(3);

        // add three items (0,1,2) first:
        for (int i = 0; i < 3; i++) cache.put(i, i * i);
        for (int i = 0; i < 3; i++) {
            assertTrue(cache.get(i).isPresent());
            Integer expected = i * i;
            Integer actual = cache.get(i).get();
            assertEquals(expected, actual);
        }

        Integer expected, actual;

        // add a fourth item (exceeding capacity):
        cache.put(3, 3 * 3);
        // item 0 should be removed, items 1,2,3 still there:
        assertFalse(cache.hasHit(0));
        for (int i = 1; i < 4; i++) assertTrue(cache.hasHit(i));
        expected = 3 * 3;
        actual = cache.get(3).get();
        assertEquals(expected, actual);

        // once more, add a fifth item (exceeding capacity again):
        cache.put(4, 4 * 4);
        assertFalse(cache.hasHit(0));
        for (int i = 2; i < 5; i++) assertTrue(cache.hasHit(i));
        expected = 4 * 4;
        actual = cache.get(4).get();
        assertEquals(expected, actual);
    }

    @Test
    public void simplePutCapacityTest() {
        LRUHashCache<Integer, Integer> cache;

        // Each input should only use one slot of the cache:
        cache = new LRUHashCache<>(3);
        for (int i = 0; i < 3; i++) {
            assertEquals(i, cache.getSize());
            cache.put(i, i * i);
            assertEquals(i + 1, cache.getSize());
        }
        for (int i = 0; i < 3; i++) {
            cache.put(i, i * i);
            assertEquals(3, cache.getSize());
        }
    }

    @Test
    public void simpleSupplierTest() {
        LRUHashCache<Integer, Integer> cache;

        cache = new LRUHashCache<>(3);
        cache.put(2, 2);
        cache.put(3, 3);
        assertEquals((Integer) 2, cache.get(2).get());
        assertEquals((Integer) 3, cache.get(3).get());
        assertEquals((Integer) 3, cache.get(3, () -> 9).get());
        assertEquals((Integer) 16, cache.get(4, () -> 16).get());
        assertEquals(3, cache.getSize());

        cache = new LRUHashCache<>(3);
        cache.put(2, 2);
        cache.put(3, 3);
        assertEquals((Integer) 2, cache.get(2).get());
        assertEquals((Integer) 3, cache.get(3).get());
        assertEquals((Integer) 3, cache.get(3, x -> x * x).get());
        assertEquals((Integer) 16, cache.get(4, x -> x * x).get());
        assertEquals(3, cache.getSize());
    }
}
