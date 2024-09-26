package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import de.tudortmund.cs.iltis.utils.tree.pattern.TreePattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;

public class UnaryPatternTransformation<T extends Tree<T>> extends PatternTransformation<T> {

    protected TreePath path;

    /* Need for serialization */
    public UnaryPatternTransformation() {}

    public UnaryPatternTransformation(TreePattern<T> match, TreePattern<T> replace) {

        this(new TreePath(), match, replace);
    }

    public UnaryPatternTransformation(
            final TreePath path, TreePattern<T> match, TreePattern<T> replace) {

        super(match, replace);
        this.path = path;
    }

    @Override
    public boolean isApplicable(final T tree) {
        T subtree = tree.retrieve(this.path);
        return matchPattern.matches(subtree);
    }

    @Override
    public T apply(final T tree) {
        T subtree = tree.retrieve(this.path);
        Match<T> match = matchPattern.getFirstMatchIfAny(subtree).get();
        return (T) tree.transform(this.path, replacePattern.createTree(match));
    }

    @Override
    public Transformation<T> forPath(TreePath path) {
        return new UnaryPatternTransformation<>(path, this.matchPattern, this.replacePattern);
    }
}
