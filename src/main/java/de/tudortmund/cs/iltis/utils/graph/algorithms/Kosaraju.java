package de.tudortmund.cs.iltis.utils.graph.algorithms;

import de.tudortmund.cs.iltis.utils.graph.Graph;
import de.tudortmund.cs.iltis.utils.graph.Vertex;
import java.util.*;

/**
 * This algorithm computes the SCCs (strongly connected components) of the given graph based on the
 * approach by Kosaraju.
 *
 * <p>Resources: Wikipedia: https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm Original paper: "A
 * strong-connectivity algorithm and its applications to data flow analysis" by Sharir/1981
 *
 * <p>Complexity: O(V^3) Actually, the algorithm could be linear, but `Graph.getIncomingEdges(v)`
 * and `Graph.getOutgoingEdges(v)` are implemented as filters like `edges.filter(e -> e.source ==
 * v)` which means for each vertex we iterate over up to O(V^2) edges.
 */
public class Kosaraju {

    /**
     * Computes the SCCs of the given graph
     *
     * <p>The returned map contains a one representative for each SCC as a key and all vertices of
     * that SCC as a value, including the representative itself. The representative is random and
     * you should not rely on a specific representative being chosen.
     *
     * @param graph the graph of which to compute the SCCs
     * @param <V> the type of data associated with each vertex
     * @param <E> the type of data associated with each edge
     * @return a map mapping an (arbitrary) representative of each SCCs to a set of all vertices in
     *     that SCC
     */
    public static <V, E> Map<Vertex<V, E>, Set<Vertex<V, E>>> computeStronglyConnectedComponents(
            Graph<V, E> graph) {
        LinkedList<Vertex<V, E>> list = new LinkedList<>();
        Set<Vertex<V, E>> visited = new HashSet<>();
        Set<Vertex<V, E>> assigned = new HashSet<>();
        Map<Vertex<V, E>, Set<Vertex<V, E>>> mapping = new HashMap<>();
        for (Vertex<V, E> vertex : graph.getVertices()) {
            visit(vertex, list, visited);
        }
        for (Vertex<V, E> vertex : list) {
            assign(vertex, vertex, mapping, assigned);
        }
        return mapping;
    }

    private static <V, E> void visit(
            Vertex<V, E> current, LinkedList<Vertex<V, E>> list, Set<Vertex<V, E>> visited) {
        if (!visited.contains(current)) {
            visited.add(current);
            for (Vertex<V, E> outNeighbour : current.getOutNeighbors()) {
                visit(outNeighbour, list, visited);
            }
            list.addFirst(current);
        }
    }

    private static <V, E> void assign(
            Vertex<V, E> vertex,
            Vertex<V, E> root,
            Map<Vertex<V, E>, Set<Vertex<V, E>>> mapping,
            Set<Vertex<V, E>> assigned) {
        if (!assigned.contains(vertex)) {
            assigned.add(vertex);
            Set<Vertex<V, E>> scc = mapping.getOrDefault(root, new HashSet<>());
            scc.add(vertex);
            mapping.put(root, scc);
            for (Vertex<V, E> inNeighbour : vertex.getInNeighbors()) {
                assign(inNeighbour, root, mapping, assigned);
            }
        }
    }
}
