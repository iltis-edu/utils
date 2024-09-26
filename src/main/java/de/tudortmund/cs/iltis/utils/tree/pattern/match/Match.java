package de.tudortmund.cs.iltis.utils.tree.pattern.match;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * An interface representing a match, i.e. a mapping of forests (i.e. a list of trees) to ids.
 *
 * <p>This interface supports positive and negative mappings, which is useful in combination with
 * pattern matching. For one id there can be at most one positive forest (the one that is matched by
 * this id by some pattern) and a set of negative forests (the forests which are excluded by some
 * patterns). Additionally, for each id a different equality tester can be defined, which can be
 * used to determine whether two forests are equal and therefore to determine whether two forest for
 * the same id are compatible.
 *
 * @param <MatchedT> the type of tree to map to ids
 */
public interface Match<MatchedT extends Tree<MatchedT>> extends Serializable {

    /**
     * Returns, whether a forest is positively defined for the given id.
     *
     * @param id the id to check for
     * @return true iff a forest is positively defined for the given id
     */
    boolean defines(IndexedSymbol id);

    /**
     * Returns, whether a forest is negatively defined for the given id.
     *
     * @param id the id to check for
     * @return true iff a forest is negatively defined for the given id
     */
    boolean definesNegatively(IndexedSymbol id);

    /**
     * Returns the forest positively defined for the given id.
     *
     * @param id the id
     * @return the positively defined forest or empty, if no forest is positively defined for this
     *     id
     */
    Optional<List<MatchedT>> getDefinedForest(IndexedSymbol id);

    /**
     * Returns the tree positively defined for the given id. If a forest is defined for this id, the
     * first tree is returned.
     *
     * @param id the id
     * @return the defined tree or empty, if no tree is positively defined for this id
     */
    default Optional<MatchedT> getDefinedTree(IndexedSymbol id) {
        Optional<List<MatchedT>> result = getDefinedForest(id);
        if (result.isPresent() && !result.get().isEmpty()) return Optional.of(result.get().get(0));
        return Optional.empty();
    }

    /**
     * Returns the set of forests negatively defined for the given id.
     *
     * @param id the id
     * @return the defined set of forest or empty, if no forest is negatively defined for this id
     */
    Optional<? extends Set<List<MatchedT>>> getNegativelyDefined(IndexedSymbol id);

    /**
     * Returns the equality tester defined for the given id.
     *
     * @param id the id
     * @return the defined equality tester or empty, if no equality tester is defined for this id,
     *     i.e. if no forest is positively or negatively defined for this id
     */
    Optional<SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>> getEqTesterFor(
            IndexedSymbol id);

    /**
     * Returns a new match additionally containing the positive mapping of id to tree.
     *
     * @param id the id
     * @param tree the tree to map to id
     * @return a new match object which additionally contains the positive mapping of id to tree or
     *     empty, if this new mapping conflicts with an older mapping
     */
    Optional<? extends Match<MatchedT>> withDefinition(
            @NotNull IndexedSymbol id, @NotNull MatchedT tree);

    /**
     * Returns a new match additionally containing the positive mapping of id to forest.
     *
     * @param id the id
     * @param forest the forest to map to id
     * @return a new match object which additionally contains the positive mapping of id to forest
     *     or empty, if this new mapping conflicts with an older mapping
     */
    Optional<? extends Match<MatchedT>> withDefinition(
            @NotNull IndexedSymbol id, @NotNull List<MatchedT> forest);

    /**
     * Returns a new match additionally containing the negative mapping of id to tree.
     *
     * @param id the id
     * @param tree the tree to map to id
     * @return a new match object which additionally contains the negative mapping of id to tree or
     *     empty, if this new mapping conflicts with an older mapping
     */
    Optional<? extends Match<MatchedT>> withNegativeDefinition(
            @NotNull IndexedSymbol id, @NotNull MatchedT tree);

    /**
     * Returns a new match additionally containing the negative mapping of id to forest.
     *
     * @param id the id
     * @param forest the forest to map to id
     * @return a new match object which additionally contains the negative mapping of id to forest
     *     or empty, if this new mapping conflicts with an older mapping
     */
    Optional<? extends Match<MatchedT>> withNegativeDefinition(
            @NotNull IndexedSymbol id, @NotNull List<MatchedT> forest);

    /**
     * Returns a new match additionally containing the positive mapping of id to tree.
     *
     * @param id the id
     * @param tree the tree to map to id
     * @param eqTesterForId the equality tester to map to the id
     * @return a new match object which additionally contains the positive mapping of id to tree or
     *     empty, if this new mapping conflicts with an older mapping
     */
    Optional<? extends Match<MatchedT>> withDefinition(
            @NotNull IndexedSymbol id,
            @NotNull MatchedT tree,
            SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTesterForId);

    /**
     * Returns a new match additionally containing the positive mapping of id to forest.
     *
     * @param id the id
     * @param forest the forest to map to id
     * @param eqTesterForId the equality tester to map to the id
     * @return a new match object which additionally contains the positive mapping of id to forest
     *     or empty, if this new mapping conflicts with an older mapping
     */
    Optional<? extends Match<MatchedT>> withDefinition(
            @NotNull IndexedSymbol id,
            @NotNull List<MatchedT> forest,
            SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTesterForId);

    /**
     * Returns a new match additionally containing the negative mapping of id to tree.
     *
     * @param id the id
     * @param tree the tree to map to id
     * @param eqTesterForId the equality tester to map to the id
     * @return a new match object which additionally contains the negative mapping of id to tree or
     *     empty, if this new mapping conflicts with an older mapping
     */
    Optional<? extends Match<MatchedT>> withNegativeDefinition(
            @NotNull IndexedSymbol id,
            @NotNull MatchedT tree,
            SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTesterForId);

    /**
     * Returns a new match additionally containing the negative mapping of id to forest.
     *
     * @param id the id
     * @param forest the forest to map to id
     * @param eqTesterForId the equality tester to map to the id
     * @return a new match object which additionally contains the negative mapping of id to forest
     *     or empty, if this new mapping conflicts with an older mapping
     */
    Optional<? extends Match<MatchedT>> withNegativeDefinition(
            @NotNull IndexedSymbol id,
            @NotNull List<MatchedT> forest,
            SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTesterForId);

    /**
     * Create a new match, which is the merge of this match and the other match, if they are
     * compatible.
     *
     * @param other the other match
     * @return the merged match if compatible and empty otherwise
     */
    Optional<? extends Match<MatchedT>> withMatch(@NotNull Match<MatchedT> other);

    /**
     * Checks whether this match is compatible to the positive mapping of tree to id.
     *
     * @param id the id
     * @param tree the tree
     * @return true iff this match is compatible to the positive mapping of tree to id.
     */
    boolean isConsistentWith(IndexedSymbol id, MatchedT tree);

    /**
     * Checks whether this match is compatible to the positive mapping of forest to id.
     *
     * @param id the id
     * @param forest the forest
     * @return true iff this match is compatible to the positive mapping of forest to id.
     */
    boolean isConsistentWith(IndexedSymbol id, List<MatchedT> forest);

    /**
     * Returns a set of all positively defined ids.
     *
     * @return a set of all positively defined ids
     */
    Set<IndexedSymbol> getAllDefinedIds();

    /**
     * Returns a set of all negatively defined ids.
     *
     * @return a set of all negatively defined ids
     */
    Set<IndexedSymbol> getAllNegativelyDefinedIds();
}
