package de.tudortmund.cs.iltis.utils.io.parser.parentheses;

import de.tudortmund.cs.iltis.utils.collections.SerializablePair;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsableSymbol;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface ParenthesesCheckPropertiesProvidable extends Cloneable, Serializable {
    Map<ParenthesesType, SerializablePair<ParsableSymbol, ParsableSymbol>> getParenthesesSymbols();

    default Set<ParenthesesType> getAllowedParenthesesTypes() {
        return getParenthesesSymbols().keySet();
    }

    ParenthesesCheckPropertiesProvidable clone();
}
