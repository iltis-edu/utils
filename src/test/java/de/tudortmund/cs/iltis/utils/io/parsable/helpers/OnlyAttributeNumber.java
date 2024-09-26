package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import java.util.Objects;

public class OnlyAttributeNumber {
    private final int number;

    public OnlyAttributeNumber(Integer number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof OnlyAttributeNumber)) return false;
        OnlyAttributeNumber other = (OnlyAttributeNumber) obj;
        return Objects.equals(this.number, other.number);
    }
}
