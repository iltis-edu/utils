package de.tudortmund.cs.iltis.utils.graph.bisimulation;

/**
 * This class represents two incompatible vertices.
 *
 * @param <V> The type of vertex values
 */
public class IncompatibleVertices<V> implements InsimilarityWitness<V> {

    private V firstVertex;
    private V secondVertex;

    // needed for serialization
    @SuppressWarnings("unused")
    private IncompatibleVertices() {}

    public IncompatibleVertices(V first, V second) {
        this.firstVertex = first;
        this.secondVertex = second;
    }

    public V getFirstVertex() {
        return this.firstVertex;
    }

    public V getSecondVertex() {
        return this.secondVertex;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncompatibleVertices)) {
            return false;
        }

        IncompatibleVertices vertices = (IncompatibleVertices) o;

        return this.getFirstVertex().equals(vertices.getFirstVertex())
                && this.getSecondVertex().equals(vertices.getSecondVertex());
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = prime + this.firstVertex.hashCode();
        return prime * result + this.secondVertex.hashCode();
    }

    public String toString() {
        return "("
                + this.getFirstVertex().toString()
                + ","
                + this.getSecondVertex().toString()
                + ")";
    }
}
