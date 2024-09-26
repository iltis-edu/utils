package de.tudortmund.cs.iltis.utils.graph;

import de.tudortmund.cs.iltis.utils.collections.ListSet;
import de.tudortmund.cs.iltis.utils.collections.Pair;
import de.tudortmund.cs.iltis.utils.general.Data;
import de.tudortmund.cs.iltis.utils.io.writer.collections.SetWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;
import de.tudortmund.cs.iltis.utils.io.writer.graph.EdgeWriter;
import de.tudortmund.cs.iltis.utils.io.writer.graph.VertexWriter;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Graph<V, E> implements Serializable {

    protected boolean directed = true;
    protected transient SetWriter<Vertex<V, E>> vertexWriter;
    protected transient SetWriter<Edge<V, E>> edgeWriter;

    public Graph() {
        this.vertexWriter = new SetWriter<>(new VertexWriter<>());
        this.edgeWriter = new SetWriter<>(new EdgeWriter<>());
    }

    public abstract <NewV, NewE> Graph<NewV, NewE> map(
            Function<V, NewV> vertexMapping, Function<E, NewE> edgeMapping);

    public abstract Set<Vertex<V, E>> getVertices();

    public abstract Set<V> getVertexValues();

    public abstract Set<Edge<V, E>> getEdges();

    public abstract Set<E> getEdgeValues();

    public boolean hasVertex(V vertexValue) {
        return getVertex(vertexValue) != null;
    }

    public boolean hasEdge(V sourceValue, V targetValue) {
        return getEdge(sourceValue, targetValue) != null;
    }

    public boolean hasEdge(V sourceValue, V targetValue, E label) {
        Edge<V, E> edge = getEdge(sourceValue, targetValue);
        return edge != null && edge.get().equals(label);
    }

    public abstract Vertex<V, E> getVertex(V vertexValue);

    public abstract Edge<V, E> getEdge(V sourceValue, V targetValue);

    public abstract Set<Edge<V, E>> getIncomingEdges(Vertex<V, E> vertex);

    public Set<Edge<V, E>> getIncomingEdges(V vertexValue) {
        return this.getIncomingEdges(this.getVertex(vertexValue));
    }

    public int getInDegreeOf(Vertex<V, E> vertex) {
        return this.getIncomingEdges(vertex).size();
    }

    public int getInDegreeOf(V vertexValue) {
        return this.getInDegreeOf(this.getVertex(vertexValue));
    }

    public abstract Set<Edge<V, E>> getOutgoingEdges(Vertex<V, E> vertex);

    public Set<Edge<V, E>> getOutgoingEdges(V vertexValue) {
        return this.getOutgoingEdges(this.getVertex(vertexValue));
    }

    public int getOutDegreeOf(Vertex<V, E> vertex) {
        return this.getOutgoingEdges(vertex).size();
    }

    public int getOutDegreeOf(V vertexValue) {
        return this.getOutDegreeOf(this.getVertex(vertexValue));
    }

    public Set<Vertex<V, E>> getInNeighbors(Vertex<V, E> vertex) {
        if (vertex == null) return new HashSet<>();
        Set<Edge<V, E>> inEdges = this.getIncomingEdges(vertex);
        Set<Vertex<V, E>> inNeighbors = new HashSet<>();
        for (Edge<V, E> edge : inEdges) inNeighbors.add(edge.getSource());
        return inNeighbors;
    }

    public Set<Vertex<V, E>> getInNeighbors(V vertexValue) {
        return this.getInNeighbors(this.getVertex(vertexValue));
    }

    public Set<V> getInNeighborValues(Vertex<V, E> vertex) {
        if (vertex == null) return new HashSet<>();
        Set<V> inNeighborValues = new HashSet<>();
        for (Vertex<V, E> neighbor : this.getInNeighbors(vertex))
            inNeighborValues.add(neighbor.get());
        return inNeighborValues;
    }

    public Set<V> getInNeighborValues(V vertexValue) {
        return this.getInNeighborValues(this.getVertex(vertexValue));
    }

    public Set<Vertex<V, E>> getOutNeighbors(Vertex<V, E> vertex) {
        if (vertex == null) return new HashSet<>();
        Set<Edge<V, E>> inEdges = this.getOutgoingEdges(vertex);
        Set<Vertex<V, E>> inNeighbors = new HashSet<>();
        for (Edge<V, E> edge : inEdges) inNeighbors.add(edge.getTarget());
        return inNeighbors;
    }

    public Set<Vertex<V, E>> getOutNeighbors(V vertexValue) {
        return this.getOutNeighbors(this.getVertex(vertexValue));
    }

    public Set<V> getOutNeighborValues(Vertex<V, E> vertex) {
        if (vertex == null) return new HashSet<>();
        Set<V> inNeighborValues = new HashSet<>();
        for (Vertex<V, E> neighbor : this.getOutNeighbors(vertex))
            inNeighborValues.add(neighbor.get());
        return inNeighborValues;
    }

    public Set<V> getOutNeighborValues(V vertexValue) {
        return this.getOutNeighborValues(this.getVertex(vertexValue));
    }

    public abstract Vertex<V, E> addVertex(V vertexValue);

    public abstract Vertex<V, E> addVertex(V vertexValue, int color);

    public abstract Vertex<V, E> addVertex(Vertex<V, E> vertex);

    public List<Vertex<V, E>> addVertices(V... vertexValues) {
        List<Vertex<V, E>> vertices = new ArrayList<>();
        for (V vertexValue : vertexValues) vertices.add(this.addVertex(vertexValue));
        return vertices;
    }

    public List<Vertex<V, E>> addVertices(Iterable<V> vertexValues) {
        List<Vertex<V, E>> vertices = new ArrayList<>();
        for (V vertexValue : vertexValues) vertices.add(this.addVertex(vertexValue));
        return vertices;
    }

    public abstract void removeVertex(Vertex<V, E> vertex);

    public void removeVertex(V vertexValue) {
        this.removeVertex(this.getVertex(vertexValue));
    }

    public abstract Edge<V, E> addEdge(Vertex<V, E> source, Vertex<V, E> target, E value);

    public Edge<V, E> addEdge(V sourceValue, V targetValue, E value) {
        return this.addEdge(this.getVertex(sourceValue), this.getVertex(targetValue), value);
    }

    public Edge<V, E> addEdge(Edge<V, E> edge) {
        return this.addEdge(edge.getSourceValue(), edge.getTargetValue(), edge.get());
    }

    public abstract void removeEdge(Edge<V, E> edge);

    public void removeEdge(V sourceValue, V targetValue) {
        this.removeEdge(this.getEdge(sourceValue, targetValue));
    }

    public abstract Graph<V, E> clone();

    public ListSet<Vertex<V, E>> getReachable(Vertex<V, E> vertex) {
        ListSet<Vertex<V, E>> visited = new ListSet<>();
        depthFirstTraversal(vertex.get(), v -> visited.add(new Vertex<>(this, v)), (e, n) -> {});
        return visited;
    }

    public ListSet<V> getReachableValues(Vertex<V, E> vertex) {
        ListSet<Vertex<V, E>> visited = this.getReachable(vertex);
        return Data.map(visited, Vertex::get);
    }

    public ListSet<Vertex<V, E>> getLeaves(Vertex<V, E> vertex) {
        return Data.filter(this.getReachable(vertex), leave -> !leave.hasOutNeighbors());
    }

    public ListSet<V> getLeaveValues(Vertex<V, E> vertex) {
        return Data.map(this.getLeaves(vertex), Vertex::get);
    }

    public void setVertexWriter(Writer<V> vertexWriter) {
        this.vertexWriter = new SetWriter<>(new VertexWriter<>(vertexWriter));
    }

    public void setEdgeWriter(Writer<V> vertexWriter, Writer<E> dataWriter) {
        this.edgeWriter =
                new SetWriter<>(new EdgeWriter<>(new VertexWriter<>(vertexWriter), dataWriter));
    }

    @Override
    public String toString() {
        return "("
                + this.vertexWriter.write(this.getVertices())
                + ","
                + this.edgeWriter.write(this.getEdges())
                + ")";
    }

    public enum EdgeType {
        TREE,
        BACK,
        FORWARD,
        CROSS,
    }

    /**
     * Checks whether this {@link Graph} is a DAG
     *
     * @return {@code true} iff this graph is a DAG
     */
    public boolean isDirectedAcyclic() {
        return getTopologicalOrdering().isPresent();
    }

    /**
     * Tries to determine a topological ordering of the entire graph, if one exists.
     *
     * <p>A topological ordering {@code <} of vertices has the property {@code (u,v) ∈ E => u < v},
     * and only exists if the graph is acyclic
     *
     * @return A topological ordering, if one exists.
     */
    public Optional<List<V>> getTopologicalOrdering() {
        Set<V> visited = new HashSet<>();
        Map<V, V> dfsTree = new HashMap<>();
        List<V> ordering = new ArrayList<>();

        for (V vertex : getVertexValues()) {
            if (!visited.contains(vertex)) {
                Optional<List<V>> partialOrdering =
                        depthFirstTraversal(vertex, v -> {}, (e, t) -> {}, dfsTree, visited);

                if (!partialOrdering.isPresent()) return Optional.empty();

                partialOrdering.get().addAll(ordering);
                ordering = partialOrdering.get();
            }
        }

        return Optional.of(ordering);
    }

    /**
     * Performs of a depth-first traversal of this {@link Graph}, starting at {@code initial}.
     * Returns a topological ordering of the visited vertices, if one exists.
     *
     * <p>Only visits vertices reachable from {@code initial}
     *
     * @param initial The vertex to start the traversal at
     * @param onVertexDone A callback executed whenever the algorithm leaves a vertex
     * @param onEdgeVisited A callback executed whenever an edge is discovered, which classifies the
     *     edge
     * @return A topological ordering of the visited vertices, if one exists.
     */
    public Optional<List<V>> depthFirstTraversal(
            V initial, Consumer<V> onVertexDone, BiConsumer<Edge<V, E>, EdgeType> onEdgeVisited) {
        return depthFirstTraversal(
                initial, onVertexDone, onEdgeVisited, new HashMap<>(), new HashSet<>());
    }

    /**
     * Internal helper for depth first search. Realised the actual DFS algorithm together with the
     * classification of edges
     *
     * @param current The current vertex
     * @param onVertexDone as in {@link Graph#depthFirstTraversal(Object, Consumer, BiConsumer)}
     * @param onEdgeVisited as in {@link Graph#depthFirstTraversal(Object, Consumer, BiConsumer)}
     * @param dfsTreeUnderConstruction A map storing each node's predecessor in the DFS tree.
     *     Required for determining back/forward edges
     * @param visited A set of already visited vertices
     * @return A topological ordering of the nodes reachable from {@code current}, if one exists.
     */
    private Optional<List<V>> depthFirstTraversal(
            V current,
            Consumer<V> onVertexDone,
            BiConsumer<Edge<V, E>, EdgeType> onEdgeVisited,
            Map<V, V> dfsTreeUnderConstruction,
            Set<V> visited) {
        List<V> ordering =
                new ArrayList<>(); // LinkedList sounds like a more logical choice here, but Java's
        // API don't actually optimize concatenation of LinkedLists.
        boolean hasTopologicalOrdering = true;

        visited.add(current);

        for (Edge<V, E> outgoing : getOutgoingEdges(current)) {
            V destination = outgoing.getTargetValue();

            if (isPredecessor(dfsTreeUnderConstruction, destination, current)) {
                // check if destination is predecessor of current (back edge)
                // This check has to occur first to ensure that
                // 1. Loop are detected as back edges instead of forward edges
                // 2. We catch the case of an edge going back to the DFS-starting-point

                onEdgeVisited.accept(outgoing, EdgeType.BACK);

                hasTopologicalOrdering = false; // A back edge always induces a cycle
            } else if (!dfsTreeUnderConstruction.containsKey(destination)) {
                onEdgeVisited.accept(outgoing, EdgeType.TREE);

                // This could happen if we have the graph "a -> b -> c" and we start a DFS at "b",
                // before starting anew at "a".
                if (!visited.contains(destination)) {
                    dfsTreeUnderConstruction.put(destination, current);
                    Optional<List<V>> maybeOrdering =
                            depthFirstTraversal(
                                    destination,
                                    onVertexDone,
                                    onEdgeVisited,
                                    dfsTreeUnderConstruction,
                                    visited);
                    if (maybeOrdering.isPresent()) {
                        // in a topological ordering, we have "(u,v) in E => u < v".
                        // We know that there is no edge from a previously discovered node to an
                        // undiscovered one,
                        // so putting the newly discovered ones at the front of the ordering is OK.

                        maybeOrdering.get().addAll(ordering);
                        ordering = maybeOrdering.get();
                    } else {
                        hasTopologicalOrdering = false;
                    }
                }
            } else if (isPredecessor(dfsTreeUnderConstruction, current, destination)) {
                // check if current is predecessor of destination (forward edge)

                onEdgeVisited.accept(outgoing, EdgeType.FORWARD);
            } else {
                onEdgeVisited.accept(outgoing, EdgeType.CROSS);
            }
        }

        onVertexDone.accept(current);
        ordering.add(0, current);

        if (hasTopologicalOrdering) return Optional.of(ordering);
        return Optional.empty();
    }

    private static <V> boolean isPredecessor(Map<V, V> dfsTree, V src, V dest) {
        V pred = dest;
        while (pred != null && pred != src) {
            pred = dfsTree.get(pred);
        }
        return pred == src;
    }

    /**
     * Performs a breadth first traversal of this graph, returning the breadth-first-tree.
     *
     * <p>The returned map associates each vertex with a {@link Pair} containing its predecessor and
     * the used edge between the two vertices in the breadth-first-tree. Therefore, the returned map
     * can be used to compute the shortest path.
     *
     * <p>Only the component of the given initial vertex is searched.
     *
     * @param initial The vertex at which to start breadth first traversal
     * @return The breadth-first-tree
     */
    public Map<V, Pair<V, E>> breadthFirstTraversal(V initial) {
        return breadthFirstTraversal(initial, __ -> true, __ -> true, null);
    }

    /**
     * Performs a breadth first traversal of this graph, returning the breadth-first-tree.
     *
     * <p>The returned map associates each vertex with a {@link Pair} containing its predecessor and
     * the used edge between the two vertices in the breadth-first-tree. Therefore, the returned map
     * can be used to compute the shortest path.
     *
     * <p>Only the component of the given initial vertex is searched.
     *
     * @param initial The vertex at which to start breadth first traversal
     * @param vertexFilter The algorithm will only visit vertices for those the {@link Predicate}
     *     evaluates to true.
     * @param edgeFilter The algorithm will only visit edges for those the {@link Predicate}
     *     evaluates to true.
     * @return The breadth-first-tree
     */
    public Map<V, Pair<V, E>> breadthFirstTraversal(
            V initial, Predicate<V> vertexFilter, Predicate<E> edgeFilter) {
        return breadthFirstTraversal(initial, vertexFilter, edgeFilter, null);
    }

    /**
     * Performs a breadth first traversal of this graph, returning the breadth-first-tree.
     *
     * <p>The returned map associates each vertex with a {@link Pair} containing its predecessor and
     * the used edge between the two vertices in the breadth-first-tree. Therefore, the returned map
     * can be used to compute the shortest path.
     *
     * <p>Only the component of the given initial vertex is searched.
     *
     * @param initial The vertex at which to start breadth first traversal
     * @param vertexFilter The algorithm will only visit vertices for those the {@link Predicate}
     *     evaluates to true.
     * @param edgeFilter The algorithm will only visit edges for those the {@link Predicate}
     *     evaluates to true.
     * @param edgePrioritization A {@link Comparator} to sort the outgoing edges from a state before
     *     performing the breadth-first-search iteration-step to allow prioritization for certain
     *     edges. This {@link Comparator} can be {@code null}, then all edges are considered equal.
     * @return The breadth-first-tree
     */
    public Map<V, Pair<V, E>> breadthFirstTraversal(
            V initial,
            Predicate<V> vertexFilter,
            Predicate<E> edgeFilter,
            Comparator<E> edgePrioritization) {
        Comparator<Edge<V, E>> actualEdgeComparator;
        if (edgePrioritization != null)
            actualEdgeComparator =
                    (edge1, edge2) -> edgePrioritization.compare(edge1.get(), edge2.get());
        else actualEdgeComparator = (edge1, edge2) -> 0; // All edges are considered equal

        Queue<V> vertexQueue = new LinkedList<>();
        Map<V, Pair<V, E>> predecessorMap = new HashMap<>();

        vertexQueue.offer(initial);

        while (!vertexQueue.isEmpty()) {
            V current = vertexQueue.poll();

            // Sort the outgoing edges according to the comparator to prioritize certain
            // edge-symbols
            List<Edge<V, E>> outGoingList =
                    getOutgoingEdges(current).stream()
                            .sorted(actualEdgeComparator)
                            .collect(Collectors.toList());

            for (Edge<V, E> outGoing : outGoingList) {
                V dest = outGoing.getTargetValue();

                if (!predecessorMap.containsKey(dest)
                        && dest != initial // unvisited
                        && vertexFilter.test(dest)
                        && edgeFilter.test(outGoing.get())) {
                    vertexQueue.offer(dest);
                    predecessorMap.put(dest, new Pair<>(current, outGoing.get()));
                }
            }
        }

        return predecessorMap;
    }

    public boolean isDirected() {
        return directed;
    }
}
