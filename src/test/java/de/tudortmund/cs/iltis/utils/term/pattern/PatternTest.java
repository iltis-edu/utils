package de.tudortmund.cs.iltis.utils.term.pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.term.Term;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.AnyPattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.ChildrenPattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.FlexibleArityForestPattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.RepeatForestPattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.TreePattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

/** Test for term patterns. */
public class PatternTest {

    private class StringTerm extends Term<StringTerm, String> {
        int payload;

        public StringTerm(String name, int payload) {
            super(false, name);
            this.payload = payload;
        }

        @Override
        public void addChild(StringTerm subtree) {
            super.addChild(subtree);
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj) && this.payload == ((StringTerm) obj).payload;
        }

        @Override
        public int hashCode() {
            return super.hashCode() + payload;
        }

        @Override
        public String toString() {
            return "StringTerm [name=" + name + ", payload=" + payload + "]";
        }
    }

    @Test
    public void AnyName() {
        IndexedSymbol a = new IndexedSymbol("a");
        IndexedSymbol x = new IndexedSymbol("X[]");
        IndexedSymbol y = new IndexedSymbol("Y_1^2");
        IndexedSymbol y2 = new IndexedSymbol("Y[]_1^2");

        TreePattern<StringTerm> pattern =
                new ChildrenPattern<>(
                        a, new RepeatForestPattern<>(new AnyNamePattern<StringTerm, String>(x, y)));
        TreePattern<StringTerm> pattern2 =
                new ChildrenPattern<>(
                        a,
                        new RepeatForestPattern<>(new AnyNamePattern<StringTerm, String>(x, y2)));

        StringTerm term1 = new StringTerm("A", 1);
        StringTerm term2 = new StringTerm("B", 2);
        StringTerm term3 = new StringTerm("B", 3);
        StringTerm term4 = new StringTerm("B", 4);
        StringTerm term5 = new StringTerm("C", 5);
        StringTerm term6 = new StringTerm("D", 6);
        StringTerm term7 = new StringTerm("E", 7);
        term1.addChild(term2);
        term1.addChild(term3);
        term1.addChild(term4);
        term5.addChild(term6);
        term5.addChild(term7);

        checkMatch(pattern, term1);
        checkMatch(pattern2, term1);
        checkMatch(pattern2, term5);
        checkNoMatch(pattern, term5);
    }

    @Test
    public void ExactName() {
        IndexedSymbol a = new IndexedSymbol("a");
        IndexedSymbol x = new IndexedSymbol("X");
        String y = "A";

        TreePattern<StringTerm> pattern =
                new ChildrenPattern<StringTerm>(
                        a,
                        new FlexibleArityForestPattern<>(
                                new RepeatForestPattern<>(new AnyPattern<StringTerm>()),
                                new ExactNamePattern<StringTerm, String>(x, y),
                                new RepeatForestPattern<>(new AnyPattern<StringTerm>())));

        StringTerm term1 = new StringTerm("B", 1);
        StringTerm term2 = new StringTerm("C", 2);
        StringTerm term3 = new StringTerm("B", 3);
        StringTerm term4 = new StringTerm("A", 4);
        StringTerm term5 = new StringTerm("A", 5);
        StringTerm term6 = new StringTerm("D", 6);
        StringTerm term7 = new StringTerm("E", 7);
        term1.addChild(term2);
        term1.addChild(term3);
        term1.addChild(term4);
        term5.addChild(term6);
        term5.addChild(term7);

        checkMatch(pattern, term1);
        checkNoMatch(pattern, term5);
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
