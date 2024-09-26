package de.tudortmund.cs.iltis.utils.io.parsable;

public class ParsableException extends RuntimeException {
    private Parsable<?, ?> parsable = null;
    private String locationDetails = "";

    ParsableException(Parsable<?, ?> parsable) {
        super();
        this.parsable = parsable;
    }

    ParsableException(Parsable<?, ?> parsable, String message) {
        super(message);
        this.parsable = parsable;
    }

    ParsableException(Parsable<?, ?> parsable, Exception suppressed) {
        super(suppressed);
        this.parsable = parsable;
    }

    ParsableException(String message, Exception suppressed) {
        super(message, suppressed);
    }

    ParsableException(Parsable<?, ?> parsable, String message, Exception suppressed) {
        super(message, suppressed);
        this.parsable = parsable;
    }

    protected final Parsable<?, ?> getParsable() {
        return this.parsable;
    }

    public final String getLocationDetails() {
        return this.locationDetails;
    }

    public final void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    public boolean provideLocationDetailsSelf() {
        return true;
    }

    @Override
    public String getMessage() {
        String text = "";

        String superMessage = super.getMessage();
        if (superMessage != null) text += superMessage + ": ";

        String what = (parsable != null) ? parsable.getMeta().toString() + " " : "";
        String where =
                (locationDetails == null || locationDetails.isEmpty())
                        ? "at unknown location"
                        : locationDetails;
        return text + what + where;
    }

    /** For serialization */
    @SuppressWarnings("unused")
    protected ParsableException() {}
}
