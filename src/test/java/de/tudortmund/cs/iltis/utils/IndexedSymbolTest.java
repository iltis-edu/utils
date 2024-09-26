package de.tudortmund.cs.iltis.utils;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.collections.ListSet;
import org.junit.Test;

public class IndexedSymbolTest {

    @Test
    public void test() {
        IndexedSymbol A = new IndexedSymbol("A");
        IndexedSymbol A_alt = new IndexedSymbol("A");
        IndexedSymbol B = new IndexedSymbol("B");
        IndexedSymbol A_1 = new IndexedSymbol("A^1");
        IndexedSymbol A__1 = new IndexedSymbol("A_1");
        IndexedSymbol A_11_22 = new IndexedSymbol("A^11_22");
        IndexedSymbol A_22_11 = new IndexedSymbol("A_22^11");
        IndexedSymbol Rel = new IndexedSymbol("Rel");

        assertEquals(A, A_alt);
        assertFalse(A.equals(B));
        assertFalse(A.equals(A_1));
        assertFalse(A.equals(A__1));
        assertFalse(A.equals(A_11_22));

        assertEquals(A_11_22.getName(), "A");
        assertEquals(A_11_22.getSuperscript(), "11");
        assertEquals(A_11_22.getSubscript(), "22");

        assertEquals(A_11_22, A_22_11);

        assertEquals(Rel.getName(), "Rel");
        assertFalse(Rel.hasSuperscript());
        assertFalse(Rel.hasSubscript());
    }

    @Test
    public void testIsPrefixOf() {
        assertTrue(new IndexedSymbol("a").isPrefixOf(new IndexedSymbol("abcd")));

        assertTrue(new IndexedSymbol("a").isPrefixOf(new IndexedSymbol("a")));
        assertTrue(new IndexedSymbol("a").isPrefixOf(new IndexedSymbol("a_1")));
        assertTrue(new IndexedSymbol("a").isPrefixOf(new IndexedSymbol("a^2")));
        assertTrue(new IndexedSymbol("a").isPrefixOf(new IndexedSymbol("a^i_k")));
        assertTrue(new IndexedSymbol("a").isPrefixOf(new IndexedSymbol("a_9^b")));

        assertTrue(new IndexedSymbol("a_b").isPrefixOf(new IndexedSymbol("a_bc")));
        assertTrue(new IndexedSymbol("a_b").isPrefixOf(new IndexedSymbol("a_b^1")));
        assertTrue(new IndexedSymbol("a_b").isPrefixOf(new IndexedSymbol("a^1_b")));
        assertTrue(new IndexedSymbol("a_1^k").isPrefixOf(new IndexedSymbol("a_12^k")));
        assertTrue(new IndexedSymbol("a_1^k").isPrefixOf(new IndexedSymbol("a^ki_1")));

        assertFalse(new IndexedSymbol("a").isPrefixOf(new IndexedSymbol("ba")));
        assertFalse(new IndexedSymbol("a_1").isPrefixOf(new IndexedSymbol("ab_1")));
        assertFalse(new IndexedSymbol("ab_1").isPrefixOf(new IndexedSymbol("a_1")));
        assertFalse(new IndexedSymbol("a_1^k").isPrefixOf(new IndexedSymbol("a^1_k")));
    }

    @Test
    public void testIsPrefixFree() {
        ListSet<IndexedSymbol> alphabet1 =
                new ListSet<>(
                        new IndexedSymbol("a_1"),
                        new IndexedSymbol("a_2"),
                        new IndexedSymbol("a_3"),
                        new IndexedSymbol("b^0"),
                        new IndexedSymbol("b^1"));

        ListSet<IndexedSymbol> alphabet2 =
                new ListSet<>(
                        new IndexedSymbol("a_1^k"),
                        new IndexedSymbol("a_12^k"),
                        new IndexedSymbol("b_i"),
                        new IndexedSymbol("b^0_i"),
                        new IndexedSymbol("c"));

        assertTrue(IndexedSymbol.isPrefixFree(alphabet1));
        assertFalse(IndexedSymbol.isPrefixFree(alphabet2));
    }
}
