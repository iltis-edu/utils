package de.tudortmund.cs.iltis.utils.collections;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/** Tests for {@link TupleIterator}. */
@RunWith(Parameterized.class)
public class TupleIteratorTest {
    private Iterable<Integer> universe;
    private int tupleSize;
    private List<Tuple<Integer>> solution;

    public TupleIteratorTest(
            Iterable<Integer> universe, int tupleSize, List<Tuple<Integer>> solution) {
        this.universe = universe;
        this.tupleSize = tupleSize;
        this.solution = solution;
    }

    @Parameterized.Parameters
    public static Collection<?> parameters() {
        return Arrays.asList(
                new Object[][] {
                    {
                        /* universe */ Collections.emptyList(),
                        /* tupleSize */ 0,
                        /* solution */ Collections.singletonList(new Tuple<>())
                    },
                    {
                        /* universe */ Arrays.asList(1, 2, 3),
                        /* tupleSize */ 0,
                        /* solution */ Collections.singletonList(new Tuple<>())
                    },
                    {
                        /* universe */ Collections.emptyList(),
                        /* tupleSize */ 1,
                        /* solution */ Collections.emptyList()
                    },
                    {
                        /* universe */ Arrays.asList(1, 2, 3),
                        /* tupleSize */ 1,
                        /* solution */ Arrays.asList(new Tuple<>(1), new Tuple<>(2), new Tuple<>(3))
                    },
                    {
                        /* universe */ Arrays.asList(1, 2, 3),
                        /* tupleSize */ 2,
                        /* solution */ Arrays.asList(
                                new Tuple<>(1, 1),
                                new Tuple<>(1, 2),
                                new Tuple<>(1, 3),
                                new Tuple<>(2, 1),
                                new Tuple<>(2, 2),
                                new Tuple<>(2, 3),
                                new Tuple<>(3, 1),
                                new Tuple<>(3, 2),
                                new Tuple<>(3, 3))
                    }
                });
    }

    @Test
    public void test() {
        TupleIterator<Integer> tupleIterator = new TupleIterator<>(universe, tupleSize);
        assertIteratorOutputEqualToList(tupleIterator, solution);
    }

    private static <T> void assertIteratorOutputEqualToList(
            Iterator<T> toTest, List<T> solutionItems) {
        List<T> toTestItems = new ArrayList<>();
        toTest.forEachRemaining(toTestItems::add);
        assertEquals(toTestItems, solutionItems);
    }
}
