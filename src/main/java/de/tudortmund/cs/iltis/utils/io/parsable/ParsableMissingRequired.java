package de.tudortmund.cs.iltis.utils.io.parsable;

public class ParsableMissingRequired extends ParsableException {
    ParsableMissingRequired(Parsable<?, ?> missingParsable) {
        super(missingParsable);
    }

    @Override
    public boolean provideLocationDetailsSelf() {
        return false;
    }

    @Override
    public String toString() {
        return "Missing required " + super.toString();
    }

    /** For serialization */
    @SuppressWarnings("unused")
    private ParsableMissingRequired() {}
}
