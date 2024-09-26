package de.tudortmund.cs.iltis.utils.io.parsable;

public class ParsableInvalidSpecification extends ParsableException {
    private static final String NOTE =
            "(General note: Please check that you have used type adapters where necessary.)";

    private String codeLocationDetails = "";

    public ParsableInvalidSpecification(Parsable<?, ?> parsable) {
        super(parsable, NOTE);
    }

    public ParsableInvalidSpecification(String message) {
        super(null, message + "\n" + NOTE);
    }

    public ParsableInvalidSpecification(String message, Exception suppressed) {
        super(null, message + "\n" + NOTE, suppressed);
    }

    public ParsableInvalidSpecification(Parsable<?, ?> parsable, String message) {
        super(parsable, message + "\n" + NOTE);
    }

    public ParsableInvalidSpecification(Parsable<?, ?> parsable, Exception suppressed) {
        super(parsable, NOTE, suppressed);
    }

    public ParsableInvalidSpecification(
            Parsable<?, ?> parsable, String message, Exception suppressed) {
        super(parsable, message + "\n" + NOTE, suppressed);
    }

    public void setCodeLocationDetails(String codeLocationDetails) {
        this.codeLocationDetails = codeLocationDetails;
    }

    @Override
    public String getMessage() {
        return codeLocationDetails + ": " + super.getMessage();
    }

    /** For serialization */
    @SuppressWarnings("unused")
    private ParsableInvalidSpecification() {}
}
