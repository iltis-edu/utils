package de.tudortmund.cs.iltis.utils.tree;

import java.util.Iterator;

/** A preorder iterator for trees. */
public class PreorderIterator<T extends Tree<? extends T>> implements Iterator<T> {
    /** The current node whose children are traversed. root is never null. */
    private T root;

    /** The number of the next child tree, which is going to be traversed. */
    private int nextChildNo;

    /**
     * The {@link PreorderIterator} for the current child. childIterator is null iff there are no
     * further children to traverse.
     */
    private PreorderIterator<? extends T> childIterator;

    /** Takes the element which is to be returned at the next call of {@link #next()}. */
    private T nextElement;

    public PreorderIterator(T root) {
        if (root == null) throw new IllegalArgumentException("root may not be null");
        this.root = root;
        nextChildNo = 0;
        setNextChildIterator();
        nextElement = root;
    }

    @Override
    public boolean hasNext() {
        return nextElement != null;
    }

    @Override
    public T next() {
        T nodeToReturn = nextElement;

        if (childIterator != null) {
            if (!childIterator.hasNext())
                // if there is a child to traverse but its iterator has no more elements,
                // then we have to continue with the next child
                setNextChildIterator();

            if (childIterator != null)
                // if there is a next child we can traverse,
                // call its iterator -- this iterator has to have a next element (its root)
                nextElement = childIterator.next();
            else nextElement = null;
        } else {
            nextElement = null;
        }

        return nodeToReturn;
    }

    private void setNextChildIterator() {
        if (nextChildNo < root.getNumberOfChildren())
            childIterator = new PreorderIterator<T>(root.getChild(nextChildNo++));
        else childIterator = null;
    }
}
