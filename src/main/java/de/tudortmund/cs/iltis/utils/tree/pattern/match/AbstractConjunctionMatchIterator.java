package de.tudortmund.cs.iltis.utils.tree.pattern.match;

import de.tudortmund.cs.iltis.utils.collections.PreCalcIterator;
import de.tudortmund.cs.iltis.utils.collections.SetCombinationsIterator;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.Optional;
import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * Abstract class which implements the workflow to create a conjunction of a given set of matches.
 *
 * <p>All methods handle abstract {@link Match}es, not {@link TreeMatch}es. The benefit of this
 * class is a clean handling of negatively and positively defined forests. Uses the {@link
 * SetCombinationsIterator}
 *
 * @param <MatchedT> the type of tree to be matched
 */
public abstract class AbstractConjunctionMatchIterator<MatchedT extends Tree<MatchedT>>
        extends PreCalcIterator<Match<MatchedT>> {

    protected SetCombinationsIterator<Match<MatchedT>> it;

    public AbstractConjunctionMatchIterator(@NotNull Set<Set<Match<MatchedT>>> matches) {
        it = new SetCombinationsIterator<>(matches);
    }

    protected AbstractConjunctionMatchIterator() {}

    @Override
    protected Match<MatchedT> calculateNext() {
        Optional<? extends Match<MatchedT>> opMatch = Optional.empty();

        while (it.hasNext() && !opMatch.isPresent()) {
            opMatch = createMatch(it.next());
        }

        if (opMatch.isPresent()) return opMatch.get();
        return null;
    }

    protected Optional<? extends Match<MatchedT>> createMatch(
            Iterable<Match<MatchedT>> matchesToCombine) {

        Optional<? extends Match<MatchedT>> match = Optional.of(createEmptyMatch());

        for (Match<MatchedT> matchToAdd : matchesToCombine) {
            match = match.get().withMatch(matchToAdd);
            if (!match.isPresent()) break;
        }

        return match;
    }

    protected abstract Match<MatchedT> createEmptyMatch();
}
