package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;

public class InvertibleMetaTransformation<T extends Tree<T>> extends MetaTransformation<T>
        implements InvertibleTransformation<T> {

    public InvertibleMetaTransformation() {}

    public InvertibleMetaTransformation(
            TreePath path, InvertibleTransformation<T>... transformations) {
        super(path, transformations);
    }

    public InvertibleMetaTransformation(InvertibleTransformation<T>... transformations) {
        super(new TreePath(), transformations);
    }

    public InvertibleMetaTransformation<T> forPath(final TreePath path) {
        return new InvertibleMetaTransformation<>(path, this.getTransformationArray());
    }

    @Override
    public InvertibleMetaTransformation<T> getInverse() {
        final int N = this.transformations.size();
        InvertibleTransformation<T>[] invTransformations = new InvertibleTransformation[N];
        for (int i = 0; i < N; i++) {
            InvertibleTransformation<T> trans =
                    (InvertibleTransformation<T>) this.transformations.get(i);
            InvertibleTransformation<T> invTrans = (InvertibleTransformation<T>) trans.getInverse();
            invTransformations[i] = invTrans;
        }
        return new InvertibleMetaTransformation<>(invTransformations);
    }

    @Override
    protected InvertibleTransformation<T>[] getTransformationArray() {
        return this.transformations.toArray(new InvertibleTransformation[0]);
    }
}
