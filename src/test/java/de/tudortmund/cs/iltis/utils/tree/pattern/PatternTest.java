package de.tudortmund.cs.iltis.utils.tree.pattern;

import static org.junit.Assert.*;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class PatternTest {

    @Test
    public void Equals() {
        IndexedSymbol x = new IndexedSymbol("X");
        StringTree tree = new StringTree("A");
        EqualsPattern<StringTree> pattern = new EqualsPattern<>(x, tree);
        checkMatch(pattern, tree);

        StringTree biggerTree = new StringTree("B", tree);
        checkNoMatch(pattern, biggerTree);
    }

    @Test
    public void Any() {
        IndexedSymbol x = new IndexedSymbol("X");
        PredicatePattern<StringTree> pattern =
                new PredicatePattern<StringTree>(x, PredicatePattern.anyTreePredicate());
        StringTree tree = new StringTree("A");
        checkMatch(pattern, tree);

        StringTree biggerTree = new StringTree("B", tree);
        checkMatch(pattern, biggerTree);
    }

    @Test
    public void Alternative() {
        IndexedSymbol x = new IndexedSymbol("X");
        StringTree treeA = new StringTree("A");
        StringTree treeB = new StringTree("B");
        AlternativePattern<StringTree> pattern =
                new AlternativePattern<>(
                        x, Arrays.asList(new EqualsPattern<>(treeA), new EqualsPattern<>(treeB)));

        checkMatch(pattern, treeA);
        checkMatch(pattern, treeB);

        StringTree biggerTree = new StringTree("B", treeA, treeB);
        checkNoMatch(pattern, biggerTree);
    }

    @Test
    public void IteratedNamesInRepeat() {
        IndexedSymbol x = new IndexedSymbol("X");
        IndexedSymbol y = new IndexedSymbol("Y[]");
        StringTree treeA = new StringTree("A");
        StringTree treeB = new StringTree("B");
        StringTree treeC = new StringTree("C");
        RepeatForestPattern<StringTree> pattern =
                new RepeatForestPattern<>(
                        x,
                        new AlternativePattern<>(
                                y,
                                Arrays.asList(
                                        new EqualsPattern<>(treeA), new EqualsPattern<>(treeB))));

        checkMatches(pattern, Arrays.asList(treeA));
        checkMatches(pattern, Arrays.asList(treeA, treeA));
        checkMatches(pattern, Arrays.asList(treeB));
        checkMatches(pattern, Arrays.asList(treeA, treeB));
        checkNoMatches(pattern, Arrays.asList(treeC));
        checkNoMatches(pattern, Arrays.asList(treeA, treeC, treeB));
    }

    @Test
    public void Repeat() {
        IndexedSymbol x = new IndexedSymbol("X");
        IndexedSymbol y = new IndexedSymbol("Y");
        StringTree treeA = new StringTree("A");
        StringTree treeB = new StringTree("B");
        StringTree treeC = new StringTree("C");
        RepeatForestPattern<StringTree> pattern =
                new RepeatForestPattern<>(x, new EqualsPattern<>(treeA));

        checkMatches(pattern, Arrays.asList(treeA));
        checkMatches(pattern, Arrays.asList(treeA, treeA));
        checkMatches(pattern, Arrays.asList());
        checkNoMatches(pattern, Arrays.asList(treeB));
        checkNoMatches(pattern, Arrays.asList(treeA, treeB));
        checkNoMatches(pattern, Arrays.asList(treeB, treeA));

        pattern =
                new RepeatForestPattern<>(
                        x,
                        new AlternativePattern<>(
                                y,
                                (forest1, forest2) -> true,
                                Arrays.asList(
                                        new EqualsPattern<>(treeA), new EqualsPattern<>(treeB))));
        checkMatches(pattern, Arrays.asList(treeA));
        checkMatches(pattern, Arrays.asList(treeA, treeA));
        checkMatches(pattern, Arrays.asList(treeB));
        checkMatches(pattern, Arrays.asList(treeA, treeB));
        checkNoMatches(pattern, Arrays.asList(treeC));
        checkNoMatches(pattern, Arrays.asList(treeA, treeC, treeB));
    }

    @Test
    public void FlexibleSubpatterns() {
        IndexedSymbol u = new IndexedSymbol("U");
        IndexedSymbol x = new IndexedSymbol("X");
        IndexedSymbol y1 = new IndexedSymbol("Y_1");
        IndexedSymbol y2 = new IndexedSymbol("Y_2");
        StringTree treeA = new StringTree("A");
        StringTree treeB = new StringTree("B");
        RepeatForestPattern<StringTree> Aforest1 =
                new RepeatForestPattern<>(y1, new EqualsPattern<>(treeA));
        RepeatForestPattern<StringTree> Aforest2 =
                new RepeatForestPattern<>(y2, new EqualsPattern<>(treeA));
        EqualsPattern<StringTree> eqPattern = new EqualsPattern<>(x, treeB);
        FlexibleArityForestPattern<StringTree> pattern =
                new FlexibleArityForestPattern<>(u, Aforest1, eqPattern, Aforest2);

        checkMatches(pattern, Arrays.asList(treeA, treeA, treeB, treeA));
        checkMatches(pattern, Arrays.asList(treeB, treeA, treeA));
        checkMatches(pattern, Arrays.asList(treeA, treeB));
        checkMatches(pattern, Arrays.asList(treeB));
        checkNoMatches(pattern, Arrays.asList(treeA, treeA));
        checkNoMatches(pattern, Arrays.asList(treeA, treeB, treeB));
        checkNoMatches(pattern, Arrays.asList());

        pattern = new FlexibleArityForestPattern<>(x, Aforest1, new EqualsPattern<>(treeB));
        checkNoMatches(pattern, Arrays.asList(treeA, treeA, treeB, treeA));
    }

    @Test
    public void Empty() {
        IndexedSymbol u = new IndexedSymbol("U");
        IndexedSymbol x = new IndexedSymbol("X");
        IndexedSymbol y = new IndexedSymbol("Y");
        StringTree treeA = new StringTree("A");
        ChildrenPattern<StringTree> pattern =
                new ChildrenPattern<>(
                        u, new FlexibleArityForestPattern<StringTree>(x, new AnyPattern<>(y)));

        checkMatches(pattern, Arrays.asList(treeA));
    }

    @Test
    public void FixedArityChildren() {
        IndexedSymbol x = new IndexedSymbol("X");
        IndexedSymbol y1 = new IndexedSymbol("Y_1");
        StringTree treeA = new StringTree("A");
        StringTree treeB = new StringTree("B");
        StringTree biggerTree = new StringTree("C", treeA, treeB);
        FixedArityForestPattern<StringTree> forestPattern =
                new FixedArityForestPattern<>(
                        y1, new EqualsPattern<>(treeA), new EqualsPattern<>(treeB));
        ChildrenPattern<StringTree> pattern = new ChildrenPattern<>(x, forestPattern);

        checkMatch(pattern, biggerTree);
        checkNoMatches(pattern, Arrays.asList(treeA, treeB));
        checkNoMatches(pattern, Arrays.asList());
        checkNoMatches(pattern, Arrays.asList(treeA));
    }

    @Test
    public void ContainsDescendant() {
        IndexedSymbol x = new IndexedSymbol("X");
        StringTree treeA = new StringTree("A");
        StringTree treeB = new StringTree("B");
        StringTree biggerTree = new StringTree("C", treeA, treeB);
        StringTree biggestTree = new StringTree("D", Arrays.asList(biggerTree));
        ContainsDescendantPattern<StringTree> pattern =
                new ContainsDescendantPattern<>(x, new EqualsPattern<>(treeB));

        checkMatch(pattern, biggestTree);
        checkMatch(pattern, biggerTree);
        checkMatch(pattern, treeB);
        checkMatches(pattern, Arrays.asList(treeA, biggestTree));
        checkNoMatch(pattern, treeA);
        checkNoMatches(pattern, Arrays.asList(treeA, treeA));
    }

    @Test
    public void MultiConsraint() {
        IndexedSymbol x = new IndexedSymbol("X");
        StringTree treeA = new StringTree("A");
        StringTree treeB = new StringTree("B");
        StringTree biggerTree = new StringTree("C", treeA, treeB);
        StringTree biggestTree = new StringTree("D", Arrays.asList(biggerTree));
        ContainsDescendantPattern<StringTree> cdPattern =
                new ContainsDescendantPattern<>(new EqualsPattern<>(treeB));
        ChildrenPattern<StringTree> facPattern =
                new ChildrenPattern<>(
                        new FixedArityForestPattern<>(
                                new PredicatePattern<StringTree>(
                                        PredicatePattern.anyTreePredicate())));
        MultiConstraintPattern<StringTree> pattern =
                new MultiConstraintPattern<>(x, cdPattern, facPattern);

        checkMatch(pattern, biggestTree);
        checkNoMatch(pattern, biggerTree);
        checkNoMatch(pattern, treeB);
        checkNoMatches(pattern, Arrays.asList(treeA, biggestTree));
        checkNoMatch(pattern, treeA);
        checkNoMatches(pattern, Arrays.asList(treeA, treeA));
    }

    @Test
    public void Complement() {
        IndexedSymbol x = new IndexedSymbol("X");
        StringTree treeA = new StringTree("A");
        StringTree treeB = new StringTree("B");
        ComplementPattern<StringTree> pattern =
                new ComplementPattern<>(x, new EqualsPattern<>(treeA));

        checkMatch(pattern, treeB);
        checkNoMatch(pattern, treeA);
    }

    protected <MatchedT extends Tree<MatchedT>> void checkMatch(
            TreePattern<MatchedT> pattern, MatchedT tree) {
        Optional<Match<MatchedT>> match = pattern.getFirstMatchIfAny(tree);
        assertTrue(match.isPresent());
        if (pattern.isNamed()) {
            Optional<MatchedT> value = match.get().getDefinedTree(pattern.getName());
            assertTrue(value.isPresent());
            assertEquals(tree, value.get());
        }
        assertEquals(tree, pattern.createTree(match.get()));
    }

    protected <MatchedT extends Tree<MatchedT>> void checkNoMatch(
            TreePattern<MatchedT> pattern, MatchedT tree) {
        Optional<Match<MatchedT>> match = pattern.getFirstMatchIfAny(tree);
        assertFalse(match.isPresent());
    }

    protected <MatchedT extends Tree<MatchedT>> void checkMatches(
            TreePattern<MatchedT> pattern, List<MatchedT> forest) {
        Optional<Match<MatchedT>> match = pattern.getFirstMatchIfAny(forest);
        assertTrue(match.isPresent());
        if (pattern.isNamed()) {
            Optional<List<MatchedT>> value = match.get().getDefinedForest(pattern.getName());
            assertTrue(value.isPresent());
            assertEquals(forest, value.get());
        }
        assertEquals(forest, pattern.createForest(match.get()));
    }

    protected <MatchedT extends Tree<MatchedT>> void checkNoMatches(
            TreePattern<MatchedT> pattern, List<MatchedT> forest) {
        Optional<Match<MatchedT>> match = pattern.getFirstMatchIfAny(forest);
        assertFalse(match.isPresent());
    }
}
