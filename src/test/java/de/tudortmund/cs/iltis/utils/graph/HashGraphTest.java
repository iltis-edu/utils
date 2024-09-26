package de.tudortmund.cs.iltis.utils.graph;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.graph.hashgraph.DefaultHashGraph;
import de.tudortmund.cs.iltis.utils.graph.hashgraph.DefaultUndirectedHashGraph;
import java.util.Set;
import org.junit.Test;

public class HashGraphTest {

    @Test
    public void simpleDirectedGraph() {
        DefaultHashGraph<Integer> g = new DefaultHashGraph<>();

        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);

        assertEquals(3, g.getVertices().size());
        assertTrue(g.hasVertex(1));
        assertTrue(g.hasVertex(2));
        assertTrue(g.hasVertex(3));
        assertFalse(g.hasVertex(4));
        assertEquals(3, g.getEdges().size());
        assertTrue(g.hasEdge(1, 2));
        assertTrue(g.hasEdge(1, 3));
        assertTrue(g.hasEdge(2, 3));

        assertFalse(g.hasEdge(2, 1));
        assertFalse(g.hasEdge(3, 1));
        assertFalse(g.hasEdge(3, 2));

        Set<Edge<Integer, EmptyEdgeLabel>> edges = g.getEdges();
        assertEquals(3, edges.size());

        g.removeEdge(1, 3);
        assertEquals(2, edges.size());

        g.addEdge(1, 3);
        g.removeVertex(1);
        assertEquals(2, g.getVertices().size());
        assertEquals(1, g.getEdges().size());

        System.out.println(g);
    }

    @Test
    public void simpleUndirectedGraph() {
        DefaultUndirectedHashGraph<Integer> g = new DefaultUndirectedHashGraph<>();

        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addEdge(1, 2);
        g.addEdge(1, 3);

        assertEquals(3, g.getVertices().size());
        assertTrue(g.hasVertex(1));
        assertTrue(g.hasVertex(2));
        assertTrue(g.hasVertex(3));
        assertFalse(g.hasVertex(4));
        assertEquals(4, g.getEdges().size());
        assertTrue(g.hasEdge(1, 2));
        assertTrue(g.hasEdge(1, 3));
        assertTrue(g.hasEdge(2, 1));
        assertTrue(g.hasEdge(3, 1));

        assertFalse(g.hasEdge(2, 3));
        assertFalse(g.hasEdge(3, 2));

        Set<Integer> values = g.getOutNeighborValues(1);
        assertEquals(2, values.size());
        assertTrue(values.contains(2));
        assertTrue(values.contains(3));
    }

    @Test
    public void cloneTest() {
        DefaultHashGraph<Integer> g = new DefaultHashGraph<>();

        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);

        DefaultHashGraph<Integer> h = g.clone();
        h.removeVertex(1);

        assertEquals(3, g.getVertices().size());
        assertEquals(3, g.getEdges().size());
        assertEquals(2, h.getVertices().size());
        assertEquals(1, h.getEdges().size());
    }

    @Test
    public void dfsTest() {
        DefaultHashGraph<Integer> g = new DefaultHashGraph<>();
        int i1, i2, i3, i4, i5;
        i1 = 1;
        i2 = 2;
        i3 = 3;
        i4 = 4;
        i5 = 5;

        g.addVertex(i1);
        g.addVertex(i2);
        g.addVertex(i3);
        g.addVertex(i4);
        g.addVertex(i5);
        g.addEdge(i1, i2);
        g.addEdge(i1, i3);
        g.addEdge(i2, i3);
        g.addEdge(i3, i4);

        Set<Integer> reachable = g.getReachableValues(g.getVertex(1));
        assertEquals(4, reachable.size());
        assertTrue(reachable.contains(i1));
        assertTrue(reachable.contains(i2));
        assertTrue(reachable.contains(i3));
        assertTrue(reachable.contains(i4));
    }

    @Test
    public void copyTest() {
        DefaultHashGraph<Integer> g = new DefaultHashGraph<>();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(3, 3);

        DefaultHashGraph<Integer> h = g.clone();
        assertEquals(3, h.getVertices().size());
        assertEquals(g.getVertex(1), h.getVertex(1));
        assertEquals(g.getVertex(2), h.getVertex(2));
        assertEquals(g.getVertex(3), h.getVertex(3));
        h.removeVertex(3);
        h.addVertex(4);
        h.addEdge(1, 4);

        assertTrue(g.hasVertex(3));
        assertFalse(h.hasVertex(3));
        assertEquals(3, g.getVertices().size());
        assertEquals(3, h.getVertices().size());
        assertEquals(3, g.getEdges().size());
        assertEquals(2, h.getEdges().size());

        assertTrue(g.hasEdge(1, 2));
        assertTrue(g.hasEdge(1, 3));
        assertTrue(g.hasEdge(3, 3));
        assertTrue(h.hasEdge(1, 2));
        assertTrue(h.hasEdge(1, 4));

        int w1 = 1;
        int w2 = 2;
        int w3 = 3;

        DefaultHashGraph<Integer> g1 = new DefaultHashGraph<>();
        g1.addVertex(w1);
        g1.addVertex(w2);
        g1.addVertex(w3);
    }
}
