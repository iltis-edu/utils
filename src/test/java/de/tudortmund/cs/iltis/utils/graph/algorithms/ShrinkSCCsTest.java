package de.tudortmund.cs.iltis.utils.graph.algorithms;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.graph.Edge;
import de.tudortmund.cs.iltis.utils.graph.Graph;
import de.tudortmund.cs.iltis.utils.graph.Vertex;
import de.tudortmund.cs.iltis.utils.graph.hashgraph.HashGraph;
import java.util.Comparator;
import org.junit.Test;

public class ShrinkSCCsTest {

    @Test
    public void testShrinkSCCsWithSingleVertexInGraph() {
        Graph<String, Integer> graph = new HashGraph<>();
        graph.addVertices("v0");
        graph.addEdge("v0", "v0", 13);

        ShrinkSCCs.shrink(graph, (g, vs) -> new Vertex<>(g, "representative"));

        assertEquals(1, graph.getVertices().size());
        assertTrue(graph.hasVertex("representative"));
        assertEquals(0, graph.getEdges().size()); // self loop should have been removed
    }

    @Test
    public void testShrinkSCCsWithAllVerticesInOneSCC() {
        Graph<String, Integer> graph = new HashGraph<>();
        graph.addVertices("v0", "v1", "v2", "v3");
        graph.addEdge("v0", "v0", 4);
        graph.addEdge("v0", "v1", 0);
        graph.addEdge("v1", "v3", -3);
        graph.addEdge("v2", "v1", 1);
        graph.addEdge("v3", "v2", 1);
        graph.addEdge("v3", "v0", -7);

        ShrinkSCCs.shrink(
                graph,
                (g, vs) ->
                        vs.stream()
                                .min(Comparator.comparing(Vertex::get))
                                .get()); // "v0" is the smallest vertex

        assertEquals(1, graph.getVertices().size());
        assertTrue(graph.hasVertex("v0"));
        assertEquals(0, graph.getEdges().size()); // no edges within a single SCC are kept
    }

    @Test
    public void testShrinkSCCsWithEachVertexInSeparateSCC() {
        Graph<String, Integer> graph = new HashGraph<>();
        graph.addVertices("v0", "v1", "v2", "v3");
        graph.addEdge("v0", "v0", 0);
        graph.addEdge("v0", "v1", 0);
        graph.addEdge("v0", "v3", 0);
        graph.addEdge("v2", "v1", 0);
        graph.addEdge("v1", "v3", 0);
        graph.addEdge("v2", "v3", 0);

        ShrinkSCCs.shrink(graph, (g, vs) -> vs.stream().findFirst().get());

        assertEquals(4, graph.getVertices().size());
        assertTrue(graph.hasVertex("v0"));
        assertTrue(graph.hasVertex("v1"));
        assertTrue(graph.hasVertex("v2"));
        assertTrue(graph.hasVertex("v3"));
        assertEquals(5, graph.getEdges().size()); // only self loops are removed
        assertTrue(graph.hasEdge("v0", "v1", 0));
        assertTrue(graph.hasEdge("v0", "v3", 0));
        assertTrue(graph.hasEdge("v2", "v1", 0));
        assertTrue(graph.hasEdge("v1", "v3", 0));
        assertTrue(graph.hasEdge("v2", "v3", 0));
    }

    @Test
    public void testShrinkSCCs() {
        Graph<String, Integer> graph = new HashGraph<>();
        graph.addVertices("v0", "v1", "v2", "v3", "v4");
        graph.addEdge("v0", "v0", 0);
        graph.addEdge("v0", "v1", 3);
        graph.addEdge("v0", "v2", -2);
        graph.addEdge("v1", "v1", 3);
        graph.addEdge("v1", "v3", -1);
        graph.addEdge("v2", "v1", 1);
        graph.addEdge("v3", "v2", 1);

        ShrinkSCCs.shrink(
                graph, (g, vs) -> vs.stream().min(Comparator.comparing(Vertex::get)).get());

        assertEquals(3, graph.getVertices().size());
        assertTrue(graph.hasVertex("v0"));
        assertTrue(graph.hasVertex("v1"));
        assertTrue(graph.hasVertex("v4"));
        assertEquals(2, graph.getEdges().size());
        // IMPORTANT: do *NOT* use `graph.hasEdge()` here because there are two edges from v0 to v1
        // and it is non-deterministic
        // which one is used to verify the edge's value (3 or -2). This would lead to a randomly
        // failing/passing test case.
        assertTrue(
                graph.getEdges()
                        .contains(
                                new Edge<>(
                                        graph, graph.getVertex("v0"), graph.getVertex("v1"), 3)));
        assertTrue(
                graph.getEdges()
                        .contains(
                                new Edge<>(
                                        graph, graph.getVertex("v0"), graph.getVertex("v1"), -2)));
    }
}
