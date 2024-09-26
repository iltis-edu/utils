package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import java.io.Serializable;

public interface Transformation<T extends Tree<T>> extends Serializable {
    boolean isApplicable(final T formula);

    T apply(final T formula);

    Transformation<T> forPath(final TreePath path);
}
