package de.tudortmund.cs.iltis.utils.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * This iterator splits a given list into a fixed number of sublist that respect the order of the
 * elements in the original list. For instance, a 3-element integer list [1,2,3] is split into the
 * following 2-partition
 *
 * <p>1) [[1,2,3], []]; 2) [[1,2], [3]]; 3) [[1], [2,3]]; 4) [[], [1,2,3]].
 *
 * <p>A empty list is split in a partition with the required number of empty sublists. Splitting in
 * a 0-partition will only return the empty partition (i.e. the empty list).
 *
 * @param <T> the type for elements in the list to partition
 */
public class SequencePartitionIterator<T> extends PreCalcIterator<List<List<T>>> {

    /**
     * Creates a new {@link SequencePartitionIterator} with the given list and the given number of
     * sublists.
     *
     * @param list the list to create the partition from, is cloned
     * @param noPartitions the number of sublists to have a in a partition
     */
    public SequencePartitionIterator(List<T> list, final int noPartitions) {
        if (noPartitions < 0) throw new IllegalArgumentException();
        this.list = new ArrayList<>(list);
        this.noPartitions = noPartitions;
    }

    @Override
    protected boolean init() {
        sublist = new ArrayList<>();
        if (noPartitions >= 1) sublist.addAll(list);
        if (noPartitions > 1)
            restIt = new SequencePartitionIterator<>(new ArrayList<>(), noPartitions - 1);
        else restIt = null;
        return true;
    }

    @Override
    protected List<List<T>> calculateFirst() {
        List<List<T>> newRightPartition;

        // equivalent to noPartitions == 0 on first call
        if (restIt == null) newRightPartition = new ArrayList<>();
        else
            // every SequencePartitionIterator has at least one element
            newRightPartition = restIt.next();

        List<List<T>> newPartition = new ArrayList<>();
        // if zero sublists: return empty list
        if (noPartitions >= 1)
            // new list to keep sublist internal
            newPartition.add(new ArrayList<>(sublist));
        newPartition.addAll(newRightPartition);
        return newPartition;
    }

    @Override
    protected List<List<T>> calculateNext() {
        List<List<T>> newRightPartition;

        if (restIt != null && restIt.hasNext()) {
            newRightPartition = restIt.next();
        } else if (restIt != null && !sublist.isEmpty()) {
            final int lastIndex = this.sublist.size() - 1;
            sublist.remove(lastIndex);
            List<T> remaining = list.subList(lastIndex, list.size());
            restIt = new SequencePartitionIterator<>(remaining, noPartitions - 1);
            // every SequencePartitionIterator has at least one element
            newRightPartition = restIt.next();
        } else {
            return null;
        }

        List<List<T>> newPartition = new ArrayList<>();
        // new list to keep sublist internal
        newPartition.add(new ArrayList<>(sublist));
        newPartition.addAll(newRightPartition);
        return newPartition;
    }

    private final List<T> list;
    private List<T> sublist;
    private final int noPartitions;
    private SequencePartitionIterator<T> restIt;

    /** for GWT serializability */
    @SuppressWarnings("unused")
    private SequencePartitionIterator() {
        super();
        noPartitions = 0;
        list = null;
    }
}
