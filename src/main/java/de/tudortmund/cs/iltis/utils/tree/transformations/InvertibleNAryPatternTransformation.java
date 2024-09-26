package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import de.tudortmund.cs.iltis.utils.tree.pattern.TreePattern;
import java.util.Collection;
import java.util.Collections;

/**
 * Invertible transformation that uses pattern and arbitrary number of arguments in form of tree
 * paths
 */
public class InvertibleNAryPatternTransformation<T extends Tree<T>>
        extends NAryPatternTransformation<T> implements InvertibleNAryTransformation<T> {

    public InvertibleNAryPatternTransformation() { // Serialization
    }

    public InvertibleNAryPatternTransformation(
            Collection<TreePath> paths, TreePattern<T> match, TreePattern<T> replace) {

        super(paths, match, replace);
    }

    public InvertibleNAryPatternTransformation(TreePattern<T> match, TreePattern<T> replace) {

        super(match, replace);
    }

    @Override
    public InvertibleNAryPatternTransformation<T> getInverse() {
        return new InvertibleNAryPatternTransformation<>(
                this.paths, this.replacePattern, this.matchPattern);
    }

    @Override
    public InvertibleTransformation<T> forPath(final TreePath path) {
        return new InvertibleNAryPatternTransformation<>(
                Collections.singleton(path), matchPattern, replacePattern);
    }

    @Override
    public InvertibleNAryTransformation<T> forPaths(Collection<TreePath> paths) {
        return new InvertibleNAryPatternTransformation<>(paths, matchPattern, replacePattern);
    }
}
