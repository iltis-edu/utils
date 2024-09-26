package de.tudortmund.cs.iltis.utils.graph.hashgraph;

import de.tudortmund.cs.iltis.utils.graph.Edge;
import de.tudortmund.cs.iltis.utils.graph.EmptyEdgeLabel;
import de.tudortmund.cs.iltis.utils.graph.Vertex;

public class DefaultUndirectedHashGraph<V> extends UndirectedHashGraph<V, EmptyEdgeLabel> {
    public DefaultUndirectedHashGraph() {
        super();
    }

    public DefaultUndirectedHashGraph(DefaultUndirectedHashGraph<V> right) {
        super(right);
    }

    public Edge<V, EmptyEdgeLabel> addEdge(
            Vertex<V, EmptyEdgeLabel> source, Vertex<V, EmptyEdgeLabel> target) {
        return super.addEdge(source, target, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
    }

    public Edge<V, EmptyEdgeLabel> addEdge(V sourceValue, V targetValue) {
        return super.addEdge(
                this.getVertex(sourceValue),
                this.getVertex(targetValue),
                EmptyEdgeLabel.EMPTY_EDGE_LABEL);
    }

    public boolean hasEdge(V sourceValue, V targetValue) {
        return super.hasEdge(sourceValue, targetValue, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
    }

    @Override
    public DefaultUndirectedHashGraph<V> clone() {
        return (DefaultUndirectedHashGraph<V>) super.clone();
    }
}
