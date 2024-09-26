package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import java.util.Objects;

public class RequiredElement {
    private String required;
    private String optional;

    public RequiredElement(String required, String optional) {
        this.required = required;
        this.optional = optional;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof RequiredElement)) return false;
        RequiredElement other = (RequiredElement) obj;
        return Objects.equals(this.required, other.required)
                && Objects.equals(this.optional, other.optional);
    }
}
