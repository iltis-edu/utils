package de.tudortmund.cs.iltis.utils.tree.pattern.match;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.collections.Either;
import de.tudortmund.cs.iltis.utils.collections.Pair;
import de.tudortmund.cs.iltis.utils.collections.PreCalcIterator;
import de.tudortmund.cs.iltis.utils.collections.SetCombinationsIterator;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

/**
 * Abstract class which implements the workflow to negate a set of matches.
 *
 * <p>All methods handle abstract {@link Match}es, not {@link TreeMatch}es. To be independent of
 * different (tree) match implementations and (above all) to improve readability the generic type
 * ContentT encapsulates the specific type of forests and equality functions. The benefit of this
 * class is a clean (and somewhat understandable) handling of negatively and positively defined
 * contents (forests). Uses the {@link SetCombinationsIterator}.
 *
 * @param <MatchedT> the type of tree to be matched
 * @param <ContentT> the type to encapsulate the specific type of forests and equality functions
 */
public abstract class AbstractNegationMatchIterator<MatchedT extends Tree<MatchedT>, ContentT>
        extends PreCalcIterator<Match<MatchedT>> {

    protected Set<? extends Match<MatchedT>> matches;
    private SetCombinationsIterator<Pair<IndexedSymbol, Either<ContentT, ContentT>>> it;
    private Match<MatchedT> resultIfEmpty;

    public AbstractNegationMatchIterator(@NotNull final Set<? extends Match<MatchedT>> matches) {
        this.matches = matches;
    }

    @Override
    protected boolean init() {
        if (matches.isEmpty()) {
            resultIfEmpty = createEmptyMatch();
        } else {
            Set<Set<Pair<IndexedSymbol, Either<ContentT, ContentT>>>> pairs =
                    matches.stream()
                            .map(match -> makeSinglePairs(match))
                            .map(
                                    set ->
                                            set.stream()
                                                    .map(
                                                            pair ->
                                                                    new Pair<>(
                                                                            pair.first(),
                                                                            negateSingleValue(
                                                                                    pair.second())))
                                                    .collect(Collectors.toSet()))
                            .collect(Collectors.toSet());
            it = new SetCombinationsIterator<>(pairs);
        }
        return true;
    }

    /** Make pairs (name, left: positive tree) or (name, right: set of exactly one negative tree) */
    protected Set<Pair<IndexedSymbol, Either<ContentT, ContentT>>> makeSinglePairs(
            Match<MatchedT> match) {
        Set<Pair<IndexedSymbol, Either<ContentT, ContentT>>> pos = makeSinglePositivePairs(match);
        pos.addAll(makeSingleNegativePairs(match));
        return pos;
    }

    /** Make pairs (name, left: positive tree) */
    protected abstract Set<Pair<IndexedSymbol, Either<ContentT, ContentT>>> makeSinglePositivePairs(
            Match<MatchedT> match);

    /** Make pairs (name, right: set of exactly one negative tree) */
    protected abstract Set<Pair<IndexedSymbol, Either<ContentT, ContentT>>> makeSingleNegativePairs(
            Match<MatchedT> match);

    @Override
    protected Match<MatchedT> calculateNext() {
        if (matches.isEmpty()) {
            Match<MatchedT> elemToReturn = resultIfEmpty;
            resultIfEmpty = null;
            return elemToReturn;
        }

        Optional<? extends Match<MatchedT>> opMatch = Optional.empty();

        while (it.hasNext() && !opMatch.isPresent()) {
            opMatch = createMatch(it.next());
        }

        if (opMatch.isPresent()) return opMatch.get();
        return null;
    }

    protected Optional<? extends Match<MatchedT>> createMatch(
            Iterable<Pair<IndexedSymbol, Either<ContentT, ContentT>>> names) {

        Optional<? extends Match<MatchedT>> match = Optional.of(createEmptyMatch());

        for (Pair<IndexedSymbol, Either<ContentT, ContentT>> pair : names) {
            match = extendMatch(match.get(), pair.first(), pair.second());
            if (!match.isPresent()) break;
        }

        return match;
    }

    protected abstract Match<MatchedT> createEmptyMatch();

    protected abstract Optional<? extends Match<MatchedT>> extendMatch(
            Match<MatchedT> match, IndexedSymbol id, Either<ContentT, ContentT> content);

    private Either<ContentT, ContentT> negateSingleValue(Either<ContentT, ContentT> value) {

        if (value.isLeft()) return Either.right(value.getLeft());
        return Either.left(value.getRight());
    }

    /** for GWT serialization */
    protected AbstractNegationMatchIterator() {}
}
