package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;

public class InvertibleChildrenTransformation<T extends Tree<T>> extends ChildrenTransformation<T>
        implements InvertibleTransformation<T> {

    // needed for serialization
    public InvertibleChildrenTransformation() {
        super();
    }

    public InvertibleChildrenTransformation(InvertibleTransformation<T> transformation) {
        this(new TreePath(), transformation);
    }

    public InvertibleChildrenTransformation(
            TreePath path, InvertibleTransformation<T> transformation) {
        super(path, transformation);
    }

    @Override
    public InvertibleChildrenTransformation<T> getInverse() {

        return new InvertibleChildrenTransformation<>(
                path, ((InvertibleChildrenTransformation<T>) transformation).getInverse());
    }

    @Override
    public InvertibleChildrenTransformation<T> forPath(final TreePath path) {
        return new InvertibleChildrenTransformation<>(
                path, (InvertibleTransformation<T>) transformation);
    }
}
