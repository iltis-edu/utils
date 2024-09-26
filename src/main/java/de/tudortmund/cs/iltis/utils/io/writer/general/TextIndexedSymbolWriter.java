package de.tudortmund.cs.iltis.utils.io.writer.general;

import com.google.gwt.regexp.shared.RegExp;
import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.StringUtils;

/**
 * This writer creates a string representing the symbol using "_" for subscripts and "^" for
 * superscripts.
 */
public class TextIndexedSymbolWriter implements Writer<IndexedSymbol> {

    private RegExp onlyDigitsInIndexRegExp;

    public TextIndexedSymbolWriter() {
        String onlyDigitsInIndexPattern = "^[0-9]+$";
        onlyDigitsInIndexRegExp = RegExp.compile(onlyDigitsInIndexPattern);
    }

    @Override
    public String write(IndexedSymbol symbol) {
        String text = symbol.getName();
        if (symbol.hasSuperscript()) {
            String superscript = symbol.getSuperscript();
            if (onlyDigitsInIndexRegExp.test(superscript)) {
                for (int i = 0; i < 10; i++) {
                    superscript =
                            superscript.replaceAll(
                                    String.valueOf(i), StringUtils.getUnicodeSuperscript(i));
                }
                text += superscript;
            } else {
                text += "^" + superscript;
            }
        }
        if (symbol.hasSubscript()) {
            String subscript = symbol.getSubscript();
            if (onlyDigitsInIndexRegExp.test(subscript)) {
                for (int i = 0; i < 10; i++) {
                    subscript =
                            subscript.replaceAll(
                                    String.valueOf(i), StringUtils.getUnicodeSubscript(i));
                }
                text += subscript;
            } else {
                text += "_" + subscript;
            }
        }
        return text;
    }
}
