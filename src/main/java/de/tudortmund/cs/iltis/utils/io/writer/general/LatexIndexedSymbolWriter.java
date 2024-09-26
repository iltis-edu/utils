package de.tudortmund.cs.iltis.utils.io.writer.general;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This writer creates a LaTeX string representing the symbol. Exactly the characters $, %, #, & and
 * ~ are escaped. The result of {@link #write(IndexedSymbol)} is designed to be used in math mode;
 * {@link #writeInline(IndexedSymbol)} and {@link #writeDisplay(IndexedSymbol)} add designed to work
 * in text mode because they add the respective math mode delimiters.
 */
public class LatexIndexedSymbolWriter implements Writer<IndexedSymbol> {

    private Map<String, String> replacements;

    public LatexIndexedSymbolWriter() {
        replacements = new LinkedHashMap<>();
        replacements.put("\\$", "\\$");
        replacements.put("\\%", "\\%");
        replacements.put("\\#", "\\#");
        replacements.put("\\&", "\\&");
        replacements.put("\\~", "\\sim{}");
    }

    /** Writes the given symbol. The result is designed to be used in math mode. */
    @Override
    public String write(IndexedSymbol symbol) {
        String text = symbol.getName();
        for (Entry<String, String> replacement : replacements.entrySet()) {
            text = text.replaceAll(replacement.getKey(), replacement.getValue());
        }

        if (symbol.hasSuperscript()) {
            String superscript = symbol.getSuperscript();
            for (Entry<String, String> replacement : replacements.entrySet()) {
                superscript = superscript.replaceAll(replacement.getKey(), replacement.getValue());
            }
            if (superscript.length() > 1) text += "^{" + superscript + "}";
            else text += "^" + superscript;
        }
        if (symbol.hasSubscript()) {
            String subscript = symbol.getSubscript();
            for (Entry<String, String> replacement : replacements.entrySet()) {
                subscript = subscript.replaceAll(replacement.getKey(), replacement.getValue());
            }
            if (subscript.length() > 1) text += "_{" + subscript + "}";
            else text += "_" + subscript;
        }
        return text;
    }

    /** Writes the given symbol with inline math delimiters. */
    public String writeInline(IndexedSymbol symbol) {
        return "\\(" + write(symbol) + "\\)";
    }

    /** Writes the given symbol with display math delimiters. */
    public String writeDisplay(IndexedSymbol symbol) {
        return "\\[" + write(symbol) + "\\]";
    }
}
