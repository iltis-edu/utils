package de.tudortmund.cs.iltis.utils.general.viewcopier;

import java.util.Objects;

public class CoordinatesInt {
    @ViewCopy(labels = "plane")
    private int x;

    @ViewCopy(labels = "plane")
    private int y;

    @ViewCopy private int z;

    private int hidden;

    public static CoordinatesInt coordsBasic(int x, int y, int z) {
        return new CoordinatesInt(x, y, z);
    }

    public static CoordinatesInt coordsBasic(int x, int y, int z, int hidden) {
        return new CoordinatesInt(x, y, z, hidden);
    }

    protected CoordinatesInt() {}

    public CoordinatesInt(int x, int y, int z) {
        this(x, y, z, 0);
    }

    public CoordinatesInt(int x, int y, int z, int hidden) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.hidden = hidden;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setHidden(int hidden) {
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
        CoordinatesInt that = (CoordinatesInt) o;
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
