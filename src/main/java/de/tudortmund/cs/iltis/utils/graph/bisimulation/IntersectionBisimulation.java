package de.tudortmund.cs.iltis.utils.graph.bisimulation;

import de.tudortmund.cs.iltis.utils.collections.Tuple;
import de.tudortmund.cs.iltis.utils.collections.relations.FiniteBinaryRelation;
import de.tudortmund.cs.iltis.utils.explainedresult.ComputationLog;
import de.tudortmund.cs.iltis.utils.explainedresult.ComputationState;
import de.tudortmund.cs.iltis.utils.explainedresult.ExplainedResult;
import de.tudortmund.cs.iltis.utils.graph.Edge;
import de.tudortmund.cs.iltis.utils.graph.Graph;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.BiFunction;

/**
 * This class calculates bisimulation as the intersection of the simulation and the inverse
 * simulation.
 *
 * @param <V> The type of vertex values
 * @param <E> The type of edge values
 */
public class IntersectionBisimulation<V extends Serializable, E> implements ComputeBisimulation<V> {

    private Graph<V, E> firstGraph;
    private Graph<V, E> secondGraph;
    private BiFunction<V, V, Boolean> vertexComparator;
    private BiFunction<Edge<V, E>, Edge<V, E>, Boolean> edgeComparator;

    public IntersectionBisimulation(
            Graph<V, E> rowGraph,
            Graph<V, E> columnGraph,
            BiFunction<V, V, Boolean> vertexComparator,
            BiFunction<Edge<V, E>, Edge<V, E>, Boolean> edgeComparator) {

        this.firstGraph = rowGraph;
        this.secondGraph = columnGraph;
        this.vertexComparator = vertexComparator;
        this.edgeComparator = edgeComparator;
    }

    public FiniteBinaryRelation<V> compute() {
        return this.maximalBisimulation();
    }

    /** The {@link ComputationLog} will be empty. */
    public ExplainedResult<
                    FiniteBinaryRelation<V>,
                    ComputationLog<ComputationState, BisimulationResult<V>>>
            computeWithExplanation() {

        return new ExplainedResult<>(this.maximalBisimulation(), new ComputationLog<>());
    }

    public FiniteBinaryRelation<V> maximalBisimulation() {

        FiniteBinaryRelation<V> simulation = this.maximalSimulation(firstGraph, secondGraph);

        FiniteBinaryRelation<V> inverseSimulation = this.maximalSimulation(secondGraph, firstGraph);

        FiniteBinaryRelation<V> bisimulation = new FiniteBinaryRelation<>();

        for (Tuple<V> tuple : simulation.getElements()) {

            Tuple<V> inverseTuple =
                    new Tuple<>(tuple.getElementAtPosition(1), tuple.getElementAtPosition(0));

            if (inverseSimulation.contains(inverseTuple)) {
                bisimulation.add(tuple);
            }
        }

        return bisimulation;
    }

    public FiniteBinaryRelation<V> maximalSimulation(
            Graph<V, E> firstGraph, Graph<V, E> secondGraph) {

        FiniteBinaryRelation<V> simulation = new FiniteBinaryRelation<>();

        // add potential elements of simulation
        for (V firstVertex : firstGraph.getVertexValues()) {
            for (V secondVertex : secondGraph.getVertexValues()) {
                if (vertexComparator.apply(firstVertex, secondVertex)) {
                    simulation.add(new Tuple<V>(firstVertex, secondVertex));
                }
            }
        }
        boolean changed = true;

        // remove elements, that do not satisfy the simulation condition
        while (changed) {
            changed = false;

            for (Iterator<Tuple<V>> iter = simulation.iterator(); iter.hasNext(); ) {
                Tuple<V> tuple = iter.next();
                V firstVertex = tuple.getElementAtPosition(0);
                V secondVertex = tuple.getElementAtPosition(1);
                boolean simulatableVertex = true;

                for (Edge<V, E> firstEdge : firstGraph.getOutgoingEdges(firstVertex)) {

                    boolean simulatableTransition = false;

                    for (Edge<V, E> secondEdge : secondGraph.getOutgoingEdges(secondVertex)) {

                        if (edgeComparator.apply(firstEdge, secondEdge)) {

                            Tuple<V> targetTuple =
                                    new Tuple<>(
                                            firstEdge.getTargetValue(),
                                            secondEdge.getTargetValue());

                            if (simulation.contains(targetTuple)) {
                                simulatableTransition = true;
                                break;
                            }
                        }
                    }

                    if (!simulatableTransition) {
                        simulatableVertex = false;
                        break;
                    }
                }

                if (!simulatableVertex) {
                    changed = true;
                    iter.remove();
                }
            }
        }

        return simulation;
    }
}
