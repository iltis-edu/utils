package de.tudortmund.cs.iltis.utils.tree.pattern.match;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Match iterators are the central element to do pattern matching. A match iterator knows a match
 * object (possibly empty but not null) and a forest (or tree = forest of size 1) and their central
 * task is to iterate over all matches that can be generated from the given tree taking the given
 * match object as a basis.
 *
 * <p>This class is an abstract basis class holding only references to a forest and a match object
 * but providing no logic. Subclasses implement the required methods for an iterator according to a
 * specific pattern.
 *
 * @param <MatchedT> the type of the trees to match
 */
public abstract class MatchIterator<MatchedT extends Tree<MatchedT>>
        implements Iterator<Match<MatchedT>> {

    protected List<MatchedT> forest;
    protected Match<MatchedT> match;

    public MatchIterator(@NotNull Match<MatchedT> match, @NotNull List<MatchedT> forest) {
        this.forest = forest;
        this.match = match;
    }

    public MatchIterator(@NotNull Match<MatchedT> match, @NotNull MatchedT tree) {
        this.forest = Arrays.asList(tree);
        this.match = match;
    }

    public List<MatchedT> getForest() {
        return forest;
    }

    /**
     * @return the first tree of the saved forest or null if the saved forest is empty
     */
    public MatchedT getTree() {
        if (forest.isEmpty()) return null;
        return forest.get(0);
    }

    public Match<MatchedT> getMatch() {
        return match;
    }

    /** for serialization */
    protected MatchIterator() {}
}
