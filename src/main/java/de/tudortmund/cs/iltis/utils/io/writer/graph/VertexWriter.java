package de.tudortmund.cs.iltis.utils.io.writer.graph;

import de.tudortmund.cs.iltis.utils.graph.Vertex;
import de.tudortmund.cs.iltis.utils.io.writer.general.DefaultWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;

public class VertexWriter<V, E> implements Writer<Vertex<V, E>> {
    private Writer<V> dataWriter;

    public VertexWriter() {
        this.dataWriter = new DefaultWriter<>();
    }

    public VertexWriter(Writer<V> dataWriter) {
        this.dataWriter = dataWriter;
    }

    @Override
    public String write(Vertex<V, E> vertex) {
        return this.dataWriter.write(vertex.get());
    }
}
