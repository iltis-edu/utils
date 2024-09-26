package de.tudortmund.cs.iltis.utils.term.pattern;

import de.tudortmund.cs.iltis.utils.term.Term;
import de.tudortmund.cs.iltis.utils.tree.pattern.CreateException;
import de.tudortmund.cs.iltis.utils.tree.pattern.TreePattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import java.io.Serializable;
import java.util.Optional;

/**
 * An empty interface indicating that this pattern matches a term according to its name.
 *
 * @see TreePattern
 */
public interface NamePattern<
                MatchedT extends Term<MatchedT, NameT>,
                NameT extends Comparable<NameT> & Serializable>
        extends Serializable {

    /**
     * Creates a name from the given match.
     *
     * <p>One of the methods {@link #createName(Match)} and {@link #createNameIfPossible(Match)}
     * needs to be overridden in implementing classes.
     *
     * @param match the match object which contains all needed information to create a name
     * @return the created name
     * @throws CreateException if the match object does not provide enough information to create a
     *     name
     */
    default NameT createName(Match<MatchedT> match) throws CreateException {
        Optional<NameT> optName = createNameIfPossible(match);

        if (optName.isPresent()) {
            return optName.get();
        }

        throw new CreateException(this, match);
    }

    /**
     * Creates a name from the given match.
     *
     * <p>One of the methods {@link #createName(Match)} and {@link #createNameIfPossible(Match)}
     * needs to be overridden in implementing classes.
     *
     * @param match the match object which contains all needed information to create a name
     * @return the created name or empty if the match object does not provide enough information to
     *     create a name
     */
    default Optional<NameT> createNameIfPossible(Match<MatchedT> match) {
        try {
            return Optional.of(createName(match));
        } catch (CreateException e) {
            return Optional.empty();
        }
    }
}
