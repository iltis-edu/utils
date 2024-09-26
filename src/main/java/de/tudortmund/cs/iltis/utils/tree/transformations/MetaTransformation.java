package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

public class MetaTransformation<T extends Tree<T>> implements Transformation<T> {
    protected List<Transformation<T>> transformations;

    public MetaTransformation() { // Serialization
        this.transformations = new ArrayList<>();
    }

    public MetaTransformation(Transformation<T>... transformations) {
        this(new TreePath(), transformations);
    }

    public MetaTransformation(TreePath path, Transformation<T>... transformations) {
        this.transformations = new ArrayList<>();
        for (Transformation<T> transformation : transformations)
            this.transformations.add(transformation.forPath(path));
    }

    @Override
    public boolean isApplicable(T tree) {
        for (Transformation<T> transformation : this.transformations)
            if (transformation.isApplicable(tree)) return true;
        return false;
    }

    @Override
    public T apply(T tree) {
        for (Transformation<T> transformation : this.transformations)
            if (transformation.isApplicable(tree)) return transformation.apply(tree);
        throw new TransformationUnapplicable(tree);
    }

    @Override
    public MetaTransformation<T> forPath(final TreePath path) {
        return new MetaTransformation<>(path, this.getTransformationArray());
    }

    @Override
    public String toString() {
        return "MetaTransformation " + transformations.toString();
    }

    public List<Transformation<T>> getTransformations() {
        return transformations;
    }

    protected Transformation<T>[] getTransformationArray() {
        return this.transformations.toArray(new Transformation[0]);
    }
}
