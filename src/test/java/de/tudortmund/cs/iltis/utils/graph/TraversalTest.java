package de.tudortmund.cs.iltis.utils.graph;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.collections.Pair;
import de.tudortmund.cs.iltis.utils.graph.hashgraph.DefaultHashGraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Test;

public class TraversalTest {
    /*
     _____________
    |            |
    1 --> 2 --> 3
    | \   |   /
    V   \ V /
    4     5 <-- 6
    |___________|
    missing directions are 4 -> 6, 3 -> 1, 5 -> 1, 3 -> 5
    and there's also a loop on the 4 and the 5
     */
    private DefaultHashGraph<Integer> graph;

    public TraversalTest() {
        graph = new DefaultHashGraph<>();

        graph.addVertices(1, 2, 3, 4, 5, 6);

        graph.addEdge(1, 2);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);
        graph.addEdge(2, 5);
        graph.addEdge(3, 1);
        graph.addEdge(3, 5);
        graph.addEdge(4, 6);
        graph.addEdge(4, 4);
        graph.addEdge(5, 1);
        graph.addEdge(5, 5);
    }

    @Test
    public void testBFS() {
        Map<Integer, Pair<Integer, EmptyEdgeLabel>> expectedBfsTree = new HashMap<>();
        expectedBfsTree.put(2, new Pair<>(1, new EmptyEdgeLabel()));
        expectedBfsTree.put(3, new Pair<>(2, new EmptyEdgeLabel()));
        expectedBfsTree.put(4, new Pair<>(1, new EmptyEdgeLabel()));
        expectedBfsTree.put(5, new Pair<>(2, new EmptyEdgeLabel()));
        expectedBfsTree.put(6, new Pair<>(4, new EmptyEdgeLabel()));

        assertEquals(expectedBfsTree, graph.breadthFirstTraversal(1));
    }

    @Test
    public void testTopoSort() {
        assertFalse(graph.isDirectedAcyclic());

        DefaultHashGraph<Integer> graph2 = graph.clone();
        // break cycles
        graph2.removeEdge(1, 2);
        graph2.removeEdge(4, 4);
        graph2.removeEdge(5, 5);
        graph2.removeEdge(4, 6);

        // make graph more interesting by making sure DFS first discovers the subgraph {2,3,5} and
        // later notices that 6 is also part of the component
        graph2.removeEdge(6, 2);
        graph2.addEdge(6, 2);

        Optional<List<Integer>> maybeOrder = graph2.getTopologicalOrdering();

        assertTrue(maybeOrder.isPresent());

        List<Integer> ordering = maybeOrder.get();

        for (Edge<Integer, EmptyEdgeLabel> edge : graph2.getEdges()) {
            assertTrue(
                    ordering.indexOf(edge.getSourceValue())
                            < ordering.indexOf(edge.getTargetValue()));
        }
    }

    @Test
    public void testDFSLabeling() {
        // ... amazing
        graph.depthFirstTraversal(
                1,
                v -> {},
                (e, n) -> {
                    switch (e.getSourceValue()) {
                        case 1:
                            switch (e.getTargetValue()) {
                                case 2:
                                case 4:
                                    assertEquals(n, Graph.EdgeType.TREE);
                                    break;
                                default:
                                    fail();
                            }
                            break;
                        case 2:
                            switch (e.getTargetValue()) {
                                case 3:
                                    assertEquals(n, Graph.EdgeType.TREE);
                                    break;
                                case 5:
                                    assertEquals(n, Graph.EdgeType.FORWARD);
                                    break;
                                default:
                                    fail();
                            }
                            break;
                        case 3:
                            switch (e.getTargetValue()) {
                                case 1:
                                    assertEquals(n, Graph.EdgeType.BACK);
                                    break;
                                case 5:
                                    assertEquals(n, Graph.EdgeType.TREE);
                                    break;
                                default:
                                    fail();
                            }
                            break;
                        case 4:
                            switch (e.getTargetValue()) {
                                case 4:
                                    assertEquals(n, Graph.EdgeType.BACK);
                                    break;
                                case 6:
                                    assertEquals(n, Graph.EdgeType.TREE);
                                    break;
                                default:
                                    fail();
                            }
                            break;
                        case 5:
                            switch (e.getTargetValue()) {
                                case 1:
                                case 5:
                                    assertEquals(n, Graph.EdgeType.BACK);
                                    break;
                                default:
                                    fail();
                            }
                            break;
                        case 6:
                            switch (e.getTargetValue()) {
                                case 5:
                                    assertEquals(n, Graph.EdgeType.CROSS);
                                    break;
                                default:
                                    fail();
                            }
                            break;
                        default:
                            fail();
                    }
                });
    }
}
