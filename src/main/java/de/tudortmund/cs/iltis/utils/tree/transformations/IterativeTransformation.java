package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IterativeTransformation<T extends Tree<T>>
        implements Transformation<T>, Comparable<IterativeTransformation<T>> {

    @SafeVarargs
    public IterativeTransformation(IterativeTransformation<T>... iterativeTransformations) {
        this(
                new TreePath(),
                Arrays.stream(iterativeTransformations)
                        .filter(Objects::nonNull)
                        .map(it -> it.transformations)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
    }

    @SafeVarargs
    public IterativeTransformation(Transformation<T>... transformations) {
        this(new TreePath(), transformations);
    }

    @SafeVarargs
    public IterativeTransformation(TreePath path, Transformation<T>... transformations) {
        this(path, Arrays.asList(transformations));
    }

    public IterativeTransformation(TreePath path, List<Transformation<T>> transformations) {
        this.path = path;
        // TODO: clone()
        this.transformations =
                transformations.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public boolean isApplicable(T tree) {
        T currentTree = tree;
        for (Transformation<T> transformation : transformations) {
            if (!transformation.isApplicable(currentTree)) {
                return false;
            }
            currentTree = transformation.apply(currentTree);
        }
        return true;
    }

    @Override
    public T apply(T tree) {
        T currentTree = tree;
        for (Transformation<T> transformation : transformations) {
            currentTree = transformation.apply(currentTree);
        }
        return currentTree;
    }

    @Override
    public Transformation<T> forPath(TreePath path) {
        return new IterativeTransformation<>(path, transformations);
    }

    public void addTransformation(Transformation<T> transformation) {
        transformations.add(transformation);
    }

    public List<Transformation<T>> getTransformations() {
        return transformations;
    }

    public int size() {
        return transformations.size();
    }

    @Override
    public String toString() {
        return transformations.toString();
    }

    @Override
    public int compareTo(IterativeTransformation<T> other) {
        if (null == other) {
            throw new NullPointerException();
        }
        return Integer.compare(transformations.size(), other.transformations.size());
    }

    protected TreePath path;
    protected List<Transformation<T>> transformations;

    /** For serialization */
    @SuppressWarnings("unused")
    private IterativeTransformation() {}
}
