package de.tudortmund.cs.iltis.utils.general.viewcopier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CloneableList {
    @ViewCopy(mode = ViewCopier.CopyMode.CLONE)
    private ArrayList<Integer> values = new ArrayList<Integer>();

    public static CloneableList clist(Integer... values) {
        return new CloneableList(values);
    }

    public CloneableList() {}

    public CloneableList(Integer... values) {
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
        CloneableList that = (CloneableList) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
