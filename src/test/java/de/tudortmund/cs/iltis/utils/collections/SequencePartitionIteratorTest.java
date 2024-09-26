package de.tudortmund.cs.iltis.utils.collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class SequencePartitionIteratorTest {

    public SequencePartitionIteratorTest() {
        this.three = new ArrayList<Integer>();
        this.three.add(1);
        this.three.add(2);
        this.three.add(3);
    }

    @Test
    public void test0Partitions() {
        SequencePartitionIterator<Integer> it =
                new SequencePartitionIterator<Integer>(new ArrayList<Integer>(), 0);
        List<List<Integer>> partition;

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(partition.isEmpty());
        System.out.println();

        assertFalse(it.hasNext());
    }

    @Test
    public void test1Partition() {
        SequencePartitionIterator<Integer> it =
                new SequencePartitionIterator<Integer>(this.three, 1);
        List<List<Integer>> partition;

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(1, 2, 3), partition.get(0)));
        System.out.println();

        assertFalse(it.hasNext());
    }

    @Test
    public void test1EmptyPartition() {
        SequencePartitionIterator<Integer> it =
                new SequencePartitionIterator<Integer>(newList(), 1);
        List<List<Integer>> partition;

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(), partition.get(0)));
        System.out.println();

        assertFalse(it.hasNext());
    }

    @Test
    public void test2Partitions() {
        SequencePartitionIterator<Integer> it =
                new SequencePartitionIterator<Integer>(this.three, 2);
        List<List<Integer>> partition;

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(1, 2, 3), partition.get(0)));
        assertTrue(listEquals(newList(), partition.get(1)));
        System.out.println();

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(1, 2), partition.get(0)));
        assertTrue(listEquals(newList(3), partition.get(1)));
        System.out.println();

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(1), partition.get(0)));
        assertTrue(listEquals(newList(2, 3), partition.get(1)));
        System.out.println();

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(), partition.get(0)));
        assertTrue(listEquals(newList(1, 2, 3), partition.get(1)));
        System.out.println();

        assertFalse(it.hasNext());
    }

    @Test
    public void test2EmptyPartition() {
        SequencePartitionIterator<Integer> it =
                new SequencePartitionIterator<Integer>(newList(), 2);
        List<List<Integer>> partition;

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(), partition.get(0)));
        assertTrue(listEquals(newList(), partition.get(1)));
        System.out.println();

        assertFalse(it.hasNext());
    }

    @Test
    public void test3Partitions() {
        SequencePartitionIterator<Integer> it =
                new SequencePartitionIterator<Integer>(this.three, 3);
        List<List<Integer>> partition;

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(1, 2, 3), partition.get(0)));
        assertTrue(listEquals(newList(), partition.get(1)));
        assertTrue(listEquals(newList(), partition.get(2)));
        System.out.println();

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(1, 2), partition.get(0)));
        assertTrue(listEquals(newList(3), partition.get(1)));
        assertTrue(listEquals(newList(), partition.get(2)));
        System.out.println();

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(1, 2), partition.get(0)));
        assertTrue(listEquals(newList(), partition.get(1)));
        assertTrue(listEquals(newList(3), partition.get(2)));
        System.out.println();

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(1), partition.get(0)));
        assertTrue(listEquals(newList(2, 3), partition.get(1)));
        assertTrue(listEquals(newList(), partition.get(2)));
        System.out.println();

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(1), partition.get(0)));
        assertTrue(listEquals(newList(2), partition.get(1)));
        assertTrue(listEquals(newList(3), partition.get(2)));
        System.out.println();

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(1), partition.get(0)));
        assertTrue(listEquals(newList(), partition.get(1)));
        assertTrue(listEquals(newList(2, 3), partition.get(2)));
        System.out.println();

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(), partition.get(0)));
        assertTrue(listEquals(newList(1, 2, 3), partition.get(1)));
        assertTrue(listEquals(newList(), partition.get(2)));
        System.out.println();

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(), partition.get(0)));
        assertTrue(listEquals(newList(1, 2), partition.get(1)));
        assertTrue(listEquals(newList(3), partition.get(2)));
        System.out.println();

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(), partition.get(0)));
        assertTrue(listEquals(newList(1), partition.get(1)));
        assertTrue(listEquals(newList(2, 3), partition.get(2)));
        System.out.println();

        assertTrue(it.hasNext());
        partition = it.next();
        assertTrue(listEquals(newList(), partition.get(0)));
        assertTrue(listEquals(newList(), partition.get(1)));
        assertTrue(listEquals(newList(1, 2, 3), partition.get(2)));
        System.out.println();

        assertFalse(it.hasNext());
    }

    private boolean listEquals(List<Integer> exp, List<Integer> act) {
        if (exp.size() != act.size()) return false;
        for (int i = 0; i < exp.size(); i++) if (exp.get(i) != act.get(i)) return false;
        return true;
    }

    private List<Integer> newList(Integer... values) {
        List<Integer> list = new ArrayList<Integer>();
        for (int value : values) list.add(value);
        return list;
    }

    private List<Integer> three;
}
