package de.tudortmund.cs.iltis.utils.tree;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Objects;
import org.junit.Test;

public class TreeIteratorTest {

    protected static class IntegerTree extends Tree<IntegerTree> {
        private static final long serialVersionUID = 1L;
        public int value;

        public IntegerTree(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj) && value == ((IntegerTree) obj).value;
        }
    }

    protected static class ExtendedIntegerTree extends IntegerTree {
        private static final long serialVersionUID = 1L;
        public int value;

        public ExtendedIntegerTree(int value) {
            super(value);
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj) && value == ((ExtendedIntegerTree) obj).value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), value);
        }
    }

    @Test
    public void simpleTree() {
        IntegerTree t1 = new IntegerTree(1);
        IntegerTree t2 = new IntegerTree(2);
        IntegerTree t3 = new IntegerTree(3);
        ExtendedIntegerTree t4 = new ExtendedIntegerTree(4);
        IntegerTree t5 = new IntegerTree(5);
        IntegerTree t6 = new IntegerTree(6);

        t2.addChildren(t1, t3);
        t6.addChildren(t5);
        t4.addChildren(t2, t6);

        List<IntegerTree> ordered = t4.getDescendants(t4.preorderDescendantIterator());
        assertEquals(4, ordered.remove(0).value);
        assertEquals(2, ordered.remove(0).value);
        assertEquals(1, ordered.remove(0).value);
        assertEquals(3, ordered.remove(0).value);
        assertEquals(6, ordered.remove(0).value);
        assertEquals(5, ordered.remove(0).value);

        ordered = t4.getDescendants();
        assertEquals(1, ordered.remove(0).value);
        assertEquals(3, ordered.remove(0).value);
        assertEquals(2, ordered.remove(0).value);
        assertEquals(5, ordered.remove(0).value);
        assertEquals(6, ordered.remove(0).value);
        assertEquals(4, ordered.remove(0).value);
    }

    @Test
    public void flatTree() {
        IntegerTree t1 = new IntegerTree(1);
        IntegerTree t2 = new IntegerTree(2);
        IntegerTree t3 = new IntegerTree(3);
        IntegerTree t4 = new IntegerTree(4);

        t1.addChildren(t2, t3, t4);

        List<IntegerTree> ordered = t1.getDescendants(t1.preorderDescendantIterator());
        assertEquals(1, ordered.remove(0).value);
        assertEquals(2, ordered.remove(0).value);
        assertEquals(3, ordered.remove(0).value);
        assertEquals(4, ordered.remove(0).value);

        ordered = t1.getDescendants();
        assertEquals(2, ordered.remove(0).value);
        assertEquals(3, ordered.remove(0).value);
        assertEquals(4, ordered.remove(0).value);
        assertEquals(1, ordered.remove(0).value);
    }

    @Test
    public void smallTree() {
        IntegerTree t1 = new IntegerTree(1);

        List<IntegerTree> ordered = t1.getDescendants(t1.preorderDescendantIterator());
        assertEquals(1, ordered.remove(0).value);

        ordered = t1.getDescendants();
        assertEquals(1, ordered.remove(0).value);
    }
}
