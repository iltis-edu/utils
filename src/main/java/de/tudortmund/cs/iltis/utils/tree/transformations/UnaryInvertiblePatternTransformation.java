package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import de.tudortmund.cs.iltis.utils.tree.pattern.TreePattern;

public class UnaryInvertiblePatternTransformation<T extends Tree<T>>
        extends UnaryPatternTransformation<T> implements InvertibleTransformation<T> {

    /// * Need for serialization */
    public UnaryInvertiblePatternTransformation() { // Serialization
    }

    public UnaryInvertiblePatternTransformation(TreePattern<T> match, TreePattern<T> replace) {

        super(match, replace);
    }

    public UnaryInvertiblePatternTransformation(
            TreePath path, TreePattern<T> match, TreePattern<T> replace) {

        super(path, match, replace);
    }

    @Override
    public UnaryInvertiblePatternTransformation<T> forPath(final TreePath path) {
        return new UnaryInvertiblePatternTransformation<>(
                path, this.matchPattern, this.replacePattern);
    }

    @Override
    public UnaryInvertiblePatternTransformation<T> getInverse() {
        return new UnaryInvertiblePatternTransformation<>(
                this.path, this.replacePattern, this.matchPattern);
    }
}
