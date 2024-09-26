package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import de.tudortmund.cs.iltis.utils.tree.pattern.TreePattern;
import java.util.Set;

/** Iterates a transformation until a target pattern is reached. */
public class TargetedIterationAlgorithm<T extends Tree<T>> implements Transformation<T> {
    protected TreePath path;
    protected Transformation<T> transformation;
    protected TreePattern<T> targetPattern;

    public TargetedIterationAlgorithm() {}

    public TargetedIterationAlgorithm(TreePattern<T> target, Transformation<T> transformation) {

        this(new TreePath(), target, transformation);
    }

    public TargetedIterationAlgorithm(
            TreePath path, TreePattern<T> target, Transformation<T> transformation) {

        this.path = path;
        this.transformation = transformation;
        this.targetPattern = target;
    }

    @Override
    public boolean isApplicable(T tree) {
        return this.transformation.isApplicable(tree);
    }

    @Override
    public T apply(T tree) {
        while (true) {
            if (this.targetPattern.matches(tree)) {
                return tree;
            }

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
        return new TargetedIterationAlgorithm<>(path, targetPattern, transformation);
    }
}
