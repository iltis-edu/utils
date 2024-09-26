package de.tudortmund.cs.iltis.utils.tree;

import java.util.Iterator;

/** A postorder iterator for trees. */
public class PostorderIterator<T extends Tree<? extends T>> implements Iterator<T> {
    /**
     * The current node whose children are traversed. root is null iff the root itself has been
     * traversed and therefore all children have been traversed as well.
     */
    private T root;

    /** The number of the next child tree, which is going to be traversed. */
    private int nextChildNo;

    /**
     * The PostorderIterator for the current child. childIterator is null iff there are no further
     * children to traverse.
     */
    private PostorderIterator<? extends T> childIterator;

    public PostorderIterator(T root) {
        if (root == null) throw new IllegalArgumentException("root may not be null");
        this.root = root;
        this.nextChildNo = 0;
        this.setNextChildIterator();
    }

    @Override
    public boolean hasNext() {
        return this.root != null;
    }

    @Override
    public T next() {
        if (this.childIterator != null) {
            if (!this.childIterator.hasNext())
                // if there is a child to traverse but its iterator has no more elements,
                // then we have to continue with the next child
                this.setNextChildIterator();

            if (this.childIterator != null)
                // if there is a next child we can traverse,
                // call its iterator -- this iterator has to have a next element (its root)
                return this.childIterator.next();
        }
        // traverse root itself and set end-flag (root=null)
        T node = this.root;
        this.root = null;
        return node;
    }

    private void setNextChildIterator() {
        if (this.nextChildNo < this.root.getNumberOfChildren())
            this.childIterator = new PostorderIterator<T>(this.root.getChild(this.nextChildNo++));
        else this.childIterator = null;
    }
}
