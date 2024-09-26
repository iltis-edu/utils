package de.tudortmund.cs.iltis.utils.io.parser.error;

import de.tudortmund.cs.iltis.utils.io.parser.fault.ParsingFaultTypeMapping;
import java.io.Serializable;
import java.util.Objects;

/**
 * An exception to signal an unsuccessful parsing. It is a shallow wrapper for a
 * ParsingFaultTypeMapping object.
 */
public class IncorrectParseInputException extends RuntimeException implements Serializable {

    private ParsingFaultTypeMapping<?> mapping;

    /**
     * @param mapping a mapping object, not null
     * @throws NullPointerException if the given mapping is null
     */
    public IncorrectParseInputException(ParsingFaultTypeMapping<?> mapping) {
        Objects.requireNonNull(mapping);
        this.mapping = mapping;
    }

    /**
     * @return the mapping object of any formula type, never null
     */
    public ParsingFaultTypeMapping<?> getFaultMapping() {
        return mapping;
    }

    /** For serialization */
    private static final long serialVersionUID = 1L;

    /** For serialization */
    @SuppressWarnings("unused")
    private IncorrectParseInputException() {}

    @Override
    public String toString() {
        return super.toString() + ": " + mapping.toString();
    }
}
