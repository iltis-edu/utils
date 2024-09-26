package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;

/** Transformation that replaces all children using a transformation. */
public class ChildrenTransformation<T extends Tree<T>> implements Transformation<T> {
    protected TreePath path;
    protected Transformation<T> transformation;

    // needed for serialization
    public ChildrenTransformation() {
        this.path = new TreePath();
    }

    public ChildrenTransformation(Transformation<T> transformation) {
        this(new TreePath(), transformation);
    }

    public ChildrenTransformation(TreePath path, Transformation<T> transformation) {
        this.path = path;
        this.transformation = transformation;
    }

    @Override
    public boolean isApplicable(final T tree) {
        T subtree = tree.retrieve(this.path);

        for (T child : subtree.getChildren()) {
            if (!transformation.isApplicable(child)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public T apply(T tree) {
        T result = tree;
        T subtree = tree.retrieve(this.path);

        int childrenIndex = 0;
        for (T child : subtree.getChildren()) {

            result =
                    (T)
                            result.transform(
                                    path.clone().child(childrenIndex), transformation.apply(child));

            childrenIndex++;
        }

        return result;
    }

    @Override
    public ChildrenTransformation<T> forPath(final TreePath path) {
        return new ChildrenTransformation<>(path, transformation);
    }

    @Override
    public String toString() {
        return "ChildrenTransformation [transformation:" + transformation.toString() + "]";
    }
}
