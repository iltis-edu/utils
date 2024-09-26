package de.tudortmund.cs.iltis.utils.tree;

import java.util.Set;
import java.util.TreeSet;

public abstract class SetDefaultTraversalStrategy<I, O>
        extends DefaultTraversalStrategy<I, Set<O>, Set<O>> {

    /**
     * @return The new collected value, never null.
     */
    @Override
    protected Set<O> collect(Set<O> collectedValue, Set<O> nextValue) {
        if (collectedValue == null) {
            if (nextValue == null) return new TreeSet<>();
            else return nextValue;
        } else {
            if (nextValue != null) collectedValue.addAll(nextValue);
            return collectedValue;
        }
    }
}
