package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import de.tudortmund.cs.iltis.utils.tree.pattern.TreePattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import java.util.Optional;

/**
 * Transformation that replaces all children using patterns. Injects information of outer structure
 * into local patterns.
 */
public class ChildrenPatternTransformation<T extends Tree<T>> extends UnaryPatternTransformation<T>
        implements InvertibleTransformation<T> {

    private TreePattern<T> outerPattern;

    // needed for serialization
    public ChildrenPatternTransformation() {}

    public ChildrenPatternTransformation(
            TreePattern<T> outer, TreePattern<T> match, TreePattern<T> replace) {

        this(new TreePath(), outer, match, replace);
    }

    public ChildrenPatternTransformation(
            final TreePath path,
            TreePattern<T> outer,
            TreePattern<T> match,
            TreePattern<T> replace) {

        super(path, match, replace);
        this.outerPattern = outer;
    }

    @Override
    public boolean isApplicable(final T tree) {
        return outerPattern.matches(tree);
    }

    @Override
    public T apply(T tree) {
        T result = tree;
        T subtree = tree.retrieve(this.path);

        Optional<Match<T>> optMatch = outerPattern.getFirstMatchIfAny(tree);

        if (!optMatch.isPresent()) {
            throw new TransformationUnapplicable(tree);
        }

        Match<T> match = optMatch.get();

        int childrenIndex = 0;
        for (T child : subtree.getChildren()) {

            Match<T> childMatch =
                    matchPattern.getFirstMatchIfAny(child).get().withMatch(match).get();

            result =
                    (T)
                            result.transform(
                                    this.path.clone().child(childrenIndex),
                                    this.replacePattern.createTree(childMatch));

            childrenIndex++;
        }

        return result;
    }

    @Override
    public ChildrenPatternTransformation<T> getInverse() {

        return new ChildrenPatternTransformation<>(
                path, this.outerPattern, this.replacePattern, this.matchPattern);
    }

    @Override
    public ChildrenPatternTransformation<T> forPath(TreePath path) {

        return new ChildrenPatternTransformation<>(
                path, this.outerPattern, this.matchPattern, this.replacePattern);
    }
}
