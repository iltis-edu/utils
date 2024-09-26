package de.tudortmund.cs.iltis.utils.io.parser.symbol;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.collections.Pair;
import de.tudortmund.cs.iltis.utils.general.Data;
import java.util.List;
import org.junit.Test;

public class FiniteSetSymbolSplittingPolicyTest {

    private final IndexedSymbol a0 = new IndexedSymbol("a", "0", "");
    private final IndexedSymbol a1 = new IndexedSymbol("a", "1", "");
    private final IndexedSymbol a2 = new IndexedSymbol("a", "2", "");
    private final IndexedSymbol a3 = new IndexedSymbol("a", "3", "");
    private final IndexedSymbol a4 = new IndexedSymbol("a", "4", "");
    private final IndexedSymbol a5 = new IndexedSymbol("a", "5", "");

    private final FiniteSetSymbolSplittingPolicy policy =
            new FiniteSetSymbolSplittingPolicy(Data.newListSet1(a0, a1, a2, a3, a4, a5));

    @Test
    public void testSplitEmptyString() {
        List<Pair<Integer, IndexedSymbol>> symbols = policy.splitSymbols("");
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void testSplitSingleStringWithOneIndexedSymbol() {
        List<Pair<Integer, IndexedSymbol>> symbols = policy.splitSymbols("a_2");
        assertEquals(1, symbols.size());
        assertTrue(symbols.contains(new Pair<>(0, a2)));
    }

    @Test
    public void testSplitSingleStringWithMultipleIndexedSymbols() {
        List<Pair<Integer, IndexedSymbol>> symbols = policy.splitSymbols("a_2a_0a_0a_5");
        assertEquals(4, symbols.size());
        assertTrue(symbols.contains(new Pair<>(0, a2)));
        assertTrue(symbols.contains(new Pair<>(3, a0)));
        assertTrue(symbols.contains(new Pair<>(6, a0)));
        assertTrue(symbols.contains(new Pair<>(9, a5)));
    }

    @Test(expected = SymbolsNotSplittableException.class)
    public void testSplitSingleStringWithInvalidPrefix() {
        policy.splitSymbols("ba_2");
    }

    @Test(expected = SymbolsNotSplittableException.class)
    public void testSplitSingleStringWithInvalidInfix() {
        policy.splitSymbols("a_3ba_0");
    }

    @Test(expected = SymbolsNotSplittableException.class)
    public void testSplitSingleStringWithInvalidSuffix() {
        policy.splitSymbols("a_3a_0a_bbb");
    }

    @Test
    public void testIsExpressible() {
        assertTrue(policy.isExpressible(new IndexedSymbol("a", "4", "")));
        assertFalse(policy.isExpressible(new IndexedSymbol("a", "9", "")));
        assertFalse(policy.isExpressible(new IndexedSymbol("a", "2", "i")));
        assertFalse(policy.isExpressible(new IndexedSymbol("b", "", "")));
    }
}
