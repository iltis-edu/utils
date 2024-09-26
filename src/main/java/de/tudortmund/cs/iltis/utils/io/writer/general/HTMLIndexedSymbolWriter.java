package de.tudortmund.cs.iltis.utils.io.writer.general;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;

/**
 * This writer creates a HTML string representing the symbol using "&lt;sub&gt;" for subscripts and
 * "&lt;sup&gt;" for superscripts.
 */
public class HTMLIndexedSymbolWriter implements Writer<IndexedSymbol> {

    @Override
    public String write(IndexedSymbol symbol) {
        String text = symbol.getName();
        if (symbol.hasSuperscript()) text += "<sup>" + symbol.getSuperscript() + "</sup>";
        if (symbol.hasSubscript()) text += "<sub>" + symbol.getSubscript() + "</sub>";

        return text;
    }
}
