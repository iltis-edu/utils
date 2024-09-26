package de.tudortmund.cs.iltis.utils.tree;

import static org.junit.Assert.assertEquals;

import de.tudortmund.cs.iltis.utils.io.writer.tree.TreeWriter;
import de.tudortmund.cs.iltis.utils.tree.TreeIteratorTest.ExtendedIntegerTree;
import de.tudortmund.cs.iltis.utils.tree.TreeIteratorTest.IntegerTree;
import org.junit.Test;

public class TreeWriterTest {

    @Test
    public void test() {
        IntegerTree t1 = new IntegerTree(1);
        IntegerTree t2 = new IntegerTree(2);
        IntegerTree t3 = new IntegerTree(3);
        ExtendedIntegerTree t4 = new ExtendedIntegerTree(4);
        IntegerTree t5 = new IntegerTree(5);
        IntegerTree t6 = new IntegerTree(6);

        t2.addChildren(t1, t3);
        t6.addChildren(t5);
        t4.addChildren(t2, t6);

        TreeWriter<IntegerTree> writer = new TreeWriter<>("[", "]", ";");

        assertEquals("[]", writer.write(t1));
        assertEquals("[[];[]]", writer.write(t2));
        assertEquals("[[]]", writer.write(t6));
        assertEquals("[[[];[]];[[]]]", writer.write(t4));
    }
}
