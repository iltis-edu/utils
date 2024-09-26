package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.Objects;

/** A simple tree with String labels for pattern tests. */
public class StringTree extends Tree<StringTree> implements Comparable<StringTree> {

    protected String label;

    public StringTree(String label, Iterable<StringTree> children) {
        super(children);
        this.label = label;
    }

    public StringTree(String label, StringTree... children) {
        super(children);
        this.label = label;
    }

    /** Constructs a tree without children. */
    public StringTree(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int compareTo(StringTree other) {
        Objects.requireNonNull(other);

        int orderByLabel = label.compareTo(other.label);
        if (orderByLabel != 0) return orderByLabel;

        if (!hasSameClass(other)) {
            return getClass().getName().compareTo(other.getClass().getName());
        }

        // compare number of children
        int diffSize = this.getNumberOfChildren() - other.getNumberOfChildren();
        if (diffSize != 0) {
            return diffSize;
        }

        // compare children
        for (int i = 0; i < getNumberOfChildren(); i++) {
            int diffChild = this.getChild(i).compareTo(other.getChild(i));
            if (diffChild != 0) {
                return diffChild;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && Objects.equals(label, ((StringTree) obj).label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), label);
    }

    @Override
    public String toString() {
        StringTreeWriter writer = new StringTreeWriter();
        return writer.write(this);
    }

    /** For serialization */
    @SuppressWarnings("unused")
    private StringTree() {}
}
