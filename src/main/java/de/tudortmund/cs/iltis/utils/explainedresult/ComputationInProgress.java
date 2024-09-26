package de.tudortmund.cs.iltis.utils.explainedresult;

public class ComputationInProgress extends ComputationState {

    // needed for serialization
    public ComputationInProgress() {}

    public String toString() {
        return "in progress";
    }
}
