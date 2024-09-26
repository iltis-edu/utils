package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import java.util.Collection;

/** Invertible transformation that uses arbitrary number of arguments in form of tree paths */
public interface InvertibleNAryTransformation<T extends Tree<T>>
        extends NAryTransformation<T>, InvertibleTransformation<T> {
    @Override
    public InvertibleNAryTransformation<T> getInverse();

    @Override
    public InvertibleNAryTransformation<T> forPaths(Collection<TreePath> paths);
}
