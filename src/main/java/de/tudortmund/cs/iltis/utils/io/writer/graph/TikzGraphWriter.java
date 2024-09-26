package de.tudortmund.cs.iltis.utils.io.writer.graph;

import de.tudortmund.cs.iltis.utils.function.SerializableFunction;
import de.tudortmund.cs.iltis.utils.graph.Edge;
import de.tudortmund.cs.iltis.utils.graph.Graph;
import de.tudortmund.cs.iltis.utils.graph.GraphTraversal;
import de.tudortmund.cs.iltis.utils.graph.Vertex;
import de.tudortmund.cs.iltis.utils.io.writer.collections.MapWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.DefaultWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;
import java.util.HashMap;
import java.util.Map;

public class TikzGraphWriter<V, E> extends GraphTraversal<V, E> implements Writer<Graph<V, E>> {
    private boolean firstOutputInLine;
    private boolean comment;
    private boolean alwaysPrintNodeAttributes;
    private boolean alwaysPrintEdgeAttributes;
    private String indentation;

    private String output;

    protected SerializableFunction<Vertex<V, E>, Map<String, String>> nodeAttributes;
    protected SerializableFunction<Edge<V, E>, Map<String, String>> edgeAttributes;

    private Writer<Vertex<V, E>> nodeNameWriter;
    private Writer<Vertex<V, E>> nodeContentWriter;
    private Writer<Edge<V, E>> edgeContentWriter;
    private MapWriter<String, String> nodeAttributesWriter;
    private MapWriter<String, String> edgeAttributesWriter;

    public TikzGraphWriter() {
        super(TraversalOrder.FirstNodesThenEdges);

        this.comment = true;
        this.alwaysPrintEdgeAttributes = false;
        this.alwaysPrintNodeAttributes = false;

        this.nodeAttributes = (node) -> new HashMap<>();
        this.edgeAttributes = (edge) -> new HashMap<>();

        this.nodeNameWriter = new DefaultWriter<>();
        this.nodeContentWriter = new DefaultWriter<>();
        this.edgeContentWriter = new DefaultWriter<>();

        this.nodeAttributesWriter = new MapWriter<>();
        this.nodeAttributesWriter.getSetWriter().setLeftBrace("[");
        this.nodeAttributesWriter.getSetWriter().setRightBrace("]");
        this.edgeAttributesWriter = new MapWriter<>();
        this.edgeAttributesWriter.getSetWriter().setLeftBrace("[");
        this.edgeAttributesWriter.getSetWriter().setRightBrace("]");

        this.reset();
    }

    public void setNodeNameWriter(Writer<Vertex<V, E>> writer) {
        this.nodeNameWriter = writer;
    }

    public void setNodeNameWriterWrapped(Writer<V> writer) {
        this.nodeNameWriter = new VertexWriter<>(writer);
    }

    public void setNodeContentWriter(Writer<Vertex<V, E>> writer) {
        this.nodeContentWriter = writer;
    }

    public void setNodeContentWriterWrapped(Writer<V> writer) {
        this.nodeContentWriter = new VertexWriter<>(writer);
    }

    public void setNodeAttributes(
            SerializableFunction<Vertex<V, E>, Map<String, String>> nodeAttributes) {
        this.nodeAttributes = nodeAttributes;
    }

    public void setEdgeAttributes(
            SerializableFunction<Edge<V, E>, Map<String, String>> edgeAttributes) {
        this.edgeAttributes = edgeAttributes;
    }

    public void reset() {
        this.firstOutputInLine = true;
        this.output = "";
        this.indentation = "";
    }

    @Override
    public String write(Graph<V, E> graph) {
        this.reset();
        this.traverse(graph);
        return this.output;
    }

    public void enableComments() {
        this.comment = true;
    }

    public void disableComments() {
        this.comment = false;
    }

    @Override
    protected void enterEdges(Graph<V, E> graph) {
        comment("Edges ----------------------------------");
        println("path[->]");
        increaseIndentationLevel();
    }

    @Override
    protected void enterGraph(Graph<V, E> graph) {
        comment("Graph ==================================");
    }

    @Override
    protected void enterNodes(Graph<V, E> graph) {
        comment("Nodes ----------------------------------");
    }

    @Override
    protected void leaveEdges(Graph<V, E> graph) {
        decreaseIndentationLevel();
        println(";");
        comment("End of edges ---------------------------");
        println();
    }

    @Override
    protected void leaveGraph(Graph<V, E> graph) {
        comment("End of graph ===========================");
    }

    @Override
    protected void leaveNodes(Graph<V, E> graph) {
        comment("End of nodes ---------------------------");
        println();
    }

    @Override
    protected void visitEdge(Edge<V, E> edge) {
        // specify source node:
        print("(" + this.nodeNameWriter.write(edge.getSource()) + ") ");
        // keyword 'edge':
        print("edge ");
        // write attributes, if defined:
        String attributes = this.edgeAttributesWriter.write(this.edgeAttributes.apply(edge));
        if (this.alwaysPrintEdgeAttributes || !attributes.equals("[]")) print(attributes + " ");
        // write edge label:
        visitEdgeLabel(edge);
        // specify target node:
        if (edge.isLoop()) print("()");
        else print("(" + this.nodeNameWriter.write(edge.getTarget()) + ")");
        println(";");
    }

    protected void visitEdgeLabel(Edge<V, E> edge) {
        // TODO: implement!
    }

    @Override
    protected void visitNode(Vertex<V, E> node) {
        print("node ");
        print("(" + this.nodeNameWriter.write(node) + ") ");
        // TODO: allow absolute positioning
        String attributes = this.nodeAttributesWriter.write(this.nodeAttributes.apply(node));
        if (this.alwaysPrintEdgeAttributes || !attributes.equals("[]")) print(attributes + " ");
        print("{" + this.nodeContentWriter.write(node) + "}");
        println(";");
    }

    protected void comment(String comment) {
        if (this.comment) this.println("% " + comment);
    }

    protected void print(String text) {
        StringBuilder builder = new StringBuilder();

        // indent, if necessary:
        if (this.firstOutputInLine) {
            builder.append(this.indentation);
            this.firstOutputInLine = false;
        }
        // print lines:
        String[] lines = text.split("\n");
        if (lines.length == 0) {
            builder.append("\n");
            this.firstOutputInLine = true;
        } else {
            boolean printLineBreak = text.endsWith("\n");
            for (String line : lines) {
                builder.append(line);
                if (printLineBreak) builder.append("\n");
                printLineBreak = true;
            }
        }
        this.output += builder.toString();
    }

    protected void println(String text) {
        this.print(text + "\n");
        this.firstOutputInLine = true;
    }

    protected void println() {
        this.println("");
    }

    protected void increaseIndentationLevel() {
        this.indentation += "\t";
    }

    protected void decreaseIndentationLevel() {
        this.indentation = this.indentation.substring(0, this.indentation.length() - 1);
    }
}
