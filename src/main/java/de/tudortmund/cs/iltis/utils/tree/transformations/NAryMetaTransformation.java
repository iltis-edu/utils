package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/** Meta transformation containing several n-ary transformations */
public class NAryMetaTransformation<T extends Tree<T>> extends MetaTransformation<T>
        implements NAryTransformation<T> {

    public NAryMetaTransformation() { // Serialization
    }

    public NAryMetaTransformation(
            Collection<TreePath> paths, NAryTransformation<T>... transformations) {
        this.transformations = new ArrayList<>();
        for (NAryTransformation<T> transformation : transformations)
            this.transformations.add(transformation.forPaths(paths));
    }

    public NAryMetaTransformation(NAryTransformation<T>... transformations) {
        this(Collections.emptyList(), transformations);
    }

    @Override
    public NAryTransformation<T> forPaths(Collection<TreePath> paths) {
        return new NAryMetaTransformation<>(
                paths, transformations.toArray(new NAryTransformation[transformations.size()]));
    }
}
