package de.tudortmund.cs.iltis.utils.io.parsable;

public class ParsableInvalidValue extends ParsableException {
    public ParsableInvalidValue(Parsable<?, ?> parsable, String message) {
        super(parsable, message);
    }

    public ParsableInvalidValue(Parsable<?, ?> parsable, String message, Exception suppressed) {
        super(parsable, message, suppressed);
    }

    @Override
    public boolean provideLocationDetailsSelf() {
        return getParsable() != null;
    }

    @Override
    public String toString() {
        return "Invalid value: " + super.toString();
    }

    /** For serialization */
    @SuppressWarnings("unused")
    private ParsableInvalidValue() {
        super();
    }
}
