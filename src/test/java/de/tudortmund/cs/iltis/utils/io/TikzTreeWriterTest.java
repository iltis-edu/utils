package de.tudortmund.cs.iltis.utils.io;

import static org.junit.Assert.assertTrue;

import de.tudortmund.cs.iltis.utils.io.writer.general.DefaultWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;
import de.tudortmund.cs.iltis.utils.io.writer.tree.TikzTreeWriter;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.Objects;
import org.junit.Test;

public class TikzTreeWriterTest {

    class IntegerTree extends Tree<IntegerTree> {
        private static final long serialVersionUID = 1L;

        public int value;

        public IntegerTree(int value, IntegerTree... subtrees) {
            super(subtrees);
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return "" + this.value;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj) && value == ((IntegerTree) obj).value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), value);
        }
    }

    class IntMarker implements Writer<IntegerTree> {
        @Override
        public String write(IntegerTree tree) {
            return (tree.getValue() % 2 == 1) ? "fill" : "draw";
        }
    }

    @Test
    public void simpleIntTreeOutput() {
        IntegerTree t1 = new IntegerTree(1);
        IntegerTree t3 = new IntegerTree(3);
        IntegerTree t2 = new IntegerTree(2, t1, t3);
        IntegerTree t5 = new IntegerTree(5);
        IntegerTree t6 = new IntegerTree(6, t5);
        IntegerTree t4 = new IntegerTree(4, t2, t6);

        try {
            DefaultWriter<IntegerTree> def = new DefaultWriter<>();
            IntMarker marker = new IntMarker();
            TikzTreeWriter<IntegerTree> w = new TikzTreeWriter<IntegerTree>(def, marker);
            String output;

            output = w.write(t1);
            System.out.println(output);

            output = w.write(t2);
            System.out.println(output);

            output = w.write(t4);
            System.out.println(output);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
