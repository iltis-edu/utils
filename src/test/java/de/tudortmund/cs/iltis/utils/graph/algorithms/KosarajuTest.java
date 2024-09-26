package de.tudortmund.cs.iltis.utils.graph.algorithms;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.graph.Graph;
import de.tudortmund.cs.iltis.utils.graph.Vertex;
import de.tudortmund.cs.iltis.utils.graph.hashgraph.HashGraph;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;

public class KosarajuTest {

    @Test
    public void testStronglyConnectedComponents() {
        Graph<String, Void> graph = new HashGraph<>();
        graph.addVertices("P", "Q", "R", "S", "T");
        graph.addEdge("S", "Q", null);
        graph.addEdge("T", "Q", null);
        graph.addEdge("P", "R", null);
        graph.addEdge("R", "P", null);
        graph.addEdge("R", "R", null);
        graph.addEdge("R", "S", null);

        Map<Vertex<String, Void>, Set<Vertex<String, Void>>> mapping =
                Kosaraju.computeStronglyConnectedComponents(graph);
        HashSet<Vertex<String, Void>> group1 = new HashSet<>();
        group1.add(graph.getVertex("S"));

        HashSet<Vertex<String, Void>> group2 = new HashSet<>();
        group2.add(graph.getVertex("T"));

        HashSet<Vertex<String, Void>> group3 = new HashSet<>();
        group3.add(graph.getVertex("Q"));

        HashSet<Vertex<String, Void>> group4 = new HashSet<>();
        group4.add(graph.getVertex("P"));
        group4.add(graph.getVertex("R"));

        assertEquals(4, mapping.size());
        assertTrue(mapping.containsValue(group1));
        assertTrue(mapping.containsValue(group2));
        assertTrue(mapping.containsValue(group3));
        assertTrue(mapping.containsValue(group4));

        for (Map.Entry<Vertex<String, Void>, Set<Vertex<String, Void>>> entry :
                mapping.entrySet()) {
            String representative = entry.getKey().get();
            Set<String> members =
                    entry.getValue().stream().map(Vertex::get).collect(Collectors.toSet());
            assertTrue(members.contains(representative));
        }
    }
}
