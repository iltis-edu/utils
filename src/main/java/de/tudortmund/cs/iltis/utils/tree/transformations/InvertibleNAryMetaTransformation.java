package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import java.util.Collection;
import java.util.Collections;

/** Invertible meta transformation containing several invertible n-ary transformations */
public class InvertibleNAryMetaTransformation<T extends Tree<T>> extends NAryMetaTransformation<T>
        implements InvertibleNAryTransformation<T> {

    public InvertibleNAryMetaTransformation() { // Serialization
    }

    public InvertibleNAryMetaTransformation(
            Collection<TreePath> paths, InvertibleNAryTransformation<T>... transformations) {
        super(paths, transformations);
    }

    public InvertibleNAryMetaTransformation(InvertibleNAryTransformation<T>... transformations) {
        super(Collections.emptyList(), transformations);
    }

    @Override
    public InvertibleNAryMetaTransformation<T> forPaths(Collection<TreePath> paths) {
        return new InvertibleNAryMetaTransformation<>(paths, this.getTransformationArray());
    }

    @Override
    public InvertibleNAryMetaTransformation<T> forPath(final TreePath path) {
        return new InvertibleNAryMetaTransformation<>(
                Collections.singleton(path), this.getTransformationArray());
    }

    @Override
    public InvertibleNAryMetaTransformation<T> getInverse() {
        final int N = this.transformations.size();
        InvertibleNAryTransformation<T>[] invTransformations = new InvertibleNAryTransformation[N];
        for (int i = 0; i < N; i++) {
            InvertibleNAryTransformation<T> trans =
                    (InvertibleNAryTransformation<T>) this.transformations.get(i);
            InvertibleNAryTransformation<T> invTrans = trans.getInverse();
            invTransformations[i] = invTrans;
        }
        return new InvertibleNAryMetaTransformation<T>(invTransformations);
    }

    @Override
    protected InvertibleNAryMetaTransformation<T>[] getTransformationArray() {
        return this.transformations.toArray(new InvertibleNAryMetaTransformation[0]);
    }
}
