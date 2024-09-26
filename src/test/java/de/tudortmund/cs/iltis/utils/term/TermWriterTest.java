package de.tudortmund.cs.iltis.utils.term;

import static org.junit.Assert.assertEquals;

import de.tudortmund.cs.iltis.utils.io.writer.term.TermWriter;
import org.junit.Test;

public class TermWriterTest {
    class StringTerm extends Term<StringTerm, String> {
        private static final long serialVersionUID = 1L;

        public StringTerm(String name, StringTerm... children) {
            super(false, name, children);
        }
    }

    @Test
    public void test() {
        StringTerm t1 = new StringTerm("A");
        StringTerm t2 = new StringTerm("B");
        StringTerm t3 = new StringTerm("C");
        StringTerm t4 = new StringTerm("D", t1, t2);
        StringTerm t5 = new StringTerm("E", t3, t4);

        TermWriter<StringTerm, String> writer = new TermWriter<>("[", "]", ";");

        assertEquals("A", writer.write(t1));
        assertEquals("E[C;D[A;B]]", writer.write(t5));
    }
}
