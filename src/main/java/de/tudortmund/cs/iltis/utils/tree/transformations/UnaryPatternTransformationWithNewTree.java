package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import de.tudortmund.cs.iltis.utils.tree.pattern.TreePattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnaryPatternTransformationWithNewTree<T extends Tree<T>>
        extends UnaryPatternTransformation<T>
        implements TransformationWithNewTree<T>, Serializable {
    protected String newTreeId;
    protected T newTree;

    protected UnaryPatternTransformationWithNewTree() { // Serialization
    }

    public UnaryPatternTransformationWithNewTree(
            TreePattern<T> match, TreePattern<T> replace, String newTreeId) {

        this(new TreePath(), match, replace, newTreeId);
    }

    public UnaryPatternTransformationWithNewTree(
            final TreePath path, TreePattern<T> match, TreePattern<T> replace, String newTreeId) {

        super(path, match, replace);
        this.newTreeId = newTreeId;
    }

    public void setNewTree(T tree) {
        this.newTree = tree;
    }

    @Override
    public boolean isApplicable(final T tree) {
        if (null == newTree) {
            return false;
        }
        return super.isApplicable(tree);
    }

    @Override
    public T apply(T tree) {
        T subtree = tree.retrieve(this.path);
        Match<T> match = matchPattern.getFirstMatchIfAny(subtree).get();
        match = match.withDefinition(new IndexedSymbol(newTreeId), newTree).get();
        return (T) tree.transform(this.path, replacePattern.createTree(match));
    }

    @Override
    public UnaryPatternTransformationWithNewTree<T> forPath(TreePath path) {
        UnaryPatternTransformationWithNewTree<T> clone =
                new UnaryPatternTransformationWithNewTree<>(
                        path, matchPattern, replacePattern, newTreeId);

        clone.setNewTree(newTree);
        return clone;
    }

    public List<TransformationWithNewTree<T>> forMultipleSubtrees(final T... trees) {
        return forMultipleSubtrees(Arrays.asList(trees));
    }

    @Override
    public List<TransformationWithNewTree<T>> forMultipleSubtrees(final Iterable<T> trees) {
        List<TransformationWithNewTree<T>> transformations = new ArrayList<>();
        for (T tree : trees) {
            UnaryPatternTransformationWithNewTree<T> newTrans = clone();
            newTrans.setNewTree((T) tree.clone());
            transformations.add(newTrans);
        }
        return transformations;
    }

    protected UnaryPatternTransformationWithNewTree<T> clone() {
        UnaryPatternTransformationWithNewTree<T> clone =
                new UnaryPatternTransformationWithNewTree<>(
                        matchPattern, replacePattern, newTreeId);

        clone.setNewTree(newTree);
        return clone;
    }
}
