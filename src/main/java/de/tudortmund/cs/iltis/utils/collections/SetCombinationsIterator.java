package de.tudortmund.cs.iltis.utils.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

/**
 * An iterator that iterates over all sets that can be built by taking one element of each of n
 * sets.
 *
 * <p><b>Example:</b> Let the sets {1,2}, {3} and {4,5} be given. Then this iterator iterates over
 * the elements {1,3,4}, {1,3,5}, {2,3,4} and {2,3,5} in no particular order.
 *
 * @param <T> the type of the elements in these n sets
 */
public class SetCombinationsIterator<T> extends PreCalcIterator<Set<T>> {

    protected List<ResettableIterator<T>> setIterators;
    protected List<T> curList;

    public SetCombinationsIterator(@NotNull Collection<Set<T>> sets) {
        setIterators =
                sets.stream()
                        .map(Set::iterator)
                        .map(CashedIterator::new)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

    /** for GWT serialization */
    private SetCombinationsIterator() {}

    public static <T> SetCombinationsIterator<T> fromIterators(
            @NotNull Collection<Iterator<T>> iterators) {
        SetCombinationsIterator<T> it = new SetCombinationsIterator<>();
        it.setIterators =
                iterators.stream()
                        .map(CashedIterator::new)
                        .collect(Collectors.toCollection(ArrayList::new));
        return it;
    }

    @Override
    protected boolean init() {
        boolean hasAElement = setIterators.stream().allMatch(Iterator::hasNext);

        if (!hasAElement) return false;

        curList = new ArrayList<>(setIterators.size());
        for (Iterator<T> it : setIterators) {
            curList.add(it.next());
        }

        return true;
    }

    @Override
    protected Set<T> calculateFirst() {
        return new ListSet<>(curList);
    }

    @Override
    public Set<T> calculateNext() {
        for (int i = 0; i < setIterators.size(); i++) {
            if (setIterators.get(i).hasNext()) {
                curList.set(i, setIterators.get(i).next());
                return new ListSet<>(curList);
            }
            setIterators.get(i).reset();
            curList.set(i, setIterators.get(i).next());
        }
        return null;
    }
}
