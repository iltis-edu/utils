package de.tudortmund.cs.iltis.utils.graph;

public abstract class GraphTraversal<V, E> {
    public enum TraversalOrder {
        FirstNodesThenEdges,
        FirstEdgesThenNodes,
        OnlyNodes,
        OnlyEdges
    }

    private TraversalOrder traversalOrder;

    public GraphTraversal() {
        this(TraversalOrder.FirstEdgesThenNodes);
    }

    public GraphTraversal(TraversalOrder traversalOrder) {
        this.traversalOrder = traversalOrder;
    }

    public void traverse(Graph<V, E> graph) {
        this.enterGraph(graph);

        if (this.traversalOrder == TraversalOrder.FirstNodesThenEdges) {
            this.traverseNodes(graph);
            this.traverseEdges(graph);
        } else if (this.traversalOrder == TraversalOrder.FirstEdgesThenNodes) {
            this.traverseEdges(graph);
            this.traverseNodes(graph);
        } else if (this.traversalOrder == TraversalOrder.OnlyNodes) {
            this.traverseNodes(graph);
        } else if (this.traversalOrder == TraversalOrder.OnlyEdges) {
            this.traverseEdges(graph);
        }

        this.leaveGraph(graph);
    }

    protected void enterEdges(Graph<V, E> graph) {}

    protected void enterGraph(Graph<V, E> graph) {}

    protected void enterNodes(Graph<V, E> graph) {}

    protected void leaveEdges(Graph<V, E> graph) {}

    protected void leaveGraph(Graph<V, E> graph) {}

    protected void leaveNodes(Graph<V, E> graph) {}

    protected void traverseNodes(Graph<V, E> graph) {
        this.enterNodes(graph);
        for (Vertex<V, E> node : graph.getVertices()) this.visitNode(node);
        this.leaveNodes(graph);
    }

    protected void traverseEdges(Graph<V, E> graph) {
        this.enterEdges(graph);
        for (Edge<V, E> edge : graph.getEdges()) this.visitEdge(edge);
        this.leaveEdges(graph);
    }

    protected abstract void visitEdge(Edge<V, E> edge);

    protected abstract void visitNode(Vertex<V, E> node);
}
