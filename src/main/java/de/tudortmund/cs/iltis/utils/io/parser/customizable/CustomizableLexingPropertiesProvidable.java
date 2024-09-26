package de.tudortmund.cs.iltis.utils.io.parser.customizable;

import de.tudortmund.cs.iltis.utils.function.SerializableComparator;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsableSymbol;
import de.tudortmund.cs.iltis.utils.io.parser.parentheses.ParenthesesCheckPropertiesProvidable;
import de.tudortmund.cs.iltis.utils.io.parser.symbol.BaseSymbolSplittingPolicy;
import de.tudortmund.cs.iltis.utils.io.parser.symbol.SymbolSplittingPolicy;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public interface CustomizableLexingPropertiesProvidable
        extends ParenthesesCheckPropertiesProvidable {

    default Map<String, ParsableSymbol> getSeparatingAndSeparationSymbols() {
        Map<String, ParsableSymbol> result = new TreeMap<>(lengthComparator);
        result.putAll(getSeparatingSymbols());
        result.putAll(
                getSeparationSymbols().stream()
                        .collect(Collectors.toMap(s -> s, s -> getSymbolForSeparation())));
        return result;
    }

    default Set<String> getSeparationSymbols() {
        return getSeparatingAndSeparationSymbols().entrySet().stream()
                .filter(entry -> entry.getValue().equals(getSymbolForSeparation()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    default Map<String, ParsableSymbol> getSeparatingSymbols() {
        return getSeparatingAndSeparationSymbols().entrySet().stream()
                .filter(entry -> !entry.getValue().equals(getSymbolForSeparation()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    Map<String, ParsableSymbol> getNonSeparatingSymbols();

    default SymbolSplittingPolicy getSymbolSplittingPolicy() {
        return new BaseSymbolSplittingPolicy();
    }

    ParsableSymbol getSymbolForNonSeparatedText();

    ParsableSymbol getSymbolForISymbols();

    ParsableSymbol getSymbolForSeparation();

    SerializableComparator<String> lengthComparator =
            (s1, s2) -> {
                int lengthDif = s2.length() - s1.length();
                if (lengthDif != 0) return lengthDif;
                return s1.compareTo(s2);
            };

    @Override
    CustomizableLexingPropertiesProvidable clone();
}
