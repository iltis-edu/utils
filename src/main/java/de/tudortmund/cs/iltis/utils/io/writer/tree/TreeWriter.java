package de.tudortmund.cs.iltis.utils.io.writer.tree;

import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;
import de.tudortmund.cs.iltis.utils.tree.TraversalStrategy;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.List;

/**
 * A default writer for trees.
 *
 * @param <T> Type of subtrees.
 */
public class TreeWriter<T extends Tree<T>>
        implements TraversalStrategy<T, String>, Writer<Tree<T>> {

    /** Separator used to separate arguments. */
    private String separator;

    /** String used in front of first argument. */
    private String front;

    /** String used behind last argument. */
    private String back;

    public TreeWriter() {
        this("(", ")", ",");
    }

    public TreeWriter(String front, String back, String separator) {
        this.front = front;
        this.back = back;
        this.separator = separator;
    }

    @Override
    public String write(Tree<T> tree) {
        return tree.traverse(this);
    }

    @Override
    public String inspect(T item, List<String> childrenOutput) {
        StringBuilder text = new StringBuilder();
        text.append(front);
        text.append(String.join(separator, childrenOutput));
        text.append(back);
        return text.toString();
    }
}
