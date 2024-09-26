package de.tudortmund.cs.iltis.utils.io.writer.graph;

import de.tudortmund.cs.iltis.utils.graph.Edge;
import de.tudortmund.cs.iltis.utils.graph.Vertex;
import de.tudortmund.cs.iltis.utils.io.writer.general.DefaultWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;

public class EdgeWriter<V, E> implements Writer<Edge<V, E>> {
    protected Writer<Vertex<V, E>> vertexWriter;
    protected Writer<E> dataWriter;

    public EdgeWriter() {
        this(new DefaultWriter<>(), new DefaultWriter<>());
    }

    public EdgeWriter(Writer<Vertex<V, E>> vertexWriter) {
        this(vertexWriter, new DefaultWriter<>());
    }

    public EdgeWriter(Writer<Vertex<V, E>> vertexWriter, Writer<E> dataWriter) {
        this.vertexWriter = vertexWriter;
        this.dataWriter = dataWriter;
    }

    @Override
    public String write(Edge<V, E> edge) {
        return "("
                + this.vertexWriter.write(edge.getSource())
                + " -> "
                + this.vertexWriter.write(edge.getTarget())
                + ", label: "
                + this.dataWriter.write(edge.get())
                + ")";
    }
}
