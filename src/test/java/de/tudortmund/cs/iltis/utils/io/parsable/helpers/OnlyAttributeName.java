package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import java.util.Objects;

public class OnlyAttributeName {
    private final String name;

    public OnlyAttributeName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof OnlyAttributeName)) return false;
        OnlyAttributeName other = (OnlyAttributeName) obj;
        return Objects.equals(this.name, other.name);
    }
}
