package de.tudortmund.cs.iltis.utils.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Iterator over every possible tuple with the given size and elements in the given universe.
 *
 * <p><b>Example:</b> If given the universe consists of the elements 0 and 1 and the tuple size
 * should be 2 then this iterator iterates over four tuples each of them representing one of the
 * possible combinations of 0 and 1, i.e. 00, 01, 10 and 11.
 *
 * <p>Note: for this iterator to be efficient it requires the given universe to be efficiently
 * iterable.
 *
 * @param <T> type of elements of the universe
 */
public class TupleIterator<T> extends PreCalcIterator<Tuple<T>> {

    private Iterable<T> universe;
    private int tupleSize;
    private List<Iterator<T>> universeIterators;
    private List<T> curTupleElements;

    /**
     * Creates a new iterators over tuples of the given size over the given universe.
     *
     * <p><b>Important:</b> The universe is neither cloned nor cached, so you have to make sure that
     * the universe does not change after being passed to this constructor. Otherwise the behavior
     * of this iterator is undefined.
     *
     * @param universe the universe to get the elements of the tuples from
     * @param tupleSize the size of the tuples to generate
     */
    public TupleIterator(Iterable<T> universe, int tupleSize) {
        if (tupleSize < 0)
            throw new IllegalArgumentException("tupleSize has to be positive or equal to zero");
        this.universe = universe;
        this.tupleSize = tupleSize;
    }

    /** for GWT serialization */
    private TupleIterator() {}

    @Override
    protected boolean init() {
        if (!universe.iterator().hasNext() && tupleSize != 0) return false;

        universeIterators = new ArrayList<>(tupleSize);
        curTupleElements = new ArrayList<>(tupleSize);
        for (int i = 0; i < tupleSize; i++) {
            universeIterators.add(universe.iterator());
            curTupleElements.add(universeIterators.get(i).next());
        }
        return true;
    }

    public void reset() {
        super.reset();
    }

    @Override
    protected Tuple<T> calculateFirst() {
        return new Tuple<>(curTupleElements);
    }

    @Override
    protected Tuple<T> calculateNext() {
        for (int i = tupleSize - 1; i >= 0; i--) {
            if (universeIterators.get(i).hasNext()) {
                curTupleElements.set(i, universeIterators.get(i).next());
                return new Tuple<>(curTupleElements);
            }
            universeIterators.set(i, universe.iterator());
            curTupleElements.set(i, universeIterators.get(i).next());
        }
        return null;
    }
}
