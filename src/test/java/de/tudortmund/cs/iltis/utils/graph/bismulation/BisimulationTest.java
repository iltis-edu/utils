package de.tudortmund.cs.iltis.utils.graph.bismulation;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.collections.Tuple;
import de.tudortmund.cs.iltis.utils.collections.relations.FiniteBinaryRelation;
import de.tudortmund.cs.iltis.utils.graph.Edge;
import de.tudortmund.cs.iltis.utils.graph.Vertex;
import de.tudortmund.cs.iltis.utils.graph.bisimulation.IntersectionBisimulation;
import de.tudortmund.cs.iltis.utils.graph.hashgraph.HashGraph;
import java.util.function.BiFunction;
import org.junit.Test;

public class BisimulationTest {

    private HashGraph<Integer, Character> firstGraph;
    private HashGraph<Integer, Character> secondGraph;
    private Vertex<Integer, Character> vertex1;
    private Vertex<Integer, Character> vertex2;
    private Vertex<Integer, Character> vertex3;
    private Vertex<Integer, Character> vertex4;
    private Vertex<Integer, Character> vertex5;
    private BiFunction<Integer, Integer, Boolean> vertexComparator;
    private BiFunction<Edge<Integer, Character>, Edge<Integer, Character>, Boolean> edgeComparator;
    private Tuple<Integer> tuple14;
    private Tuple<Integer> tuple15;
    private Tuple<Integer> tuple24;
    private Tuple<Integer> tuple25;
    private Tuple<Integer> tuple34;
    private Tuple<Integer> tuple35;
    private Tuple<Integer> tuple41;
    private Tuple<Integer> tuple42;
    private Tuple<Integer> tuple43;
    private Tuple<Integer> tuple51;
    private Tuple<Integer> tuple52;
    private Tuple<Integer> tuple53;

    public BisimulationTest() {
        vertexComparator = (p, q) -> true;
        edgeComparator = (s, t) -> s.get().equals(t.get());

        firstGraph = new HashGraph<>();
        vertex1 = new Vertex<>(firstGraph, 1);
        vertex2 = new Vertex<>(firstGraph, 2);
        vertex3 = new Vertex<>(firstGraph, 3);
        firstGraph.addVertices(1, 2, 3);
        firstGraph.addEdge(1, 2, 'a');
        firstGraph.addEdge(1, 3, 'b');

        secondGraph = new HashGraph<>();
        vertex4 = new Vertex<>(secondGraph, 4);
        vertex5 = new Vertex<>(secondGraph, 5);
        secondGraph.addVertex(4);
        secondGraph.addVertex(5);
        secondGraph.addEdge(4, 5, 'a');

        tuple14 = new Tuple<>(1, 4);
        tuple15 = new Tuple<>(1, 5);
        tuple24 = new Tuple<>(2, 4);
        tuple25 = new Tuple<>(2, 5);
        tuple34 = new Tuple<>(3, 4);
        tuple35 = new Tuple<>(3, 5);
        tuple41 = new Tuple<>(4, 1);
        tuple42 = new Tuple<>(4, 2);
        tuple43 = new Tuple<>(4, 3);
        tuple51 = new Tuple<>(5, 1);
        tuple52 = new Tuple<>(5, 2);
        tuple53 = new Tuple<>(5, 3);
    }

    @Test
    public void testMaximalSimulation() {
        IntersectionBisimulation<Integer, Character> calculator =
                new IntersectionBisimulation<>(
                        firstGraph, secondGraph, vertexComparator, edgeComparator);

        FiniteBinaryRelation<Integer> bisimulation = calculator.compute();
        assertContains(bisimulation, tuple25, tuple35);
        assertContainsNot(bisimulation, tuple14, tuple15, tuple24, tuple34);

        calculator =
                new IntersectionBisimulation<>(
                        secondGraph, firstGraph, vertexComparator, edgeComparator);

        bisimulation = calculator.compute();
        assertContains(bisimulation, tuple52, tuple53);
        assertContainsNot(bisimulation, tuple41, tuple51, tuple42, tuple43);
    }

    private <T> void assertContains(FiniteBinaryRelation<T> simulation, Tuple<T>... tuples) {
        for (Tuple<T> tuple : tuples) assertTrue(simulation.contains(tuple));
    }

    private <T> void assertContainsNot(FiniteBinaryRelation<T> simulation, Tuple<T>... tuples) {
        for (Tuple<T> tuple : tuples) assertFalse(simulation.contains(tuple));
    }
}
