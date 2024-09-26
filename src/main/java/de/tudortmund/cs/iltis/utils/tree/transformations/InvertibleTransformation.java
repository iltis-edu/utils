package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;

public interface InvertibleTransformation<T extends Tree<T>> extends Transformation<T> {
    public InvertibleTransformation<T> getInverse();

    public abstract InvertibleTransformation<T> forPath(final TreePath path);
}
