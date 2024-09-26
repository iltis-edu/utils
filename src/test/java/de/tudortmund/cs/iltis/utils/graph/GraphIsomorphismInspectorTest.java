package de.tudortmund.cs.iltis.utils.graph;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.graph.hashgraph.UndirectedHashGraph;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.junit.Ignore;
import org.junit.Test;

/**
 * TODO: Do not ignore tests anymore when utils is split into server and client code. Currently, the
 * tests fail because they use the WebLibAdapter, which is built upon Java 11 Code, which is not
 * usable within client code.
 */
@Ignore
public class GraphIsomorphismInspectorTest {

    private IndexedSymbol v1 = new IndexedSymbol("v1");
    private IndexedSymbol v2 = new IndexedSymbol("v2");
    private IndexedSymbol v3 = new IndexedSymbol("v3");
    private IndexedSymbol v4 = new IndexedSymbol("v4");
    private IndexedSymbol v5 = new IndexedSymbol("v5");
    private IndexedSymbol v6 = new IndexedSymbol("v6");

    @Test
    public void testCanonicalLabelling() {
        UndirectedHashGraph<IndexedSymbol, EmptyEdgeLabel> graph = new UndirectedHashGraph<>();

        graph.addVertices(v1, v2, v3);

        graph.addEdge(v1, v2, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph.addEdge(v2, v3, EmptyEdgeLabel.EMPTY_EDGE_LABEL);

        Map<IndexedSymbol, Integer> mapping =
                GraphIsomorphismInspector.getCanonicalLabelling(graph);

        boolean firstPossibility =
                (Objects.equals(0, mapping.get(v1)))
                        && (Objects.equals(2, mapping.get(v2)))
                        && (Objects.equals(1, mapping.get(v3)));

        boolean secondPossibility =
                (Objects.equals(1, mapping.get(v1)))
                        && (Objects.equals(2, mapping.get(v2)))
                        && (Objects.equals(0, mapping.get(v3)));

        assertTrue(firstPossibility || secondPossibility);
    }

    @Test
    public void testLabelCanonically() {
        UndirectedHashGraph<IndexedSymbol, EmptyEdgeLabel> graph = new UndirectedHashGraph<>();

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        graph.addEdge(v1, v2, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph.addEdge(v2, v3, EmptyEdgeLabel.EMPTY_EDGE_LABEL);

        UndirectedHashGraph<Integer, EmptyEdgeLabel> targetGraph = new UndirectedHashGraph<>();

        targetGraph.addVertices(0, 1, 2);

        targetGraph.addEdge(0, 2, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        targetGraph.addEdge(2, 1, EmptyEdgeLabel.EMPTY_EDGE_LABEL);

        Graph<Integer, EmptyEdgeLabel> labelledGraph =
                GraphIsomorphismInspector.getCanonicallyLabelledGraph(graph);

        assertEquals(targetGraph, labelledGraph);
    }

    @Test
    public void testIsIsomorphicTo() {
        UndirectedHashGraph<IndexedSymbol, EmptyEdgeLabel> graph1 = new UndirectedHashGraph<>();

        graph1.addVertices(v1, v2, v3, v4, v5, v6);

        graph1.addEdge(v1, v2, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v2, v3, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v3, v1, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v3, v4, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v4, v5, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v5, v6, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v4, v6, EmptyEdgeLabel.EMPTY_EDGE_LABEL);

        UndirectedHashGraph<String, EmptyEdgeLabel> graph2 = new UndirectedHashGraph<>();

        // graph2 is the same graph as graph1 but with strings and every vertex + 1
        graph2.addVertices("v1", "v2", "v3", "v4", "v5", "v6");

        graph2.addEdge("v2", "v3", EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge("v3", "v4", EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge("v4", "v2", EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge("v4", "v5", EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge("v5", "v6", EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge("v6", "v1", EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge("v5", "v1", EmptyEdgeLabel.EMPTY_EDGE_LABEL);

        assertTrue(GraphIsomorphismInspector.isIsomorphicTo(graph1, graph2));
    }

    /**
     * This test case is taken from the survey paper <a
     * href="https://dl.acm.org/doi/10.1145/3372123">here</a> which shows two non-isomorphic graphs
     * which cannot be decided by normal color refinement.
     */
    @Test
    public void testIsNotIsomorphicTo() {
        UndirectedHashGraph<IndexedSymbol, EmptyEdgeLabel> graph1 = new UndirectedHashGraph<>();

        graph1.addVertices(v1, v2, v3, v4, v5, v6);

        graph1.addEdge(v1, v2, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v2, v3, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v3, v1, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v3, v4, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v4, v5, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v5, v6, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v4, v6, EmptyEdgeLabel.EMPTY_EDGE_LABEL);

        UndirectedHashGraph<IndexedSymbol, EmptyEdgeLabel> graph2 = new UndirectedHashGraph<>();

        graph2.addVertices(v1, v2, v3, v4, v5, v6);

        graph2.addEdge(v1, v2, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge(v2, v3, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge(v3, v6, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge(v6, v5, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge(v5, v4, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge(v4, v1, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge(v3, v4, EmptyEdgeLabel.EMPTY_EDGE_LABEL);

        assertFalse(GraphIsomorphismInspector.isIsomorphicTo(graph1, graph2));
    }

    @Test
    public void testAutomorphismGenerators() {
        // This graph is a line, so there should be exactly one automorphism
        UndirectedHashGraph<IndexedSymbol, EmptyEdgeLabel> graph = new UndirectedHashGraph<>();

        graph.addVertices(v1, v2, v3);

        graph.addEdge(v1, v2, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph.addEdge(v2, v3, EmptyEdgeLabel.EMPTY_EDGE_LABEL);

        Set<Map<IndexedSymbol, IndexedSymbol>> automorphismGenerators =
                GraphIsomorphismInspector.getAutomorphismGenerators(graph);

        Map<IndexedSymbol, IndexedSymbol> automorphism = new HashMap<>();
        automorphism.put(v1, v3);
        automorphism.put(v3, v1);
        automorphism.put(v2, v2);

        assertEquals(1, automorphismGenerators.size());
        assertTrue(automorphismGenerators.contains(automorphism));
    }

    @Test
    public void testColoring() {
        // This graph is a line, but one end is colored differently
        UndirectedHashGraph<IndexedSymbol, EmptyEdgeLabel> graph = new UndirectedHashGraph<>();

        graph.addVertex(v1, 1);
        graph.addVertices(v2, v3);

        graph.addEdge(v1, v2, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph.addEdge(v2, v3, EmptyEdgeLabel.EMPTY_EDGE_LABEL);

        Set<Map<IndexedSymbol, IndexedSymbol>> automorphismGenerators =
                GraphIsomorphismInspector.getAutomorphismGenerators(graph);

        assertEquals(0, automorphismGenerators.size());
    }

    @Test
    public void testColoringNotIsomorphic() {
        // This graph is a line, so there should be exactly one automorphism
        UndirectedHashGraph<IndexedSymbol, EmptyEdgeLabel> graph1 = new UndirectedHashGraph<>();

        graph1.addVertices(v1, v2, v3);

        graph1.addEdge(v1, v2, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph1.addEdge(v2, v3, EmptyEdgeLabel.EMPTY_EDGE_LABEL);

        // This graph is a line, but one end is colored differently
        UndirectedHashGraph<IndexedSymbol, EmptyEdgeLabel> graph2 = new UndirectedHashGraph<>();

        graph2.addVertex(v1, 1);
        graph2.addVertices(v2, v3);

        graph2.addEdge(v1, v2, EmptyEdgeLabel.EMPTY_EDGE_LABEL);
        graph2.addEdge(v2, v3, EmptyEdgeLabel.EMPTY_EDGE_LABEL);

        assertFalse(GraphIsomorphismInspector.isIsomorphicTo(graph1, graph2));
    }
}
