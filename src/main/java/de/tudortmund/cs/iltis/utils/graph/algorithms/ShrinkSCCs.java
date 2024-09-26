package de.tudortmund.cs.iltis.utils.graph.algorithms;

import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.graph.Edge;
import de.tudortmund.cs.iltis.utils.graph.Graph;
import de.tudortmund.cs.iltis.utils.graph.Vertex;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A simple algorithm which shrinks all vertices of each SCC down to a single (chosen or newly
 * created) vertex.
 *
 * <p>Complexity: O(V^3) The complexity stems from the complexity of {@link Kosaraju} and the fact
 * that `Graph.getIncomingEdges(v)` and `graph.getOutgoingEdges(v)` are implemented as filters on
 * the set of edges.
 */
public class ShrinkSCCs {

    /**
     * Shrinks the SCCs of the graph and removes/add/adjusts edges accordingly.
     *
     * <p><b>Note:</b> Be careful when working with graphs with labeled edges because there are two
     * possible pitfalls caused by the way `Graph` and its subclasses work.<br>
     * 1) it is possible that multiple edges between the same vertices are created. If they have the
     * *same* value, only one such edge is preserved, the others are silently discarded<br>
     * 2) if multiple edges are created between two vertices with *different* values, some utility
     * functions on `Graph` no longer work as expected, e.g. `Graph.hasEdge()` may randomly return
     * `true` and `false` for an existing edge.
     *
     * @param graph the graph in which SCCs are shrunk
     * @param pickRepresentative a call-back for the user to either pick the representative for each
     *     SCC or provide a new vertex as representative
     * @param <V> the type of data stored on vertices
     * @param <E> the type of data stored on edges
     */
    public static <V, E> void shrink(
            Graph<V, E> graph,
            SerializableBiFunction<Graph<V, E>, Set<Vertex<V, E>>, Vertex<V, E>>
                    pickRepresentative) {
        removeSelfLoops(graph);

        Collection<Set<Vertex<V, E>>> sccs =
                Kosaraju.computeStronglyConnectedComponents(graph).values();

        for (Set<Vertex<V, E>> scc : sccs) {
            Vertex<V, E> representative = pickRepresentative.apply(graph, scc);
            scc.remove(representative); // if `representative` is a new vertex this is no-op
            graph.addVertex(
                    representative); // if `representative` is an existing vertex this is a no-op

            for (Vertex<V, E> member : scc) {
                for (Edge<V, E> outEdge : graph.getOutgoingEdges(member))
                    if (!outEdge.hasTarget(representative)) // don't create self loops
                    graph.addEdge(representative, outEdge.getTarget(), outEdge.get());
                for (Edge<V, E> inEdge : graph.getIncomingEdges(member))
                    if (!inEdge.hasSource(representative)) // don't create self loops
                    graph.addEdge(inEdge.getSource(), representative, inEdge.get());
                graph.removeVertex(member);
            }
        }
    }

    public static <V, E> void shrinkByValues(
            Graph<V, E> graph, SerializableBiFunction<Graph<V, E>, Set<V>, V> pickRepresentative) {
        SerializableBiFunction<Graph<V, E>, Set<Vertex<V, E>>, Vertex<V, E>> pickRepresentative2 =
                (g, vs) -> {
                    Set<V> vertexValues = vs.stream().map(Vertex::get).collect(Collectors.toSet());
                    V representative = pickRepresentative.apply(g, vertexValues);
                    return new Vertex<>(g, representative);
                };
        shrink(graph, pickRepresentative2);
    }

    private static <V, E> void removeSelfLoops(Graph<V, E> graph) {
        for (Vertex<V, E> vertex : graph.getVertices()) {
            graph.removeEdge(vertex.get(), vertex.get());
        }
    }
}
