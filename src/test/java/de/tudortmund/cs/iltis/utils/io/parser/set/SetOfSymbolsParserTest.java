package de.tudortmund.cs.iltis.utils.io.parser.set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.io.parser.error.IncorrectParseInputException;
import de.tudortmund.cs.iltis.utils.io.reader.set.SetOfSymbolsReader;
import de.tudortmund.cs.iltis.utils.io.reader.set.SetOfSymbolsReaderProperties;
import java.util.List;
import org.junit.Test;

public class SetOfSymbolsParserTest {

    @Test
    public void testSetOfSymbolsParser() {
        SetOfSymbolsReaderProperties props = SetOfSymbolsReaderProperties.createDefault();
        SetOfSymbolsReader setOfSymbolsReader = new SetOfSymbolsReader(props);

        List<IndexedSymbol> output1 = setOfSymbolsReader.read("{}");
        assertEquals(0, output1.size());

        List<IndexedSymbol> output2 = setOfSymbolsReader.read("{a, b, c, d,     e}");
        assertEquals(5, output2.size());
        assertEquals(new IndexedSymbol("c"), output2.get(2));

        boolean exceptionCatched = false;
        try {
            setOfSymbolsReader.read("{a, b, c,}");
        } catch (IncorrectParseInputException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);

        List<IndexedSymbol> output3 = setOfSymbolsReader.read("{a_3^c, b, 7^4_3}");
        assertEquals(3, output3.size());
        assertEquals(new IndexedSymbol("a", "3", "c"), output3.get(0));
        assertEquals(new IndexedSymbol("b", "", ""), output3.get(1));
        assertEquals(new IndexedSymbol("7", "3", "4"), output3.get(2));
    }
}
