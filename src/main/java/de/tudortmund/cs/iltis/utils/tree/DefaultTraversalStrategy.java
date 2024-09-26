package de.tudortmund.cs.iltis.utils.tree;

import java.util.List;

/**
 * A default implementation of a traversal strategy, that simplifies usage by offering {@link
 * #collect(Object, Object)} for <b>folding</b> an item's children values to an intermediate result
 * and {@link #value(Object, Object)} for creating an output thereof.
 *
 * @param <I> type of input on each node.
 * @param <M> type of intermediate result while calculating output.
 * @param <O> type of output of each node.
 */
public abstract class DefaultTraversalStrategy<I, M, O> implements TraversalStrategy<I, O> {

    /**
     * Calculates the output of item by connecting childrensValues with {@link #collect(Object,
     * Object)} and finally calling {@link #value(Object, Object)} to transform the collected value
     * into an output.
     */
    @Override
    public final O inspect(I item, List<O> childrensValues) {
        M collectedValue = this.collect(null, null);
        for (O nextValue : childrensValues)
            collectedValue = this.collect(collectedValue, nextValue);
        return this.value(collectedValue, item);
    }

    /**
     * Calculates an output value from the previously collected value.
     *
     * @param collectedValue The previously collected value.
     * @param item The item to calculate the output from.
     * @return The output for item.
     */
    protected abstract O value(M collectedValue, I item);

    /**
     * Adds nextValue to the previous collectedValue.
     *
     * @param collectedValue The The previous collected value.
     * @param nextValue The output to add to collectedValue.
     * @return The new collected value.
     */
    protected abstract M collect(M collectedValue, O nextValue);
}
