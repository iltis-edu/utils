package de.tudortmund.cs.iltis.utils.term.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.term.Term;
import de.tudortmund.cs.iltis.utils.tree.pattern.PatternType;
import de.tudortmund.cs.iltis.utils.tree.pattern.PredicatePattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.TreePattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * Matches any tree with the given name.
 *
 * @param <MatchedT> the type of term to be matched
 * @see TreePattern
 */
public class ExactNamePattern<
                MatchedT extends Term<MatchedT, NameT>,
                NameT extends Comparable<NameT> & Serializable>
        extends PredicatePattern<MatchedT> implements NamePattern<MatchedT, NameT> {

    private NameT nameToMatch;

    public ExactNamePattern(@NotNull final NameT nameToMatch) {
        super(
                treeToForestPredicate(
                        term ->
                                term != null
                                        && term.isNamed()
                                        && term.getName().equals(nameToMatch)));

        this.nameToMatch = nameToMatch;
    }

    public ExactNamePattern(@NotNull final IndexedSymbol name, @NotNull final NameT nameToMatch) {
        super(
                name,
                treeToForestPredicate(
                        term ->
                                term != null
                                        && term.isNamed()
                                        && term.getName().equals(nameToMatch)));

        this.nameToMatch = nameToMatch;
    }

    public ExactNamePattern(
            @NotNull final IndexedSymbol name,
            @NotNull final NameT nameToMatch,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>
                            eqTester) {
        super(
                name,
                eqTester,
                treeToForestPredicate(
                        term ->
                                term != null
                                        && term.isNamed()
                                        && term.getName().equals(nameToMatch)));

        this.nameToMatch = nameToMatch;
    }

    @Override
    public NameT createName(Match<MatchedT> match) {
        return this.nameToMatch;
    }

    @Override
    public PatternType getType() {
        return TermPatternType.ExactNamePattern;
    }

    @Override
    public String toString() {
        return "ExactNamePattern [id=" + name + ", nameToMatch=" + nameToMatch + "]";
    }

    @Override
    public ExactNamePattern<MatchedT, NameT> clone() {
        return new ExactNamePattern<>(name, nameToMatch, eqTester);
    }

    @Override
    protected ExactNamePattern<MatchedT, NameT> cloneWithIteratedName(int iteration) {
        return new ExactNamePattern<>(iterateName(name, iteration), nameToMatch, eqTester);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !hasSameClass(obj)) {
            return false;
        }
        return Objects.equals(nameToMatch, ((ExactNamePattern<?, ?>) obj).nameToMatch)
                && Objects.equals(name, ((ExactNamePattern<?, ?>) obj).name)
                && Objects.equals(eqTester, ((ExactNamePattern<?, ?>) obj).eqTester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nameToMatch);
    }

    /** for GWT serialization */
    protected ExactNamePattern() {
        super();
    }
}
