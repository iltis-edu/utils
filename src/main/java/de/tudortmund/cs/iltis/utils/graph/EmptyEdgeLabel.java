package de.tudortmund.cs.iltis.utils.graph;

import java.io.Serializable;

public class EmptyEdgeLabel implements Serializable {
    public static final EmptyEdgeLabel EMPTY_EDGE_LABEL = new EmptyEdgeLabel();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof EmptyEdgeLabel;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }
}
