package de.tudortmund.cs.iltis.utils.explainedresult;

public class ComputationFailed extends ComputationState {

    // needed for serialization
    public ComputationFailed() {}

    public String toString() {
        return "failure";
    }
}
