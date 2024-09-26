package de.tudortmund.cs.iltis.utils.tree.pattern.match;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.collections.Either;
import de.tudortmund.cs.iltis.utils.collections.EqualsFunction;
import de.tudortmund.cs.iltis.utils.collections.ListSet;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.general.Data;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import javax.validation.constraints.NotNull;

/**
 * An <b>immutable</b> implementation of the {@link Match} interface.
 *
 * <p>For each id an equality function and either a positive forest or a set of negative forests can
 * be stored. Mapping a single positive forest means that this forest was matched for this id (by
 * some pattern), mapping a set of negative forests means that theses forest cannot be matched by
 * this id (because some (complement) pattern forbids this). The equality function is used when
 * checking whether two mappings for the same id are compatible.
 *
 * <p>Trees are internally handled as forests of size 1. All methods for trees are delegating to the
 * methods for forest and extracting the first element of the forest.
 *
 * @param <MatchedT> the type of tree to map to ids
 */
public class TreeMatch<MatchedT extends Tree<MatchedT>> implements Match<MatchedT> {

    protected Map<IndexedSymbol, Either<List<MatchedT>, ListSet<List<MatchedT>>>> definedForests;
    protected SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> defaultEqTester;
    protected Map<IndexedSymbol, SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>>
            eqTesterForIds;

    /** Creates a new empty tree match object with an {@link EqualsFunction} as equality tester. */
    public TreeMatch() {
        definedForests = new TreeMap<>();
        defaultEqTester = new EqualsFunction<>();
        eqTesterForIds = new TreeMap<>();
    }

    /**
     * Creates a new empty tree match object with the given default equality tester.
     *
     * @param equalityTester the default equality tester
     */
    public TreeMatch(
            @NotNull SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>
                            equalityTester) {
        definedForests = new TreeMap<>();
        defaultEqTester = equalityTester;
        eqTesterForIds = new TreeMap<>();
    }

    // GETTER

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#defines(de.tudortmund.cs.iltis.utils.IndexedSymbol)
     */
    @Override
    public boolean defines(IndexedSymbol id) {
        return this.definedForests.containsKey(id) && this.definedForests.get(id).isLeft();
    }

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#definesNegatively(de.tudortmund.cs.iltis.utils.IndexedSymbol)
     */
    @Override
    public boolean definesNegatively(IndexedSymbol id) {
        return this.definedForests.containsKey(id) && this.definedForests.get(id).isRight();
    }

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#getDefinedForest(de.tudortmund.cs.iltis.utils.IndexedSymbol)
     */
    @Override
    public Optional<List<MatchedT>> getDefinedForest(IndexedSymbol id) {
        if (defines(id)) return Optional.of(this.definedForests.get(id).getLeft());
        return Optional.empty();
    }

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#getNegativelyDefined(de.tudortmund.cs.iltis.utils.IndexedSymbol)
     */
    @Override
    public Optional<? extends Set<List<MatchedT>>> getNegativelyDefined(IndexedSymbol id) {
        if (definesNegatively(id)) return Optional.of(this.definedForests.get(id).getRight());
        return Optional.empty();
    }

    /**
     * @see de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#getAllDefinedIds()
     */
    @Override
    public Set<IndexedSymbol> getAllDefinedIds() {
        return new ListSet<>(
                definedForests.keySet().stream().filter(key -> definedForests.get(key).isLeft()));
    }

