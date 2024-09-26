package de.tudortmund.cs.iltis.utils.collections.exceptions;

public class AmbiguousColumnLabelException extends AmbiguousArgumentException {

    public AmbiguousColumnLabelException(Object columnLabel) {
        super("Ambiguous column label '" + columnLabel + "'");
    }

    /** For serialization */
    @SuppressWarnings("unused")
    private AmbiguousColumnLabelException() {
        super();
    }
}
