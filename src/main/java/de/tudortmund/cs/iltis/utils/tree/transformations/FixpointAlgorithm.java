package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import java.util.Set;

/** Iterates a transformation until a fixpoint is reached. */
public class FixpointAlgorithm<T extends Tree<T>> implements Transformation<T> {

    public FixpointAlgorithm() {}

    public FixpointAlgorithm(Transformation<T> transformation) {
        this(new TreePath(), transformation);
    }

    public FixpointAlgorithm(TreePath path, Transformation<T> transformation) {
        this.path = path;
        this.transformation = transformation;
    }

    @Override
    public boolean isApplicable(T tree) {
        return !tree.getAllApplications(this.transformation).isEmpty();
    }

    @Override
    public T apply(T tree) {
        while (true) {
            Set<TreePath> paths = tree.getAllApplications(this.transformation);
            TreePath firstPath = null;
            for (TreePath path : paths)
                if (path.equals(this.path) || path.isDescendantOf(this.path)) {
                    firstPath = path;
                    break;
                }
            if (firstPath == null) break;
            tree = this.transformation.forPath(firstPath).apply(tree);
        }
        return tree;
    }

    @Override
    public Transformation<T> forPath(TreePath path) {
        return new FixpointAlgorithm<>(path, transformation);
    }

    @Override
    public String toString() {
        return "FixpointAlgorithm [" + transformation + "]";
    }

    protected TreePath path;
    protected Transformation<T> transformation;
}
