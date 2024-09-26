package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import java.util.Collection;

/** Transformation that uses arbitrary number of arguments in form of tree paths */
public interface NAryTransformation<T extends Tree<T>> extends Transformation<T> {

    /**
     * Creates a copy of this transformation with given arguments
     *
     * @param paths paths for new transformation
     * @return transformation with given arguments
     */
    NAryTransformation<T> forPaths(Collection<TreePath> paths);
}
