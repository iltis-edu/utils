package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import java.util.Objects;
import javax.xml.bind.annotation.XmlValue;

public class TestReference {
    @XmlValue private final String name;
    private final String tag;

    public TestReference(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return (tag == null) ? name : name + " (" + tag + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof TestReference)) return false;
        TestReference other = (TestReference) obj;
        return Objects.equals(this.name, other.name) && Objects.equals(this.tag, other.tag);
    }
}
