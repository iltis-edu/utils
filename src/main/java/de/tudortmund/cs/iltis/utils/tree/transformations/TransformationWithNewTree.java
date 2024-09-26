package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import java.util.List;

public interface TransformationWithNewTree<T extends Tree<T>> extends Transformation<T> {
    public void setNewTree(T newTree);

    public List<TransformationWithNewTree<T>> forMultipleSubtrees(final Iterable<T> trees);

    public TransformationWithNewTree<T> forPath(TreePath path);
}
