package de.tudortmund.cs.iltis.utils.tree;

public class TreePathException extends RuntimeException {

    public TreePathException(final String message) {
        super(message);
    }

    /** For serialization */
    @SuppressWarnings("unused")
    private TreePathException() {}
}
