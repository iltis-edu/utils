package de.tudortmund.cs.iltis.utils.general.viewcopier;

import java.util.Objects;

public class CoordinatesInteger {
    @ViewCopy(labels = "plane")
    private Integer x;

    @ViewCopy(labels = "plane")
    private Integer y;

    @ViewCopy private Integer z;

    private Integer hidden;

    public static CoordinatesInteger coords(Integer x, Integer y, Integer z) {
        return new CoordinatesInteger(x, y, z);
    }

    public static CoordinatesInteger coords(Integer x, Integer y, Integer z, Integer hidden) {
        return new CoordinatesInteger(x, y, z, hidden);
    }

    protected CoordinatesInteger() {}

    public CoordinatesInteger(Integer x, Integer y, Integer z) {
        this(x, y, z, null);
    }

    public CoordinatesInteger(Integer x, Integer y, Integer z, Integer hidden) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.hidden = hidden;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public Integer getHidden() {
        return this.hidden;
    }

    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return "("
                + Objects.toString(x)
                + ","
                + Objects.toString(y)
                + ","
                + Objects.toString(z)
                + ":"
                + Objects.toString(hidden)
                + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinatesInteger that = (CoordinatesInteger) o;
        return Objects.equals(x, that.x)
                && Objects.equals(y, that.y)
                && Objects.equals(z, that.z)
                && Objects.equals(hidden, that.hidden);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, hidden);
    }
}
