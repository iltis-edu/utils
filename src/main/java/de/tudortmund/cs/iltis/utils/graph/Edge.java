package de.tudortmund.cs.iltis.utils.graph;

import java.io.Serializable;
import java.util.Objects;

public class Edge<V, E> implements Serializable {

    public Edge(Graph<V, E> graph, Vertex<V, E> source, Vertex<V, E> target, E value) {
        this.graph = graph;
        this.source = source;
        this.target = target;
        this.value = value;
    }

    public Vertex<V, E> getSource() {
        return this.source;
    }

    public V getSourceValue() {
        return this.source.get();
    }

    public boolean hasSource(Vertex<V, E> vertex) {
        return this.getSource().equals(vertex);
    }

    public boolean hasSourceValue(V vertexValue) {
        return this.getSourceValue().equals(vertexValue);
    }

    public boolean isLoop() {
        return this.source.equals(target);
    }

    public Vertex<V, E> getTarget() {
        return this.target;
    }

    public V getTargetValue() {
        return this.target.get();
    }

    public boolean hasTarget(Vertex<V, E> vertex) {
        return this.getTarget().equals(vertex);
    }

    public boolean hasTargetValue(V vertexValue) {
        return this.getTargetValue().equals(vertexValue);
    }

    public E get() {
        return this.value;
    }

    protected Graph<V, E> getGraph() {
        return this.graph;
    }

    /** Two edges are considered equal even though the graphs they belong to are different. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge<?, ?> other = (Edge<?, ?>) o;
        return Objects.equals(this.source, other.source)
                && Objects.equals(this.target, other.target)
                && Objects.equals(this.value, other.value);
    }

    public Edge<V, E> clone() {
        return new Edge<>(this.graph, this.source, this.target, this.value);
    }

    public int hashCode() {
        return 513687 + 6 * this.source.hashCode() + 7 * this.target.hashCode();
    }

    public String toString() {
        return "(" + this.source.toString() + "," + this.target.toString() + ")";
    }

    private Graph<V, E> graph;
    private Vertex<V, E> source;
    private Vertex<V, E> target;
    private E value;

    /** For serialization */
    @SuppressWarnings("unused")
    protected Edge() {}
}
