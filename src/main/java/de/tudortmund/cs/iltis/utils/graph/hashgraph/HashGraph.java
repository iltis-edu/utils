package de.tudortmund.cs.iltis.utils.graph.hashgraph;

import de.tudortmund.cs.iltis.utils.graph.Edge;
import de.tudortmund.cs.iltis.utils.graph.Graph;
import de.tudortmund.cs.iltis.utils.graph.Vertex;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;

public class HashGraph<V, E> extends Graph<V, E> {
    public HashGraph() {
        this.vertices = new HashSet<>();
        this.edges = new HashSet<>();
    }

    public HashGraph(HashGraph<V, E> right) {
        this();
        for (Vertex<V, E> vertex : right.getVertices()) this.addVertex(vertex);
        for (Edge<V, E> edge : right.getEdges()) this.addEdge(edge);
    }

    @Override
    public <NewV, NewE> HashGraph<NewV, NewE> map(
            Function<V, NewV> vertexMapping, Function<E, NewE> edgeMapping) {
        HashGraph<NewV, NewE> mappedGraph = new HashGraph<>();

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
    public HashSet<Vertex<V, E>> getVertices() {
        return this.vertices;
    }

    @Override
    public HashSet<Edge<V, E>> getEdges() {
        return this.edges;
    }

    @Override
    public HashSet<V> getVertexValues() {
        HashSet<V> values = new HashSet<>();
        for (Vertex<V, E> vertex : this.vertices) values.add(vertex.get());
        return values;
    }

    @Override
    public HashSet<E> getEdgeValues() {
        HashSet<E> values = new HashSet<>();
        for (Edge<V, E> edge : this.edges) values.add(edge.get());
        return values;
    }

    @Override
    public Vertex<V, E> getVertex(V vertexValue) {
        for (Vertex<V, E> vertex : this.vertices)
            if (vertex.get().equals(vertexValue)) return vertex;
        return null;
    }

    @Override
    public Edge<V, E> getEdge(V sourceValue, V targetValue) {
        for (Edge<V, E> edge : this.edges)
            if (edge.hasSourceValue(sourceValue) && edge.hasTargetValue(targetValue)) return edge;
        return null;
    }

    @Override
    public HashSet<Edge<V, E>> getIncomingEdges(Vertex<V, E> target) {
        HashSet<Edge<V, E>> edgesToTarget = new HashSet<>();
        for (Edge<V, E> edge : this.edges) if (edge.hasTarget(target)) edgesToTarget.add(edge);
        return edgesToTarget;
    }

    @Override
    public HashSet<Edge<V, E>> getOutgoingEdges(Vertex<V, E> source) {
        HashSet<Edge<V, E>> edgesFromSource = new HashSet<>();
        for (Edge<V, E> edge : this.edges) if (edge.hasSource(source)) edgesFromSource.add(edge);
        return edgesFromSource;
    }

    @Override
    public Vertex<V, E> addVertex(V vertexValue) {
        return addVertex(vertexValue, 0);
    }

    @Override
    public Vertex<V, E> addVertex(V vertexValue, int color) {
        Vertex<V, E> newVertex = new Vertex<>(this, vertexValue, color);
        this.vertices.add(newVertex);
        return newVertex;
    }

    @Override
    public Vertex<V, E> addVertex(Vertex<V, E> vertex) {
        this.vertices.add(vertex);
        return vertex;
    }

    @Override
    public void removeVertex(Vertex<V, E> vertex) {
        if (vertex == null) return;

        for (Edge<V, E> edge : vertex.getIncomingEdges()) this.removeEdge(edge);
        for (Edge<V, E> edge : vertex.getOutgoingEdges()) this.removeEdge(edge);
        this.vertices.remove(vertex);
    }

    @Override
    public Edge<V, E> addEdge(Vertex<V, E> source, Vertex<V, E> target, E value) {
        source = this.getVertex(source.get());
        target = this.getVertex(target.get());
        Edge<V, E> newEdge = new Edge<>(this, source, target, value);
        this.edges.add(newEdge);
        return newEdge;
    }

    @Override
    public void removeEdge(Edge<V, E> edge) {
        if (edge == null) return;

        this.edges.remove(edge);
    }

    @Override
    public HashGraph<V, E> clone() {
        return new HashGraph<>(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashGraph)) return false;
        HashGraph<?, ?> other = (HashGraph<?, ?>) o;
        return Objects.equals(this.vertices, other.vertices)
                && Objects.equals(this.edges, other.edges);
    }

    private HashSet<Vertex<V, E>> vertices;
    private HashSet<Edge<V, E>> edges;
}
