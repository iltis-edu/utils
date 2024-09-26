package de.tudortmund.cs.iltis.utils.io.parser.general;

import de.tudortmund.cs.iltis.utils.io.Creatable;
import java.io.Serializable;

/** An interface for formula which have a type and therefore are suitable for parsing. */
public interface ParsablyTyped extends Creatable, Serializable {

    ParsableType getType();
}
