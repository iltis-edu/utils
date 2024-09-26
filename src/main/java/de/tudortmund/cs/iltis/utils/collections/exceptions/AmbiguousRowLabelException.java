package de.tudortmund.cs.iltis.utils.collections.exceptions;

public class AmbiguousRowLabelException extends AmbiguousArgumentException {

    public AmbiguousRowLabelException(Object rowLabel) {
        super("Ambiguous row label '" + rowLabel + "'");
    }

    /** For serialization */
    @SuppressWarnings("unused")
    private AmbiguousRowLabelException() {}
}
