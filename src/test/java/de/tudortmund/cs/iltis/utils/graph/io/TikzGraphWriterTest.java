package de.tudortmund.cs.iltis.utils.graph.io;

import de.tudortmund.cs.iltis.utils.graph.EmptyEdgeLabel;
import de.tudortmund.cs.iltis.utils.graph.hashgraph.DefaultHashGraph;
import de.tudortmund.cs.iltis.utils.io.writer.graph.TikzGraphWriter;
import org.junit.Test;

public class TikzGraphWriterTest {
    @Test
    public void simpleDirectedGraphTest() {
        DefaultHashGraph<Integer> graph = new DefaultHashGraph<>();
        graph.addVertices(1, 2, 3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 1);
        graph.addEdge(2, 3);

        TikzGraphWriter<Integer, EmptyEdgeLabel> writer;
        writer = new TikzGraphWriter<>();
        System.out.print("\n" + writer.write(graph) + "\n");
    }
}
