package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransformationUnapplicable extends RuntimeException {

    private Tree<?> tree;
    private List<TreePath> paths;

    public TransformationUnapplicable(final Tree<?> formula, final TreePath... paths) {
        this.tree = formula;
        this.paths = new ArrayList<>();
        this.paths.addAll(Arrays.asList(paths));
    }

    public Tree<?> getTree() {
        return tree;
    }

    public List<TreePath> getPaths() {
        return paths;
    }

    /** For serialization */
    @SuppressWarnings("unused")
    private TransformationUnapplicable() {}
}
