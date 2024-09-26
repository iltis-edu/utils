package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.collections.ReverseListIterator;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

public class InvertibleSequentialTransformation<T extends Tree<T>>
        extends SequentialTransformation<T> implements InvertibleTransformation<T> {

    // needed for serialization
    public InvertibleSequentialTransformation() {
        super();
    }

    public InvertibleSequentialTransformation(InvertibleTransformation<T>... transformations) {
        this(new TreePath(), transformations);
    }

    public InvertibleSequentialTransformation(
            TreePath path, InvertibleTransformation<T>... transformations) {

        super(path, transformations);
    }

    @Override
    public InvertibleSequentialTransformation<T> getInverse() {
        ReverseListIterator<Transformation<T>> it = new ReverseListIterator<>(transformations);
        List<InvertibleTransformation<T>> reverseTransformations = new ArrayList<>();

        while (it.hasNext()) {

            reverseTransformations.add(((InvertibleTransformation<T>) it.next()).getInverse());
        }

        return new InvertibleSequentialTransformation<>(
                path, reverseTransformations.toArray(new InvertibleTransformation[0]));
    }

    @Override
    public InvertibleSequentialTransformation<T> forPath(final TreePath path) {

        return new InvertibleSequentialTransformation<>(
                path, (InvertibleTransformation<T>[]) this.getTransformationArray());
    }
}
