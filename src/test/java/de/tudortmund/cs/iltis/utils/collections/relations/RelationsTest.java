package de.tudortmund.cs.iltis.utils.collections.relations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RelationsTest {
    @Test
    public void simpleFiniteRelationTest() {
        FiniteBinaryRelation<Integer> rel;

        rel = new FiniteBinaryRelation<>();
        assertTrue(rel.isEmpty());
        rel.add(1, 2);
        rel.add(3, 4);
        assertFalse(rel.isEmpty());
        assertEquals(2, rel.size());
        assertTrue(rel.contains(1, 2));
        assertTrue(rel.contains(3, 4));
        assertFalse(rel.contains(2, 1));
        assertFalse(rel.contains(1, 4));
        assertFalse(rel.contains(4, 3));
    }
}
