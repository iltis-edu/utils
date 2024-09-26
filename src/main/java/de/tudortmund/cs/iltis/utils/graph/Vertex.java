package de.tudortmund.cs.iltis.utils.graph;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Vertex<V, E> implements Serializable {

    public Vertex(Graph<V, E> graph, V value) {
        this(graph, value, 0);
    }

    public Vertex(Graph<V, E> graph, V value, int color) {
        this.graph = graph;
        this.value = value;
        this.color = color;
    }

    public V get() {
        return this.value;
    }

    public void set(V value) {
        this.value = value;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Set<Edge<V, E>> getIncomingEdges() {
        return this.graph.getIncomingEdges(this);
    }

    public Set<Edge<V, E>> getOutgoingEdges() {
        return this.graph.getOutgoingEdges(this);
    }

    public int getInDegree() {
        return this.graph.getInDegreeOf(this);
    }

    public int getOutDegree() {
        return this.graph.getOutDegreeOf(this);
    }

    public boolean hasInNeighbors() {
        return this.getInDegree() > 0;
    }

    public boolean hasOutNeighbors() {
        return this.getOutDegree() > 0;
    }

    public Set<Vertex<V, E>> getInNeighbors() {
        return this.graph.getInNeighbors(this);
    }

    public Set<Vertex<V, E>> getOutNeighbors() {
        return this.graph.getOutNeighbors(this);
    }

    public void addIncomingEdge(Vertex<V, E> source, E value) {
        this.graph.addEdge(source, this, value);
    }

    public void addOutgoingEdge(Vertex<V, E> target, E value) {
        this.graph.addEdge(this, target, value);
    }

    protected Graph<V, E> getGraph() {
        return this.graph;
    }

    /**
     * Two vertices are considered equal even though the graphs they belong to are different. The
     * colors won't be considered either to avoid having the same vertex in a graph twice with
     * different colors.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;
        Vertex<?, ?> other = (Vertex<?, ?>) o;
        return Objects.equals(this.value, other.value);
    }

    public Vertex<V, E> clone() {
        return new Vertex<>(this.graph, this.value);
    }

    @Override
    public int hashCode() {
        return 5851 + 3 * this.value.hashCode();
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    private Graph<V, E> graph;
    private V value;
    private int color;

    /** For serialization */
    @SuppressWarnings("unused")
    private Vertex() {}
}
