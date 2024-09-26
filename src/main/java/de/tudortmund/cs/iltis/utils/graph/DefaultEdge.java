package de.tudortmund.cs.iltis.utils.graph;

public class DefaultEdge<V> extends Edge<V, EmptyEdgeLabel> {

    public DefaultEdge(
            Graph<V, EmptyEdgeLabel> graph,
            Vertex<V, EmptyEdgeLabel> source,
            Vertex<V, EmptyEdgeLabel> target) {
        super(graph, source, target, null);
    }

    /** For serialization */
    @SuppressWarnings("unused")
    private DefaultEdge() {}
}
