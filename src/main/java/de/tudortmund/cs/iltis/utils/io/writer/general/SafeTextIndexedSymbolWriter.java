package de.tudortmund.cs.iltis.utils.io.writer.general;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;

/**
 * This writer creates a string representing the symbol using "_" for subscripts and "^" for
 * superscripts.
 */
public class SafeTextIndexedSymbolWriter implements Writer<IndexedSymbol> {

    /** Writes the symbol by appending the superscript first and subscript second (if present). */
    @Override
    public String write(IndexedSymbol symbol) {
        StringBuilder builder = new StringBuilder(symbol.getName());
        if (symbol.hasSuperscript()) builder.append("^").append(symbol.getSuperscript());
        if (symbol.hasSubscript()) builder.append("_").append(symbol.getSubscript());

        return builder.toString();
    }

    /** Writes the symbol by appending the subscript first and superscript second (if present). */
    public String writeReversed(IndexedSymbol symbol) {
        StringBuilder builder = new StringBuilder(symbol.getName());
        if (symbol.hasSubscript()) builder.append("_").append(symbol.getSubscript());
        if (symbol.hasSuperscript()) builder.append("^").append(symbol.getSuperscript());

        return builder.toString();
    }
}
