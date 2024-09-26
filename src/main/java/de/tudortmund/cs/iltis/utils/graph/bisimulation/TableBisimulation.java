package de.tudortmund.cs.iltis.utils.graph.bisimulation;

import de.tudortmund.cs.iltis.utils.collections.ListSet;
import de.tudortmund.cs.iltis.utils.collections.SerializableLabeledTable;
import de.tudortmund.cs.iltis.utils.collections.SerializablePair;
import de.tudortmund.cs.iltis.utils.collections.Tuple;
import de.tudortmund.cs.iltis.utils.collections.relations.FiniteBinaryRelation;
import de.tudortmund.cs.iltis.utils.explainedresult.ComputationInProgress;
import de.tudortmund.cs.iltis.utils.explainedresult.ComputationLog;
import de.tudortmund.cs.iltis.utils.explainedresult.ComputationState;
import de.tudortmund.cs.iltis.utils.explainedresult.ComputationSuccess;
import de.tudortmund.cs.iltis.utils.explainedresult.ExplainedResult;
import de.tudortmund.cs.iltis.utils.graph.Edge;
import de.tudortmund.cs.iltis.utils.graph.Graph;
import java.io.Serializable;
import java.util.function.BiFunction;

/**
 * This class will use a table to calculate the bisimulation.
 *
 * @param <V> The type of vertex values
 * @param <E> The type of edge values
 */
public class TableBisimulation<V extends Serializable, E> implements ComputeBisimulation<V> {

    private SerializableLabeledTable<V, V, ListSet<InsimilarityWitness<V>>> bisimulationTable;

    private Graph<V, E> rowGraph;
    private Graph<V, E> columnGraph;
    private BiFunction<V, V, Boolean> vertexComparator;
    private BiFunction<Edge<V, E>, Edge<V, E>, Boolean> edgeComparator;
    private ListSet<SerializablePair<V, V>> lastMarkedCells;

    public TableBisimulation(
            Graph<V, E> rowGraph,
            Graph<V, E> columnGraph,
            BiFunction<V, V, Boolean> vertexComparator,
            BiFunction<Edge<V, E>, Edge<V, E>, Boolean> edgeComparator) {

        this.rowGraph = rowGraph;
        this.columnGraph = columnGraph;
        this.vertexComparator = vertexComparator;
        this.edgeComparator = edgeComparator;
    }

    public FiniteBinaryRelation<V> compute() {
        this.initialize();

        do {
            this.updateTable();
        } while (!this.lastMarkedCells.isEmpty());

        return this.generateBisimulation();
    }

    public ExplainedResult<
                    FiniteBinaryRelation<V>,
                    ComputationLog<ComputationState, BisimulationResult<V>>>
            computeWithExplanation() {

        ComputationLog<ComputationState, BisimulationResult<V>> log = new ComputationLog<>();

        this.initialize();

        log.log(
                new ComputationInProgress(),
                new BisimulationResult<>(this.lastMarkedCells, this.bisimulationTable));

        do {
            this.updateTable();

            log.log(
                    new ComputationInProgress(),
                    new BisimulationResult<>(this.lastMarkedCells, this.bisimulationTable));

        } while (!this.lastMarkedCells.isEmpty());

        log.log(
                new ComputationSuccess(),
                new BisimulationResult<>(this.lastMarkedCells, this.bisimulationTable));

        FiniteBinaryRelation<V> result = this.generateBisimulation();

        return new ExplainedResult<>(result, log);
    }

    private void initialize() {
        this.bisimulationTable = new SerializableLabeledTable<>();
        this.bisimulationTable.getRowLabels().addAll(rowGraph.getVertexValues());
        this.bisimulationTable.getColumnLabels().addAll(columnGraph.getVertexValues());
        this.lastMarkedCells = new ListSet<>();

        for (V rowLabel : this.bisimulationTable.getRowLabels()) {
            for (V columnLabel : this.bisimulationTable.getColumnLabels()) {
                if (vertexComparator.apply(rowLabel, columnLabel)) {

                    this.bisimulationTable.setCell(rowLabel, columnLabel, new ListSet<>());

                } else {

                    this.bisimulationTable.setCell(
                            rowLabel,
                            columnLabel,
                            new ListSet<>(new IncompatibleVertices<>(rowLabel, columnLabel)));

                    this.lastMarkedCells.add(new SerializablePair<>(rowLabel, columnLabel));
                }
            }
        }
    }

    private void updateTable() {
        this.lastMarkedCells = new ListSet<>();

        for (V rowLabel : this.bisimulationTable.getRowLabels()) {
            for (V columnLabel : this.bisimulationTable.getColumnLabels()) {
                checkCell(rowLabel, columnLabel);
            }
        }
    }

    private void checkCell(V rowLabel, V columnLabel) {
        if (this.bisimulationTable.getCell(rowLabel, columnLabel).isEmpty()) {
            ListSet<InsimilarityWitness<V>> witnesses = new ListSet<>();

            for (Edge<V, E> rowEdge : rowGraph.getOutgoingEdges(rowLabel)) {
                boolean isBisimulatable = false;
                V rowNeighbor = rowEdge.getTargetValue();

                for (Edge<V, E> columnEdge : columnGraph.getOutgoingEdges(columnLabel)) {

                    V columnNeighbor = columnEdge.getTargetValue();

                    if (this.edgeComparator.apply(rowEdge, columnEdge)) {

                        if (this.bisimulationTable.getCell(rowNeighbor, columnNeighbor).isEmpty()) {

                            isBisimulatable = true;
                            break;
                        }
                    }
                }

                if (!isBisimulatable) {
                    witnesses.add(new IncompatibleEdge<>(rowLabel, rowNeighbor));
                }
            }

            for (Edge<V, E> columnEdge : columnGraph.getOutgoingEdges(columnLabel)) {

                boolean isBisimulatable = false;
                V columnNeighbor = columnEdge.getTargetValue();

                for (Edge<V, E> rowEdge : rowGraph.getOutgoingEdges(rowLabel)) {
                    V rowNeighbor = rowEdge.getTargetValue();

                    if (this.edgeComparator.apply(columnEdge, rowEdge)) {

                        if (this.bisimulationTable.getCell(rowNeighbor, columnNeighbor).isEmpty()) {

                            isBisimulatable = true;
                            break;
                        }
                    }
                }

                if (!isBisimulatable) {

                    witnesses.add(new IncompatibleEdge<>(columnLabel, columnNeighbor));
                }
            }

            if (!witnesses.isEmpty()) {
                this.bisimulationTable.setCell(rowLabel, columnLabel, witnesses);
                this.lastMarkedCells.add(new SerializablePair<>(rowLabel, columnLabel));
            }
        }
    }

    private FiniteBinaryRelation<V> generateBisimulation() {
        FiniteBinaryRelation<V> result = new FiniteBinaryRelation<>();

        for (V rowLabel : this.bisimulationTable.getRowLabels()) {
            for (V columnLabel : this.bisimulationTable.getColumnLabels()) {

                if (this.bisimulationTable.getCell(rowLabel, columnLabel).isEmpty()) {

                    result.add(new Tuple<>(rowLabel, columnLabel));
                }
            }
        }
        return result;
    }
}
