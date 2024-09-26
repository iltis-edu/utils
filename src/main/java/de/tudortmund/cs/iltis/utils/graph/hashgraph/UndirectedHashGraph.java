package de.tudortmund.cs.iltis.utils.graph.hashgraph;

import de.tudortmund.cs.iltis.utils.graph.Edge;
import de.tudortmund.cs.iltis.utils.graph.Vertex;
import java.util.function.Function;

public class UndirectedHashGraph<V, E> extends HashGraph<V, E> {
    public UndirectedHashGraph() {
        super();
        directed = false;
    }

    public UndirectedHashGraph(UndirectedHashGraph<V, E> right) {
        super(right);
        directed = false;
    }

    @Override
    public <NewV, NewE> UndirectedHashGraph<NewV, NewE> map(
            Function<V, NewV> vertexMapping, Function<E, NewE> edgeMapping) {
        UndirectedHashGraph<NewV, NewE> mappedGraph = new UndirectedHashGraph<>();

        for (Vertex<V, E> oldVertex : this.getVertices()) {
            mappedGraph.addVertex(vertexMapping.apply(oldVertex.get()), oldVertex.getColor());
        }
        for (Edge<V, E> oldEdge : this.getEdges()) {
            mappedGraph.addEdge(
                    vertexMapping.apply(oldEdge.getSourceValue()),
                    vertexMapping.apply(oldEdge.getTargetValue()),
                    edgeMapping.apply(oldEdge.get()));
        }

        return mappedGraph;
    }

    @Override
    public Edge<V, E> addEdge(Vertex<V, E> source, Vertex<V, E> target, E value) {
        super.addEdge(target, source, value);
        return super.addEdge(source, target, value);
    }

    @Override
    public void removeEdge(Edge<V, E> edge) {
        super.removeEdge(edge);
        super.removeEdge(new Edge<>(this, edge.getTarget(), edge.getSource(), edge.get()));
    }

    @Override
    public UndirectedHashGraph<V, E> clone() {
        return new UndirectedHashGraph<>(this);
    }
}
