package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.term.pattern.NamePattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;

/**
 * Exception thrown when the creation of a tree according to the information stored in a {@link
 * Match} object fails.
 */
public class CreateException extends RuntimeException {

    protected NamePattern<?, ?> namePattern;
    protected TreePattern<?> treePattern;
    protected Match<?> match;

    public CreateException() {
        super();
    }

    public CreateException(String message) {
        super(message);
    }

    public CreateException(String variableId, Match<?> match) {
        super("Variable '" + variableId + "' not defined in match '" + match + "'");
    }

    public CreateException(TreePattern<?> pattern, Match<?> match) {
        super("Cannot create forest from pattern '" + pattern + "' with match '" + match + "'");
        this.treePattern = pattern;
        this.match = match;
    }

    public CreateException(NamePattern<?, ?> pattern, Match<?> match) {
        super("Cannot create name from pattern '" + pattern + "' with match '" + match + "'");
        this.namePattern = pattern;
        this.match = match;
    }
}
