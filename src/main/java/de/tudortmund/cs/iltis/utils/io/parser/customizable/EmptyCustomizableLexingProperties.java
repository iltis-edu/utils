package de.tudortmund.cs.iltis.utils.io.parser.customizable;

import de.tudortmund.cs.iltis.utils.collections.SerializablePair;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsableSymbol;
import de.tudortmund.cs.iltis.utils.io.parser.parentheses.ParenthesesType;
import java.util.Map;
import java.util.TreeMap;

public class EmptyCustomizableLexingProperties implements CustomizableLexingPropertiesProvidable {

    @Override
    public Map<ParenthesesType, SerializablePair<ParsableSymbol, ParsableSymbol>>
            getParenthesesSymbols() {
        return new TreeMap<>();
    }

    @Override
    public ParsableSymbol getSymbolForNonSeparatedText() {
        return () -> -1;
    }

    @Override
    public ParsableSymbol getSymbolForISymbols() {
        return () -> -1;
    }

    @Override
    public ParsableSymbol getSymbolForSeparation() {
        return () -> -1;
    }

    @Override
    public Map<String, ParsableSymbol> getSeparatingAndSeparationSymbols() {
        return new TreeMap<>();
    }

    @Override
    public Map<String, ParsableSymbol> getNonSeparatingSymbols() {
        return new TreeMap<>();
    }

    @Override
    public EmptyCustomizableLexingProperties clone() {
        return new EmptyCustomizableLexingProperties();
    }
}