    /**
     * @see de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#getAllNegativelyDefinedIds()
     */
    @Override
    public Set<IndexedSymbol> getAllNegativelyDefinedIds() {
        return new ListSet<>(
                definedForests.keySet().stream().filter(key -> definedForests.get(key).isRight()));
    }

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#isConsistentWith(de.tudortmund.cs.iltis.utils.IndexedSymbol,
     *     MatchedT)
     */
    @Override
    public boolean isConsistentWith(IndexedSymbol id, MatchedT tree) {
        return withDefinition(id, tree).isPresent();
    }

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#isConsistentWith(de.tudortmund.cs.iltis.utils.IndexedSymbol,
     *     java.util.List)
     */
    @Override
    public boolean isConsistentWith(IndexedSymbol id, List<MatchedT> forest) {
        return withDefinition(id, forest).isPresent();
    }

    /**
     * @see de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#getEqTesterFor(IndexedSymbol)
     */
    @Override
    public Optional<SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>> getEqTesterFor(
            IndexedSymbol id) {
        if (eqTesterForIds.containsKey(id)) return Optional.of(eqTesterForIds.get(id));
        return Optional.empty();
    }

    // SETTER

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#withDefinition(de.tudortmund.cs.iltis.utils.IndexedSymbol,
     *     MatchedT)
     */
    @Override
    public Optional<TreeMatch<MatchedT>> withDefinition(
            @NotNull IndexedSymbol id, @NotNull MatchedT tree) {
        return withDefinition(id, Data.newArrayList1(tree));
    }

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#withDefinition(de.tudortmund.cs.iltis.utils.IndexedSymbol,
     *     MatchedT, SerializableBiFunction)
     */
    @Override
    public Optional<TreeMatch<MatchedT>> withDefinition(
            @NotNull IndexedSymbol id,
            @NotNull MatchedT tree,
            @NotNull SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTesterForId) {
        return withDefinition(id, Data.newArrayList1(tree), eqTesterForId);
    }

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#withDefinition(de.tudortmund.cs.iltis.utils.IndexedSymbol,
     *     java.util.List)
     */
    @Override
    public Optional<TreeMatch<MatchedT>> withDefinition(
            @NotNull IndexedSymbol id, @NotNull List<MatchedT> forest) {
        return clone().extendMatch(id, Either.left(forest));
    }

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#withDefinition(de.tudortmund.cs.iltis.utils.IndexedSymbol,
     *     java.util.List, SerializableBiFunction)
     */
    @Override
    public Optional<TreeMatch<MatchedT>> withDefinition(
            @NotNull IndexedSymbol id,
            @NotNull List<MatchedT> forest,
            @NotNull SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTesterForId) {
        return clone().extendMatch(id, Either.left(forest), eqTesterForId);
    }

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#withNegativeDefinition(de.tudortmund.cs.iltis.utils.IndexedSymbol,
     *     MatchedT)
     */
    @Override
    public Optional<TreeMatch<MatchedT>> withNegativeDefinition(
            @NotNull IndexedSymbol id, @NotNull MatchedT tree) {
        return withNegativeDefinition(id, Data.newArrayList(tree));
    }

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#withNegativeDefinition(de.tudortmund.cs.iltis.utils.IndexedSymbol,
     *     MatchedT, SerializableBiFunction)
     */
    @Override
    public Optional<TreeMatch<MatchedT>> withNegativeDefinition(
            @NotNull IndexedSymbol id,
            @NotNull MatchedT tree,
            @NotNull SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTesterForId) {
        return withNegativeDefinition(id, Data.newArrayList(tree), eqTesterForId);
    }

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#withNegativeDefinition(de.tudortmund.cs.iltis.utils.IndexedSymbol,
     *     java.util.List)
     */
    @Override
    public Optional<TreeMatch<MatchedT>> withNegativeDefinition(
            @NotNull IndexedSymbol id, @NotNull List<MatchedT> forest) {
        ListSet<List<MatchedT>> listSet = new ListSet<>(defaultEqTester);
        listSet.add(forest);
        return clone().extendMatch(id, Either.right(listSet));
    }

    /**
     * @see
     *     de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#withNegativeDefinition(de.tudortmund.cs.iltis.utils.IndexedSymbol,
     *     java.util.List, SerializableBiFunction)
     */
    @Override
    public Optional<TreeMatch<MatchedT>> withNegativeDefinition(
            @NotNull IndexedSymbol id,
            @NotNull List<MatchedT> forest,
            @NotNull SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTesterForId) {
        ListSet<List<MatchedT>> listSet = new ListSet<>(defaultEqTester);
        listSet.add(forest);
        return clone().extendMatch(id, Either.right(listSet), eqTesterForId);
    }

    /**
     * @see de.tudortmund.cs.iltis.utils.tree.pattern.match.Match#withMatch(Match)
     */
    @Override
    public Optional<? extends Match<MatchedT>> withMatch(@NotNull Match<MatchedT> other) {
        return extendMatchByMatch(clone(), other);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected static <MatchedT extends Tree<MatchedT>>
            Optional<TreeMatch<MatchedT>> extendMatchByMatch(
                    @NotNull TreeMatch<MatchedT> basis, @NotNull Match<MatchedT> other) {
        Optional<TreeMatch<MatchedT>> copy = Optional.of(basis);

        for (IndexedSymbol sym : other.getAllDefinedIds()) {
            copy =
                    copy.get()
                            .extendMatch(
                                    sym,
                                    Either.left(other.getDefinedForest(sym).get()),
                                    other.getEqTesterFor(sym).get());
            if (!copy.isPresent()) return copy;
        }
        for (IndexedSymbol sym : other.getAllNegativelyDefinedIds()) {
            copy =
                    copy.get()
                            .extendMatch(
                                    sym,
                                    Either.right(other.getNegativelyDefined(sym).get()),
                                    other.getEqTesterFor(sym).get());
            if (!copy.isPresent()) return copy;
        }

        return copy;
    }

    protected Optional<TreeMatch<MatchedT>> extendMatch(
            @NotNull IndexedSymbol id,
            @NotNull Either<List<MatchedT>, ? extends Set<List<MatchedT>>> value) {

        return extendMatch(id, value, defaultEqTester);
    }

    protected Optional<TreeMatch<MatchedT>> extendMatch(
            @NotNull IndexedSymbol id,
            @NotNull Either<List<MatchedT>, ? extends Set<List<MatchedT>>> value,
            @NotNull SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>
                            eqTesterForNew) {

        boolean matches = true;

        if (!this.definedForests.containsKey(id)) {
            if (value.isLeft()) {
                // no value contained, new value is positive
                this.definedForests.put(id, Either.left(value.getLeft()));
            } else {
                // no value contained, new value is negative
                ListSet<List<MatchedT>> listSet = new ListSet<>(eqTesterForNew);
                listSet.addAll(value.getRight());
                this.definedForests.put(id, Either.right(listSet));
            }
            this.eqTesterForIds.put(id, eqTesterForNew);
        } else {
            Either<List<MatchedT>, ListSet<List<MatchedT>>> containedValue =
                    this.definedForests.get(id);
            SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester =
                    eqTesterForIds.getOrDefault(id, defaultEqTester);

            if (containedValue.isLeft() && value.isLeft()) {
                // both, contained and new value are positive
                matches = eqTester.apply(containedValue.getLeft(), value.getLeft());
            } else if (containedValue.isRight() && value.isLeft()) {
                // contained value is negative, new value is positive
                if (containedValue.getRight().contains(value.getLeft())) {
                    matches = false;
                } else {
                    this.definedForests.put(id, Either.left(value.getLeft()));
                }
            } else if (containedValue.isLeft() && value.isRight()) {
                // contained value is positive, new value is negative
                ListSet<List<MatchedT>> listSet = new ListSet<>(eqTester);
                listSet.addAll(value.getRight());
                if (listSet.contains(containedValue.getLeft())) {
                    matches = false;
                }
            } else {
                // both, contained and new value are negative
                containedValue.getRight().addAll(value.getRight());
            }
        }

        if (matches) return Optional.of(this);
        return Optional.empty();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + defaultEqTester.hashCode();
        result = prime * result + definedForests.hashCode();
        result = prime * result + eqTesterForIds.hashCode();
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof TreeMatch)) return false;
        TreeMatch<MatchedT> other = (TreeMatch<MatchedT>) obj;
        return defaultEqTester.equals(other.defaultEqTester)
                && definedForests.equals(other.definedForests)
                && eqTesterForIds.equals(other.eqTesterForIds);
    }

    @Override
    public String toString() {
        return "TreeMatch " + definedForests.toString();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public TreeMatch<MatchedT> clone() {
        TreeMatch<MatchedT> copy = new TreeMatch<>(defaultEqTester);
        for (IndexedSymbol id : this.definedForests.keySet())
            copy.definedForests.put(id, this.definedForests.get(id));
        for (IndexedSymbol id : this.eqTesterForIds.keySet())
            copy.eqTesterForIds.put(id, this.eqTesterForIds.get(id));
        return copy;
    }
}
