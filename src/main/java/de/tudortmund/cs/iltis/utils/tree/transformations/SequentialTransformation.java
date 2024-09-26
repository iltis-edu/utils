package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/** Transformation that applies a sequence of other transformations in the specified order. */
public class SequentialTransformation<T extends Tree<T>> implements Transformation<T> {
    protected TreePath path;
    protected List<Transformation<T>> transformations;

    // needed for serialization
    public SequentialTransformation() {
        this.path = new TreePath();
        this.transformations = new ArrayList<>();
    }

    public SequentialTransformation(Transformation<T>... transformations) {
        this(new TreePath(), transformations);
    }

    public SequentialTransformation(TreePath path, Transformation<T>... transformations) {
        this.path = path;
        this.transformations = new ArrayList<>();
        for (Transformation<T> transformation : transformations) {
            this.transformations.add(transformation);
        }
    }

    @Override
    public boolean isApplicable(T tree) {
        T transformedTree = tree.retrieve(path);

        for (Transformation<T> transformation : this.transformations) {
            if (transformation.isApplicable(transformedTree)) {
                transformedTree = transformation.apply(transformedTree);
            } else {
                return false;
            }
        }

        return true;
    }

    @Override
    public T apply(T tree) {
        T subtree = tree.retrieve(path);

        for (Transformation<T> transformation : this.transformations) {
            if (transformation.isApplicable(subtree)) {
                subtree = transformation.apply(subtree);
            } else {
                throw new TransformationUnapplicable(tree);
            }
        }

        return (T) tree.transform(this.path, subtree);
    }

    @Override
    public SequentialTransformation<T> forPath(final TreePath path) {
        return new SequentialTransformation<>(path, this.getTransformationArray());
    }

    @Override
    public String toString() {
        return "SequentialTransformation [transformations:" + transformations.toString() + "]";
    }

    public List<Transformation<T>> getTransformations() {
        return transformations;
    }

    protected Transformation<T>[] getTransformationArray() {
        return this.transformations.toArray(new Transformation[0]);
    }
}
