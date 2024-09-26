package de.tudortmund.cs.iltis.utils.tree;

import java.util.List;
import java.util.Optional;

/**
 * By using this interface it can be specified what shall be done while (post order) traversing a
 * tree.
 *
 * @param <I> type of input on each node.
 * @param <O> type of output of each node.
 */
public interface TraversalStrategy<I, O> {
    /**
     * Specifies the output of item. The output of all of item's children, which were previously
     * traversed, can be used. Is to be called in post-order after an item and all of its children
     * are traversed.
     *
     * @param item The item traversed lately.
     * @param childrenOutput The output of all the children of item.
     * @return An output for this item.
     */
    O inspect(final I item, final List<O> childrenOutput);

    /**
     * Specifies the output of item. The output of those of item's children, which were previously
     * traversed, can be used. Is to be called in post-order after <em>any child</em> of the current
     * item has been inspected before {@link #nextSibling()} is called.
     *
     * <p>If a value different from {@link Optional#empty()} is returned, the remaining children do
     * not get traversed anymore.
     *
     * @param item The item traversed lately.
     * @param childrenOutput The output of the previously inspected children of item which may not
     *     be all cildren.
     * @return An output for this item.
     */
    default Optional<O> preinspect(final I item, final List<O> childrenOutput) {
        return Optional.empty();
    }

    /**
     * Specifies what shall be done before traversing item. Is to be called in pre-order.
     *
     * @param item The item to be traversed next.
     */
    default void visit(final I item) {}

    /**
     * Specifies what shall be done when entering a deeper nesting level. Is to be called after
     * visiting the current item even if it does not have children.
     */
    default void nextLevel() {}

    /**
     * Specifies what shall be done when returning from a deeper nesting level. Is to be called
     * after inspecting all children of an item, even if it does not have any children.
     */
    default void previousLevel() {}

    /**
     * Specifies what shall be done after inspecting an item. Is to be called after inspecting the
     * current item and all of its children even if it does not have any siblings.
     */
    default void nextSibling() {}
}
