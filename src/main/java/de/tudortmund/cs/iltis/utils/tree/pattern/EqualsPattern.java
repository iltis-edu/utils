package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.collections.EqualsFunction;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * Matches if the given tree equals the patterns tree.
 *
 * <p>This class does <b>not</b> overwrite {@link #createForest(Match)} to return the forest
 * specified in the constructor for the following reason: If it would be implemented that way and if
 * an equality function different from {@link EqualsFunction} is used, the created tree would always
 * be the one specified in the constructor which does not need to be the one actually matched.
 *
 * @param <MatchedT> the type of tree to be matched
 * @see TreePattern
 */
public class EqualsPattern<MatchedT extends Tree<MatchedT>> extends PredicatePattern<MatchedT> {

    private List<MatchedT> patternForest;
    private static SerializableBiFunction<List<?>, List<?>, Boolean> DEFAULT_EQ_TESTER =
            new EqualsFunction();

    public EqualsPattern(@NotNull final MatchedT patternTree) {
        this(Arrays.asList(patternTree));
    }

    public EqualsPattern(@NotNull final List<MatchedT> patternForest) {
        this(
                patternForest,
                (SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>)
                        (Object) DEFAULT_EQ_TESTER);
    }

    public EqualsPattern(
            @NotNull final List<MatchedT> patternForest,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>
                            eqTester) {
        super(forest -> eqTester.apply(forest, patternForest));
        this.patternForest = new ArrayList<>(patternForest);
        this.eqTester = eqTester;
        // Alternative to omit in some cases the necessity to specify a name (for forest
        // creation), but there may be side effects
        // this(new IndexedSymbol("equals@" + patternForest.toString(), "", ""), patternForest,
        // eqTester);
    }

    public EqualsPattern(@NotNull final IndexedSymbol name, @NotNull final MatchedT patternTree) {
        this(name, Arrays.asList(patternTree));
    }

    public EqualsPattern(
            @NotNull final IndexedSymbol name, @NotNull final List<MatchedT> patternForest) {
        this(
                name,
                patternForest,
                (SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>)
                        (Object) DEFAULT_EQ_TESTER);
    }

    public EqualsPattern(
            @NotNull final IndexedSymbol name,
            @NotNull final List<MatchedT> patternForest,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>
                            eqTester) {
        super(name, eqTester, forest -> eqTester.apply(forest, patternForest));
        this.patternForest = new ArrayList<>(patternForest);
    }

    @Override
    public PatternType getType() {
        return TreePatternType.EqualsPattern;
    }

    @Override
    public String toString() {
        return "EqualsPattern [id=" + name + ", patternForest=" + patternForest + "]";
    }

    @Override
    public EqualsPattern<MatchedT> clone() {
        return new EqualsPattern<>(name, patternForest, eqTester);
    }

    @Override
    protected EqualsPattern<MatchedT> cloneWithIteratedName(int iteration) {
        return new EqualsPattern<>(iterateName(name, iteration), patternForest, eqTester);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !hasSameClass(obj)) {
            return false;
        }
        return Objects.equals(patternForest, ((EqualsPattern<?>) obj).patternForest)
                && Objects.equals(name, ((EqualsPattern<?>) obj).name)
                && Objects.equals(eqTester, ((EqualsPattern<?>) obj).eqTester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), patternForest);
    }

    /** for GWT serialization */
    @SuppressWarnings("unused")
    private EqualsPattern() {
        super();
    }
}
