package de.tudortmund.cs.iltis.utils.general.viewcopier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ReferenceList {
    @ViewCopy private ArrayList<Integer> values = new ArrayList<Integer>();

    public static ReferenceList rlist(Integer... values) {
        return new ReferenceList(values);
    }

    public ReferenceList() {}

    public ReferenceList(Integer... values) {
        this.values = new ArrayList<Integer>(Arrays.<Integer>asList(values));
    }

    public void addValue(Integer value) {
        this.values.add(value);
    }

    @Override
    public String toString() {
        return values.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferenceList that = (ReferenceList) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
