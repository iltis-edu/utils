package de.tudortmund.cs.iltis.utils.io.writer.graph;

import de.tudortmund.cs.iltis.utils.graph.Edge;
import de.tudortmund.cs.iltis.utils.graph.EmptyEdgeLabel;

public class EdgeUnicodeWriter<V, E> extends EdgeWriter<V, E> {
    private boolean directed;

    public EdgeUnicodeWriter(boolean directed) {
        this.directed = directed;
    }

    public static String edgeSymbol(boolean directed) {
        return directed ? " → " : " — ";
    }

    @Override
    public String write(Edge<V, E> edge) {
        String edgeSymbol = edgeSymbol(directed);
        String label =
                edge.get() instanceof EmptyEdgeLabel
                        ? ""
                        : ", label: " + this.dataWriter.write(edge.get());
        return "("
                + super.vertexWriter.write(edge.getSource())
                + edgeSymbol
                + this.vertexWriter.write(edge.getTarget())
                + label
                + ")";
    }
}
