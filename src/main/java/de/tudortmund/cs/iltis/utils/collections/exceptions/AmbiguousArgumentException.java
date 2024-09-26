package de.tudortmund.cs.iltis.utils.collections.exceptions;

public class AmbiguousArgumentException extends RuntimeException {
    public AmbiguousArgumentException(final String message) {
        super(message);
    }

    /** For serialization */
    @SuppressWarnings("unused")
    protected AmbiguousArgumentException() {}
}
