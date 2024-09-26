package de.tudortmund.cs.iltis.utils.io.parsable.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ListOfRequiredElements {
    private final List<RequiredElement> elements;

    public ListOfRequiredElements(List<RequiredElement> elements) {
        this.elements = elements;
    }

    public ListOfRequiredElements(RequiredElement... elements) {
        this.elements = new ArrayList<>();
        Collections.addAll(this.elements, elements);
    }

    public ListOfRequiredElements() {
        this(Collections.emptyList());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ListOfRequiredElements)) return false;
        ListOfRequiredElements other = (ListOfRequiredElements) obj;
        return Objects.equals(this.elements, other.elements);
    }
}
