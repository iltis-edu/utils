package de.tudortmund.cs.iltis.utils.tree.pattern.match;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.collections.Either;
import de.tudortmund.cs.iltis.utils.collections.Pair;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.general.Data;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

/**
 * An iterator that extends {@link AbstractNegationMatchIterator} to implement the negation of a set
 * of matches. It creates new {@link TreeMatch}es and iterates over them.
 *
 * @param <MatchedT> the type of tree to be matched
 */
public class NegationTreeMatchIterator<MatchedT extends Tree<MatchedT>>
        extends AbstractNegationMatchIterator<
                MatchedT,
                Pair<
                        List<MatchedT>,
                        SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>>> {

    /**
     * Creates a new negation tree match iterator which iterates over the matches that arise of
     * negating the given set of matches.
     *
     * @param matches the set of matches to negate
     */
    public NegationTreeMatchIterator(@NotNull final Set<? extends Match<MatchedT>> matches) {
        super(matches);
    }

    /** {@inheritDoc} */
    @Override
    protected Set<
                    Pair<
                            IndexedSymbol,
                            Either<
                                    Pair<
                                            List<MatchedT>,
                                            SerializableBiFunction<
                                                    List<MatchedT>, List<MatchedT>, Boolean>>,
                                    Pair<
                                            List<MatchedT>,
                                            SerializableBiFunction<
                                                    List<MatchedT>, List<MatchedT>, Boolean>>>>>
            makeSinglePositivePairs(Match<MatchedT> match) {

        return match.getAllDefinedIds().stream()
                .map(
                        id ->
                                new Pair<
                                        IndexedSymbol,
                                        Either<
                                                Pair<
                                                        List<MatchedT>,
                                                        SerializableBiFunction<
                                                                List<MatchedT>,
                                                                List<MatchedT>,
                                                                Boolean>>,
                                                Pair<
                                                        List<MatchedT>,
                                                        SerializableBiFunction<
                                                                List<MatchedT>,
                                                                List<MatchedT>,
                                                                Boolean>>>>(
                                        id,
                                        Either.left(
                                                new Pair<>(
                                                        match.getDefinedForest(id).get(),
                                                        match.getEqTesterFor(id).get()))))
                .collect(Collectors.toSet());
    }

    /** {@inheritDoc} */
    @Override
    protected Set<
                    Pair<
                            IndexedSymbol,
                            Either<
                                    Pair<
                                            List<MatchedT>,
                                            SerializableBiFunction<
                                                    List<MatchedT>, List<MatchedT>, Boolean>>,
                                    Pair<
                                            List<MatchedT>,
                                            SerializableBiFunction<
                                                    List<MatchedT>, List<MatchedT>, Boolean>>>>>
            makeSingleNegativePairs(Match<MatchedT> match) {

        return match.getAllNegativelyDefinedIds().stream()
                .map(
                        id ->
                                match.getNegativelyDefined(id).get().stream()
                                        .map(
                                                negForest ->
                                                        new Pair<
                                                                IndexedSymbol,
                                                                Either<
                                                                        Pair<
                                                                                List<MatchedT>,
                                                                                SerializableBiFunction<
                                                                                        List<
                                                                                                MatchedT>,
                                                                                        List<
                                                                                                MatchedT>,
                                                                                        Boolean>>,
                                                                        Pair<
                                                                                List<MatchedT>,
                                                                                SerializableBiFunction<
                                                                                        List<
                                                                                                MatchedT>,
                                                                                        List<
                                                                                                MatchedT>,
                                                                                        Boolean>>>>(
                                                                id,
                                                                Either.right(
                                                                        new Pair<>(
                                                                                negForest,
                                                                                match.getEqTesterFor(
                                                                                                id)
                                                                                        .get()))))
                                        .collect(Collectors.toSet()))
                .flatMap(set -> set.stream())
                .collect(Collectors.toSet());
    }

    @Override
    protected Match<MatchedT> createEmptyMatch() {
        return new TreeMatch<>();
    }

    @Override
    protected Optional<? extends Match<MatchedT>> extendMatch(
            Match<MatchedT> match,
            IndexedSymbol id,
            Either<
                            Pair<
                                    List<MatchedT>,
                                    SerializableBiFunction<
                                            List<MatchedT>, List<MatchedT>, Boolean>>,
                            Pair<
                                    List<MatchedT>,
                                    SerializableBiFunction<
                                            List<MatchedT>, List<MatchedT>, Boolean>>>
                    content) {

        Either<List<MatchedT>, Set<List<MatchedT>>> toAdd;
        SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester;

        if (content.isLeft()) {
            toAdd = Either.left(content.getLeft().first());
            eqTester = content.getLeft().second();
        } else {
            toAdd = Either.right(Data.newListSet1(content.getRight().first()));
            eqTester = content.getRight().second();
        }

        return ((TreeMatch<MatchedT>) match).extendMatch(id, toAdd, eqTester);
    }

    /** for GWT serialization */
    @SuppressWarnings("unused")
    private NegationTreeMatchIterator() {}
}
