package de.tudortmund.cs.iltis.utils.term.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.term.NameOnlyEqualityFunction;
import de.tudortmund.cs.iltis.utils.term.Term;
import de.tudortmund.cs.iltis.utils.tree.pattern.CreateException;
import de.tudortmund.cs.iltis.utils.tree.pattern.PatternType;
import de.tudortmund.cs.iltis.utils.tree.pattern.RepeatForestPattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.TreePattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.MatchIterator;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.SingleMatchIterator;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.constraints.NotNull;

/**
 * Matches any tree with the name associated with the given name.
 *
 * <p>The name used by a term to be matched does not need to be the same as the nameToMatch
 * specified in this pattern's constructor, but every time this nameToMatch appears in a
 * tree/forest, it has to be the same actual name. Internally, that is achieved by storing an
 * additional mapping "anyname@idToMatchForName" in the match object having a {@link
 * NameOnlyEqualityFunction} as equality function.
 *
 * <p>In combination with the {@link RepeatForestPattern}, the wish may arise to allow for different
 * names to be matched in different iterations, but the same names to be matched in the same
 * iteration. To get this behavior, you can add brackets not only behind the pattern's id (as
 * described in class {@link RepeatForestPattern}), but also behind the pattern's idToMatchForName
 * (e.g. <code>xyz[]^1_2</code>).
 *
 * @param <MatchedT> the type of term to be matched
 * @see TreePattern
 */
public class AnyNamePattern<
                MatchedT extends Term<MatchedT, NameT>,
                NameT extends Comparable<NameT> & Serializable>
        extends TreePattern<MatchedT> implements NamePattern<MatchedT, NameT> {

    private IndexedSymbol idToMatchForName;

    public class AnyNamePatternMatchIterator extends SingleMatchIterator<MatchedT> {

        public AnyNamePatternMatchIterator(@NotNull Match<MatchedT> match, @NotNull MatchedT tree) {
            super(match, tree);
        }

        @Override
        protected Match<MatchedT> calculateMatch() {
            if (!isConsistentWithThisPatternsName(match, forest)) return null;

            IndexedSymbol nameId = makeMatchId();
            Optional<? extends Match<MatchedT>> opMatch =
                    match.withDefinition(nameId, getTree(), new NameOnlyEqualityFunction<>());

            return withThisPatternsDefinition(opMatch.orElse(null), forest);
        }

        /** for GWT serialization */
        @SuppressWarnings("unused")
        private AnyNamePatternMatchIterator() {}
    }

    /**
     * Create a new AnyMatchPattern without an own name
     *
     * @param idToMatchForName the id for the name to be matched by this pattern
     */
    public AnyNamePattern(@NotNull final IndexedSymbol idToMatchForName) {
        super(true);
        this.idToMatchForName = idToMatchForName;
    }

    /**
     * Create a new AnyMatchPattern with an own name
     *
     * @param name the name of this pattern, not the name to match for
     * @param idToMatchForName the id for the name to be matched by this pattern
     */
    public AnyNamePattern(
            @NotNull final IndexedSymbol name, @NotNull final IndexedSymbol idToMatchForName) {
        super(true, name);
        this.idToMatchForName = idToMatchForName;
    }

    /**
     * Create a new AnyMatchPattern with an own name
     *
     * @param name the name of this pattern, not the name to match for
     * @param eqTester the equality tester of this pattern, only used for this pattern's name, not
     *     the id to match for
     * @param idToMatchForName the id for the name to be matched by this pattern
     */
    public AnyNamePattern(
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final IndexedSymbol idToMatchForName) {
        super(true, name, eqTester);
        this.idToMatchForName = idToMatchForName;
    }

    @Override
    public NameT createName(Match<MatchedT> match) {
        Optional<MatchedT> optTree = match.getDefinedTree(makeMatchId());

        if (optTree.isPresent()) {
            return optTree.get().getName();
        }

        throw new CreateException((NamePattern<MatchedT, NameT>) this, match);
    }

    private IndexedSymbol makeMatchId() {
        return new IndexedSymbol(
                "anyname@" + idToMatchForName.getName(),
                idToMatchForName.getSubscript(),
                idToMatchForName.getSuperscript());
    }

    public IndexedSymbol getIDToMatchForName() {
        return this.idToMatchForName;
    }

    @Override
    public PatternType getType() {
        return TermPatternType.AnyNamePattern;
    }

    @Override
    public MatchIterator<MatchedT> matchIterator(
            @NotNull Match<MatchedT> match, @NotNull MatchedT tree) {
        return new AnyNamePatternMatchIterator(match, tree);
    }

    @Override
    public MatchedT createTree(Match<MatchedT> match) throws CreateException {
        return createTreeByMatchAndName(match);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((idToMatchForName == null) ? 0 : idToMatchForName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj)
                && Objects.equals(idToMatchForName, ((AnyNamePattern<?, ?>) obj).idToMatchForName);
    }

    @Override
    public String toString() {
        return "AnyNamePattern [id=" + name + ", idToMatchForName=" + idToMatchForName + "]";
    }

    @Override
    public AnyNamePattern<MatchedT, NameT> clone() {
        return new AnyNamePattern<>(name, eqTester, idToMatchForName);
    }

    @Override
    protected AnyNamePattern<MatchedT, NameT> cloneWithIteratedName(int iteration) {
        return new AnyNamePattern<>(
                iterateName(name, iteration), eqTester, iterateName(idToMatchForName, iteration));
    }

    /** for GWT serialization */
    @SuppressWarnings("unused")
    private AnyNamePattern() {
        super(true);
    }
}
