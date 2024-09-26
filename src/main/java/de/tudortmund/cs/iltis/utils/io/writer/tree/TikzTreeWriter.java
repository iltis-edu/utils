package de.tudortmund.cs.iltis.utils.io.writer.tree;

import de.tudortmund.cs.iltis.utils.io.writer.general.DefaultWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.EmptyWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;
import de.tudortmund.cs.iltis.utils.tree.Tree;

public class TikzTreeWriter<T extends Tree<T>> implements Writer<T> {
    private Writer<T> nodeWriter;
    private Writer<T> nodeStyler;

    public TikzTreeWriter() {
        this(new DefaultWriter<>(), new EmptyWriter<>());
    }

    public TikzTreeWriter(Writer<T> nodeWriter, Writer<T> nodeStyler) {
        this.nodeWriter = nodeWriter;
        this.nodeStyler = nodeStyler;
    }

    @Override
    public String write(T tree) {
        StringBuilder builder = new StringBuilder();
        builder.append("\\node ")
                .append(styleNode(tree))
                .append(" ")
                .append(writeNode(tree))
                .append("\n");
        for (T child : tree.getChildren()) builder.append(this.write(child, "\t"));
        builder.append(";\n");
        return builder.toString();
    }

    private String write(T tree, String indent) {
        StringBuilder builder = new StringBuilder();
        builder.append(indent).append("child {\n");
        builder.append(indent)
                .append("\tnode ")
                .append(styleNode(tree))
                .append(" ")
                .append(writeNode(tree))
                .append("\n");
        for (T child : tree.getChildren()) builder.append(this.write(child, indent + "\t"));
        builder.append(indent).append("}\n");
        return builder.toString();
    }

    private String styleNode(T tree) {
        return "[" + this.nodeStyler.write(tree) + "]";
    }

    private String writeNode(T tree) {
        return "{" + this.nodeWriter.write(tree) + "}";
    }
}
