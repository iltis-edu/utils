package de.tudortmund.cs.iltis.utils.graph.bisimulation;

import de.tudortmund.cs.iltis.utils.collections.ListSet;
import de.tudortmund.cs.iltis.utils.collections.SerializableLabeledTable;
import de.tudortmund.cs.iltis.utils.collections.SerializablePair;
import java.io.Serializable;

/**
 * This class contains the result of one calculation step. {@code bisimulationTable} is the
 * resulting table and {@code changedCells} contains all cells that have been marked in this step.
 *
 * @param <V> The type of vertex values
 */
public class BisimulationResult<V extends Serializable> implements Serializable {

    // needed for serialization
    private BisimulationResult() {}

    private ListSet<SerializablePair<V, V>> changedCells;

    private SerializableLabeledTable<V, V, ListSet<InsimilarityWitness<V>>> bisimulationTable;

    public BisimulationResult(
            ListSet<SerializablePair<V, V>> changedCells,
            SerializableLabeledTable<V, V, ListSet<InsimilarityWitness<V>>> table) {

        this.changedCells = changedCells;
        this.bisimulationTable = table;
    }

    public ListSet<SerializablePair<V, V>> getChangedCells() {
        return this.changedCells;
    }

    public SerializableLabeledTable<V, V, ListSet<InsimilarityWitness<V>>> getBisimulationTable() {

        return this.bisimulationTable;
    }
}
