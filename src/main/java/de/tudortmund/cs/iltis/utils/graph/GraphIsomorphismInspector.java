package de.tudortmund.cs.iltis.utils.graph;

import com.google.gson.Gson;
import de.tudortmund.cs.iltis.utils.graph.hashgraph.UndirectedHashGraph;
import de.tudortmund.cs.iltis.utils.weblib.*;
import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class which provides functionality for testing two {@link UndirectedHashGraph}s on isomorphism.
 *
 * <p>This class does not directly compute the results. It calls an external tool called <a
 * href="http://www.tcs.hut.fi/Software/bliss/">bliss</a> which computes the results on a remote
 * server. In order to do this, the {@link WebLib}-Adapter is used.
 *
 * <p>Since bliss (specifically bliss' Java-Wrapper) only supports undirected graphs, the input
 * graphs must be undirected. bliss also does not support edge-labels or -weights, hence the edges
 * must all be {@link EmptyEdgeLabel}. This also ensures that no multi-edges are used in the input
 * graphs.
 *
 * <p>Because the communication to bliss works via a REST-API, connection errors can possibly occur.
 */
@SuppressWarnings("NonJREEmulationClassesInClientCode")
public class GraphIsomorphismInspector {
    private static final String BLISS = "bliss";
    private static final String CANONICAL_LABELLING = "canonicalLabelling";
    private static final String AUTOMORPHISM_GENERATORS = "automorphismGenerators";

    private static Gson gson = new Gson();

    /**
     * Computes if the given {@link UndirectedHashGraph}s are isomorphic, i.e. if they are
     * structurally equal, but their vertex labels may differ.
     *
     * <p>The vertices' colors will be considered in the computation of the canonical labelling,
     * which is the base of this isomorphism test. For more information on the initial coloring see
     * {@link #getAutomorphismGenerators(UndirectedHashGraph)}.
     *
     * @see #getCanonicalLabelling(UndirectedHashGraph)
     * @param graph1 The first undirected graph
     * @param graph2 The second undirected graph
     * @return {@code true} iff the given graphs are isomorphic with respect to their vertices'
     *     coloring, {@code false} otherwise
     * @param <V1> The type of the vertices in the first graph
     * @param <V2> The type of the vertices in the second graph
     */
    public static <V1, V2> boolean isIsomorphicTo(
            UndirectedHashGraph<V1, EmptyEdgeLabel> graph1,
            UndirectedHashGraph<V2, EmptyEdgeLabel> graph2) {
        if (graph1.getVertices().size() != graph2.getVertices().size()) return false;
        if (graph1.getEdges().size() != graph2.getEdges().size()) return false;

        UndirectedHashGraph<Integer, EmptyEdgeLabel> relabelled1 =
                getCanonicallyLabelledGraph(graph1);
        UndirectedHashGraph<Integer, EmptyEdgeLabel> relabelled2 =
                getCanonicallyLabelledGraph(graph2);

        return relabelled1.equals(relabelled2);
    }

    /**
     * Labels the given {@link UndirectedHashGraph} canonically.
     *
     * @see #getCanonicalLabelling(UndirectedHashGraph)
     * @param graph The undirected graph
     * @return A canonically labelled graph with {@link Integer} as its vertices' type
     * @param <V> The type of the vertices in the input graph
     */
    public static <V> UndirectedHashGraph<Integer, EmptyEdgeLabel> getCanonicallyLabelledGraph(
            UndirectedHashGraph<V, EmptyEdgeLabel> graph) {
        Map<V, Integer> labelling = getCanonicalLabelling(graph);
        return graph.map(labelling::get, e -> e);
    }

    /**
     * Computes a canonical labelling for all vertices of the given input-{@link
     * UndirectedHashGraph}. All isomorphic graphs with the same initial vertex-coloring will result
     * in the same canonically labelled graphs.
     *
     * <p>Different initial colorings result in different canonical labellings, even for
     * structurally isomorphic graphs! For more information on the initial coloring see {@link
     * #getAutomorphismGenerators(UndirectedHashGraph)}.
     *
     * @param graph The undirected graph
     * @return A canonical labelling which maps the original vertices to {@link Integer}s with
     *     respect to their initial coloring
     * @param <V> The type of the vertices in the input graph
     */
    public static <V> Map<V, Integer> getCanonicalLabelling(
            UndirectedHashGraph<V, EmptyEdgeLabel> graph) {
        String argument = gson.toJson(convertToJSONGraph(graph));

        final String[] response =
                new String[1]; // Needs to be a one-field array because of Java-lambda-weirdness
        WebLibAdapter.getWebLibFunction(BLISS, CANONICAL_LABELLING)
                .call(
                        argument,
                        new WebLibFunctionCallHandler() {
                            @Override
                            public void onSuccess(String body) {
                                response[0] = body;
                            }

                            @Override
                            public void onError(Exception exception) {
                                throw new RuntimeException("Call to bliss was unsuccessful");
                            }
                        });

        CanonicalLabellingResponse parsedResponse =
                gson.fromJson(response[0], CanonicalLabellingResponse.class);
        // Convert back to original object
        return mapJavaMap(
                parsedResponse.canonicalLabelling,
                GraphIsomorphismInspector::deserializeObjectFromString,
                i -> i);
    }

    /**
     * TODO: Adapt this doc if the used method in bliss gets changed Computes some automorphisms of
     * the given input-{@link UndirectedHashGraph} that are used to calculate the canonical
     * labelling. It is not guaranteed that all automorphisms get returned. An automorphism is an
     * isomorphism onto itself. This calculation takes the initial coloring of the vertices into
     * account.
     *
     * <p>A vertex-coloring means that every {@link Vertex} of the graph is assigned an initial
     * {@link Integer}, i.e. its initial color. Vertices with different colors are considered
     * different and can definitely not be mapped onto each other during the
     * automorphism-calculation, which is also the base of the canonical labelling computation.
     * {@link #getCanonicalLabelling(UndirectedHashGraph)}.
     *
     * <p>To ignore the initial coloring (e.g. to just compute structural isomorphism), assign the
     * same color to every vertex.
     *
     * <p>The automorphisms are returned as a {@link Map}, where the key corresponds to the original
     * vertex and the value corresponds to the new vertex-label to create an automorphism.
     *
     * @param graph The undirected graph
     * @return A {@link Set} containing all possible automorphism generators, each represented as a
     *     {@link Map}
     * @param <V> The type of the vertices in the input graph
     */
    public static <V> Set<Map<V, V>> getAutomorphismGenerators(
            UndirectedHashGraph<V, EmptyEdgeLabel> graph) {
        String argument = gson.toJson(convertToJSONGraph(graph));

        final String[] response =
                new String[1]; // Needs to be a one-field array because of Java-lambda-weirdness
        WebLibAdapter.getWebLibFunction(BLISS, AUTOMORPHISM_GENERATORS)
                .call(
                        argument,
                        new WebLibFunctionCallHandler() {
                            @Override
                            public void onSuccess(String body) {
                                response[0] = body;
                            }

                            @Override
                            public void onError(Exception exception) {
                                throw new RuntimeException("Call to bliss was unsuccessful");
                            }
                        });

        AutomorphismGeneratorsResponse parsedResponse =
                gson.fromJson(response[0], AutomorphismGeneratorsResponse.class);
        // Convert back to original object
        return parsedResponse.automorphismGenerators.stream()
                .map(
                        automorphismGenerator ->
                                mapJavaMap(
                                        automorphismGenerator,
                                        GraphIsomorphismInspector::<V>deserializeObjectFromString,
                                        GraphIsomorphismInspector::<V>deserializeObjectFromString))
                .collect(Collectors.toSet());
    }

    private static <V> JSONGraphRequest convertToJSONGraph(
            UndirectedHashGraph<V, EmptyEdgeLabel> graph) {
        UndirectedHashGraph<String, EmptyEdgeLabel> stringGraph =
                graph.map(GraphIsomorphismInspector::serializeObjectToString, e -> e);

        JSONGraphRequest jsonGraph = new JSONGraphRequest();
        stringGraph
                .getVertices()
                .forEach(
                        oldVertex -> {
                            JSONGraphRequest.Vertex newVertex = new JSONGraphRequest.Vertex();
                            newVertex.label = oldVertex.get();
                            newVertex.color = oldVertex.getColor();
                            jsonGraph.vertices.add(newVertex);
                        });
        stringGraph
                .getEdges()
                .forEach(
                        oldEdge -> {
                            JSONGraphRequest.Edge newEdge = new JSONGraphRequest.Edge();
                            newEdge.source = oldEdge.getSourceValue();
                            newEdge.target = oldEdge.getTargetValue();

                            // We need to check the reversed edge because the bliss API only accepts
                            // edges in one direction
                            JSONGraphRequest.Edge newEdgeReversed = new JSONGraphRequest.Edge();
                            newEdgeReversed.source = oldEdge.getTargetValue();
                            newEdgeReversed.target = oldEdge.getSourceValue();

                            if (!jsonGraph.edges.contains(newEdge)
                                    && !jsonGraph.edges.contains(newEdgeReversed))
                                jsonGraph.edges.add(newEdge);
                        });

        return jsonGraph;
    }

    /** Maps the keys and values of a Java {@link Map} according to the given {@link Function}s. */
    private static <K, NewK, V, NewV> Map<NewK, NewV> mapJavaMap(
            Map<K, V> originalMap, Function<K, NewK> keyMapping, Function<V, NewV> valueMapping) {
        return originalMap.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> keyMapping.apply(entry.getKey()),
                                entry -> valueMapping.apply(entry.getValue())));
    }

    @SuppressWarnings("all")
    private static class JSONGraphRequest {
        Set<Vertex> vertices;
        Set<Edge> edges;

        public JSONGraphRequest() {
            vertices = new HashSet<>();
            edges = new HashSet<>();
        }

        public static class Vertex {
            String label;
            int color;
        }

        public static class Edge {
            String source;
            String target;

            /**
             * Needed for {@link #convertToJSONGraph(UndirectedHashGraph)} (because of {@link
             * HashSet#contains(Object)}
             */
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (!(obj instanceof Edge)) return false;
                Edge other = (Edge) obj;
                return Objects.equals(this.source, other.source)
                        && Objects.equals(this.target, other.target);
            }

            /**
             * Needed for {@link #convertToJSONGraph(UndirectedHashGraph)} (because of {@link
             * HashSet#contains(Object)}
             */
            @Override
            public int hashCode() {
                return Objects.hash(this.source, this.target);
            }
        }
    }

    @SuppressWarnings("all")
    private static class CanonicalLabellingResponse {
        Map<String, Integer> canonicalLabelling;
    }

    @SuppressWarnings("all")
    private static class AutomorphismGeneratorsResponse {
        Set<Map<String, String>> automorphismGenerators;
    }

    /**
     * The implementation is taken from <a
     * href="https://stackoverflow.com/questions/8887197/reliably-convert-any-object-to-string-and-then-back-again">here</a>
     */
    private static <O> String serializeObjectToString(O object) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(object);
            so.flush();
            return Base64.getEncoder().encodeToString(bo.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * The implementation is taken from <a
     * href="https://stackoverflow.com/questions/8887197/reliably-convert-any-object-to-string-and-then-back-again">here</a>
     */
    @SuppressWarnings("unchecked")
    private static <O> O deserializeObjectFromString(String string) {
        try {
            byte[] b = Base64.getDecoder().decode(string.getBytes());
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            return (O) si.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
