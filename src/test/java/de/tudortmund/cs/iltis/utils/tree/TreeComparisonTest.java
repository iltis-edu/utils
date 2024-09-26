package de.tudortmund.cs.iltis.utils.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TreeComparisonTest {

    /**
     * Very basic test. A more sophisticated test is done by predicate formulae which are based on
     * trees.
     */
    @Test
    public void test() {
        MyTree<?> atree = new MyTree<String>();
        MyTree<?> btree = new MyTree<Integer>();
        assertEquals(atree, btree);

        atree.addChild(new MyTree<>());
        assertNotEquals(atree, btree);

        btree.addChild(new MyTree<>());
        assertEquals(atree, btree);
    }

    private class MyTree<T> extends Tree<MyTree<T>> {
        private static final long serialVersionUID = 1L;
    }
}
