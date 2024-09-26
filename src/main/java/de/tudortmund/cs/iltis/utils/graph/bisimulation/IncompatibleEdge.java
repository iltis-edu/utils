package de.tudortmund.cs.iltis.utils.graph.bisimulation;

/**
 * This class represents an edge that can not be simulated by the other graph.
 *
 * @param <V> the type of vertex values
 */
public class IncompatibleEdge<V> implements InsimilarityWitness<V> {

    // needed for serialization
    @SuppressWarnings("unused")
    private IncompatibleEdge() {}

    private V source;
    private V target;

    public IncompatibleEdge(V source, V target) {
        this.source = source;
        this.target = target;
    }

    public V getSource() {
        return this.source;
    }

    public V getTarget() {
        return this.target;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncompatibleEdge)) {
            return false;
        }

        IncompatibleEdge incompatible = (IncompatibleEdge) o;

        return this.source.equals(incompatible.getSource())
                && this.target.equals(incompatible.getTarget());
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = prime + this.source.hashCode();
        return prime * result + this.target.hashCode();
    }

    public String toString() {
        return "(" + this.source.toString() + "," + this.target.toString() + ")";
    }
}
