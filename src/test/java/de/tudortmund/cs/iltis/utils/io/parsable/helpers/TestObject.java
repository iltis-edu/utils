package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TestObject {
    private final int number;
    private final String name;
    private final List<TestReference> references;
    private final RequiredElement required;
    private ListOfRequiredElements list = new ListOfRequiredElements();

    public TestObject(
            Integer number, String name, List<TestReference> references, RequiredElement required) {
        this.number = number;
        this.name = name;
        this.references = references;
        this.required = required;
    }

    public TestObject(Integer number, String name, List<TestReference> references) {
        this(number, name, references, new RequiredElement("foo", "bar"));
    }

    public TestObject(Integer number, String name) {
        this(number, name, Collections.emptyList(), new RequiredElement("foo", "bar"));
    }

    public void setList(ListOfRequiredElements list) {
        this.list = list;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof TestObject)) return false;
        TestObject other = (TestObject) obj;
        return Objects.equals(this.number, other.number)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.references, other.references)
                && Objects.equals(this.required, other.required)
                && Objects.equals(this.list, other.list);
    }
}
