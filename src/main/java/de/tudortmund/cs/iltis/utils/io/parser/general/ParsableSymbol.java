package de.tudortmund.cs.iltis.utils.io.parser.general;

import java.io.Serializable;

public interface ParsableSymbol extends Serializable {
    int getTokenType();
}
