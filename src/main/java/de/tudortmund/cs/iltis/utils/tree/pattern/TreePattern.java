package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.IndexedSymbol;
import de.tudortmund.cs.iltis.utils.collections.EmptyIterator;
import de.tudortmund.cs.iltis.utils.collections.EqualsFunction;
import de.tudortmund.cs.iltis.utils.collections.ListSet;
import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.io.parser.general.ParsablyTyped;
import de.tudortmund.cs.iltis.utils.term.Term;
import de.tudortmund.cs.iltis.utils.term.pattern.AnyNamePattern;
import de.tudortmund.cs.iltis.utils.term.pattern.ExactNamePattern;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.TreeMatch;
import java.util.*;
import javax.validation.constraints.NotNull;

/**
 * Class for pattern matching on trees and forests (i.e. lists of trees).
 *
 * <p>To check whether a pattern matches a given tree/forest, use {@link #matches(List)}, and to get
 * the resulting matches, use {@link #matchIterator(List)} or {@link #getAllMatches(List)} or {@link
 * #getFirstMatchIfAny(List)} (or the analogous methods for trees). All these tests take an empty
 * match object with an {@link EqualsFunction} as equality function as basis (see default value of
 * {@link TreeMatch#TreeMatch()}). To use a different basis match object use {@link
 * #matchIterator(Match, List)} (or the respective method for trees).
 *
 * <p>A match object consists of a mapping of pattern names (ids) to (sub-)trees (see {@link Match}
 * and {@link TreeMatch} for details). While matching with named patterns the actual tree which was
 * matched by this pattern is mapped to this pattern's name in a match object. With the match
 * object, subsequently, a new tree can be created, using the stored mappings. The tree can be built
 * using the same pattern, thus reconstructing the matched tree from the match object; or using
 * another pattern, thus constructing a <b>transformed</b> tree using the same subtrees, possibly in
 * a different order. To create a tree or forest from a given match object, use {@link
 * #createForest(Match)}, {@link #createTree(Match)}, {@link #createForestIfPossible(Match)} or
 * {@link #createTreeIfPossible(Match)}.
 *
 * <p>The patterns in this package are divided in tree, forest and general patterns. Tree patterns
 * only match a single tree, forest patterns match forests (and also trees since trees are forests
 * of size one), and general patterns conceptually do not differ between forests and trees. The only
 * tree patterns are the {@link ChildrenPattern} and the two name patterns {@link AnyNamePattern}
 * and {@link ExactNamePattern} from the term.pattern-package. Forest patterns are the {@link
 * FixedArityForestPattern}, {@link FlexibleArityForestPattern} and {@link RepeatForestPattern}
 * (note the word forest in their name). The rest of the patterns are general patterns. The
 * ChildrenPattern poses a connection between tree and forest patterns by matching a tree using a
 * restriction on its children, which form a forest.
 *
 * <p>This class and its subclasses are designed to be used as a kit for more specific patterns. For
 * example, a pattern that matches a relation atom in first order logic can be constructed using a
 * {@link MultiConstraintPattern} consisting of the following single subpatterns: an {@link
 * ExactNamePattern} for the name "R"; and a {@link ChildrenPattern} to restrict the children (i.e.
 * terms inside the relation atom in our example). The ChildrenPattern can be created with a
 * subpattern of type {@link FixedArityForestPattern} which consists of a {@link
 * RepeatForestPattern} which in turn consists of an {@link EqualsPattern} specifying the variable
 * "a". <br>
 * Short: MultiConstraint( "R" , Children( FixedArity ( Repeat ( Equals "a" ) ) ) )
 *
 * <p>If you plan to subclass this class, you have to overwrite at least {@link
 * #matchIterator(Match, List)} or {@link #matchIterator(Match, Tree)}, and {@link
 * #createForest(Match)} or {@link #createTree(Match)}. If you subclass a class that already
 * overwrites a method for matching (e.g. you subclass {@link MultiConstraintPattern} to set fixed
 * subpatterns in its constructor) you only need to overwrite a method for creating a tree or
 * forest.
 *
 * <p>Forests are represented by list of trees. All methods in this class and related classes
 * (especially subclasses) treat trees as a special case of forests (containing only one element).
 * The differentiation in methods expecting a forest and methods expecting a tree is pure
 * convenience for the caller. Internally, these methods freely convert between trees and
 * one-element forests wherever possible.
 *
 * @param <MatchedT> the type of tree to be matched
 */
public abstract class TreePattern<MatchedT extends Tree<MatchedT>>
        extends Term<TreePattern<MatchedT>, IndexedSymbol> implements ParsablyTyped {

    protected SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester;

    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////

    // WITHOUT NAME

    /** Creates a new tree pattern with no name, no children and no specific equality tester. */
    public TreePattern(boolean arityFixed) {
        super(arityFixed);
    }

    /** Creates a new tree pattern with no name, given children and no specific equality tester. */
    @SafeVarargs
    public TreePattern(boolean arityFixed, @NotNull final TreePattern<MatchedT>... subpatterns) {
        super(arityFixed, subpatterns);
    }

    /** Creates a new tree pattern with no name, given children and no specific equality tester. */
    public TreePattern(
            boolean arityFixed,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(arityFixed, subpatterns);
    }

    /** Creates a new tree pattern with no name, given children and no specific equality tester. */
    public TreePattern(
            boolean arityFixed,
            @NotNull final TreePattern<MatchedT> subpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(arityFixed, subpattern, subpatterns);
    }

    // WITH NAME, WITHOUT EQTESTER

    /** Creates a new tree pattern with given name, no children and no specific equality tester. */
    public TreePattern(boolean arityFixed, @NotNull final IndexedSymbol name) {
        super(arityFixed, name);
    }

    /**
     * Creates a new tree pattern with given name, given children and no specific equality tester.
     */
    @SafeVarargs
    public TreePattern(
            boolean arityFixed,
            @NotNull final IndexedSymbol name,
            @NotNull final TreePattern<MatchedT>... subpatterns) {
        super(arityFixed, name, subpatterns);
    }

    /**
     * Creates a new tree pattern with given name, given children and no specific equality tester.
     */
    public TreePattern(
            boolean arityFixed,
            @NotNull final IndexedSymbol name,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(arityFixed, name, subpatterns);
    }

    /**
     * Creates a new tree pattern with given name, given children and no specific equality tester.
     */
    public TreePattern(
            boolean arityFixed,
            @NotNull final IndexedSymbol name,
            @NotNull final TreePattern<MatchedT> subpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(arityFixed, name, subpattern, subpatterns);
    }

    // WITH NAME, WITH EQTESTER

    /** Creates a new tree pattern with given name, no children and given equality tester. */
    public TreePattern(
            boolean arityFixed,
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>
                            eqTester) {
        super(arityFixed, name);
        this.eqTester = eqTester;
    }

    /** Creates a new tree pattern with given name, given children and given equality tester. */
    @SafeVarargs
    public TreePattern(
            boolean arityFixed,
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT>... subpatterns) {
        super(arityFixed, name, subpatterns);
        this.eqTester = eqTester;
    }

    /** Creates a new tree pattern with given name, given children and given equality tester. */
    public TreePattern(
            boolean arityFixed,
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(arityFixed, name, subpatterns);
        this.eqTester = eqTester;
    }

    /** Creates a new tree pattern with given name, given children and given equality tester. */
    public TreePattern(
            boolean arityFixed,
            @NotNull final IndexedSymbol name,
            @NotNull final SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean> eqTester,
            @NotNull final TreePattern<MatchedT> subpattern,
            @NotNull final Iterable<? extends TreePattern<MatchedT>> subpatterns) {
        super(arityFixed, name, subpattern, subpatterns);
        this.eqTester = eqTester;
    }

    /** for GTW serialization */
    protected TreePattern() {}

    ///////////////////////////////////////////////////////////////////////////
    // GENERAL METHODS
    ///////////////////////////////////////////////////////////////////////////

    public abstract PatternType getType();

    protected Match<MatchedT> createEmptyMatch() {
        return new TreeMatch<>();
    }

    ///////////////////////////////////////////////////////////////////////////
    // MATCHING METHODS
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Matches the given forest against this pattern and returns an iterator over all matches, based
     * on an empty match object.
     *
     * @param forest the forest to be matched
     * @return an iterator over all matches
     */
    public Iterator<Match<MatchedT>> matchIterator(@NotNull final List<MatchedT> forest) {
        return matchIterator(createEmptyMatch(), forest);
    }

    /**
     * Matches the given forest against this pattern and returns an iterator over all matches.
     *
     * <p>One method of {@link #matchIterator(Match, Tree)} and this method needs to be overwritten
     * in subclasses, since these methods refer to each other. This method converts its given forest
     * to a tree and returns an empty iterator if not possible (i.e. if the forest does not contain
     * exactly one element).
     *
     * @param match the match taken as a basis for the new matches
     * @param forest the forest to be matched
     * @return an iterator over all matches
     */
    public Iterator<Match<MatchedT>> matchIterator(
            @NotNull final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
        if (forest.size() != 1) return new EmptyIterator<>();
        return matchIterator(match, forest.get(0));
    }

    /**
     * Matches the given tree against this pattern and returns an iterator over all matches, based
     * on an empty match object.
     *
     * @param tree the tree to be matched
     * @return an iterator over all matches
     */
    public Iterator<Match<MatchedT>> matchIterator(@NotNull final MatchedT tree) {
        return matchIterator(Collections.singletonList(tree));
    }

    /**
     * Matches the given tree against this pattern and returns an iterator over all matches.
     *
     * <p>One method of {@link #matchIterator(Match, List)} this method needs to be overwritten in
     * subclasses, since these methods refer to each other.
     *
     * @param match the match taken as a basis for the new matches
     * @param tree the tree to be matched
     * @return an iterator over all matches
     */
    public Iterator<Match<MatchedT>> matchIterator(
            @NotNull final Match<MatchedT> match, @NotNull final MatchedT tree) {
        return matchIterator(match, Collections.singletonList(tree));
    }

    /**
     * Checks if this pattern matches a given tree.
     *
     * @param tree the tree to be matched
     * @return true iff the pattern matches
     */
    public boolean matches(@NotNull final MatchedT tree) {
        return matchIterator(tree).hasNext();
    }

    /**
     * Checks if this pattern matches a given forest, i.e. a list of trees.
     *
     * @param forest the forest to be matched
     * @return whether the pattern matches
     */
    public boolean matches(@NotNull final List<MatchedT> forest) {
        return matchIterator(forest).hasNext();
    }

    /**
     * Returns the first match returned by a match iterator of empty if the given tree is not
     * matched.
     *
     * @param tree the tree to be matched
     * @return the first match or empty if the given tree is not matched
     */
    public Optional<Match<MatchedT>> getFirstMatchIfAny(@NotNull final MatchedT tree) {
        return getFirstMatchIfAny(Collections.singletonList(tree));
    }

    /**
     * Returns the first match returned by a match iterator of empty if the given forest is not
     * matched.
     *
     * @param forest the forest to be matched
     * @return the first match or empty if the given forest is not matched
     */
    public Optional<Match<MatchedT>> getFirstMatchIfAny(@NotNull final List<MatchedT> forest) {
        Iterator<Match<MatchedT>> matchIterator = matchIterator(forest);
        if (matchIterator.hasNext()) return Optional.of(matchIterator.next());
        return Optional.empty();
    }

    /**
     * Returns all matches of the given tree.
     *
     * @param tree the tree to be matched
     * @return all matches of the given tree.
     */
    public Set<Match<MatchedT>> getAllMatches(@NotNull final MatchedT tree) {
        return getAllMatches(Collections.singletonList(tree));
    }

    /**
     * Returns all matches of the given forest.
     *
     * @param forest the forest to be matched
     * @return all matches of the given forest.
     */
    public Set<Match<MatchedT>> getAllMatches(@NotNull final List<MatchedT> forest) {
        ListSet<Match<MatchedT>> set = new ListSet<>();
        Iterator<Match<MatchedT>> matchIterator = matchIterator(forest);
        matchIterator.forEachRemaining(set::add);
        return set;
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONVIENIENCE METHODS
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new match object which stores the given tree in a canonical way: it associates the
     * tree as is with the pattern's name
     *
     * @param match the match object to store the tree in (may be null)
     * @param tree the tree to store
     * @return the new match object, or the given match object if this pattern is not named, or null
     *     if the given match is null or the mapping of the given tree to this pattern's name
     *     conflicts with another mapping
     */
    protected Match<MatchedT> withThisPatternsDefinition(
            final Match<MatchedT> match, @NotNull final MatchedT tree) {
        return withThisPatternsDefinition(match, Collections.singletonList(tree));
    }

    /**
     * Checks whether the canonical mapping of the given tree to this pattern's name conflicts with
     * another mapping in the given match object.
     *
     * @param match the match object to check with (may be null)
     * @param tree the tree which mapping is to be checked
     * @return true iff the given tree can be mapped to this pattern's name in the given match
     *     object
     */
    protected boolean isConsistentWithThisPatternsName(
            final Match<MatchedT> match, @NotNull final MatchedT tree) {
        return isConsistentWithThisPatternsName(match, Collections.singletonList(tree));
    }

    /**
     * Creates a new match object which stores the given forest in a canonical way: it associates
     * the forest as is with the pattern's name
     *
     * @param match the match object to store the tree in (may be null)
     * @param forest the forest to store
     * @return the new match object, or the given match object if this pattern is not named, or null
     *     if the given match is null or the mapping of the given forest to this pattern's name
     *     conflicts with another mapping
     */
    protected Match<MatchedT> withThisPatternsDefinition(
            final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
        if (match == null) return null;
        if (isNamed()) {
            Optional<? extends Match<MatchedT>> result;
            if (eqTester == null) result = match.withDefinition(name, forest);
            else result = match.withDefinition(name, forest, eqTester);
            return result.orElse(null);
        }
        return match;
    }

    /**
     * Checks whether the canonical mapping of the given forest to this pattern's name conflicts
     * with another mapping in the given match object.
     *
     * @param match the match object to check with (may be null)
     * @param forest the forest which mapping is to be checked
     * @return true iff the given forest can be mapped to this pattern's name in the given match
     *     object
     */
    protected boolean isConsistentWithThisPatternsName(
            final Match<MatchedT> match, @NotNull final List<MatchedT> forest) {
        if (match == null) return false;
        if (isNamed()) return match.isConsistentWith(name, forest);
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////
    // FOREST CREATION
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Creates a tree from the given match.
     *
     * <p>One of the methods {@link #createForest(Match)} and this method needs to be overridden in
     * subclasses.
     *
     * @param match the match object which contains all needed information to create a tree
     * @return if a tree is created by this pattern, this tree, else the first tree of the forest
     *     created by this pattern
     * @throws CreateException if the match object does not provide enough information to create a
     *     tree or by this pattern an empty forest is created
     */
    public MatchedT createTree(@NotNull final Match<MatchedT> match) throws CreateException {
        List<MatchedT> forest = createForest(match);
        if (forest.isEmpty())
            throw new CreateException("For this pattern an empty forest is created.");
        return forest.get(0);
    }

    /**
     * Creates a forest from the given match.
     *
     * <p>One of the methods {@link #createTree(Match)} and this method has to be overridden in
     * subclasses.
     *
     * @param match contains all needed information to create a forest
     * @return the created forest
     * @throws CreateException if the match object does not provide enough information to create a
     *     forest
     */
    public List<MatchedT> createForest(@NotNull final Match<MatchedT> match)
            throws CreateException {
        return Collections.singletonList(createTree(match));
    }

    /**
     * Creates a tree from the given match.
     *
     * <p><b>Implementation note:</b> Delegates to {@link #createTree(Match)}.
     *
     * @param match contains all needed information to create a tree
     * @return the created tree or empty if the match object does not provide enough information to
     *     create a tree
     */
    public Optional<MatchedT> createTreeIfPossible(@NotNull final Match<MatchedT> match) {
        try {
            return Optional.of(createTree(match));
        } catch (CreateException e) {
            return Optional.empty();
        }
    }

    /**
     * Creates a forest from the given match.
     *
     * <p><b>Implementation note:</b> Delegates to {@link #createForest(Match)}.
     *
     * @param match contains all needed information to create a forest
     * @return the created forest or empty if the match object does not provide enough information
     *     to create a forest
     */
    public Optional<List<MatchedT>> createForestIfPossible(@NotNull final Match<MatchedT> match) {
        try {
            return Optional.of(createForest(match));
        } catch (CreateException e) {
            return Optional.empty();
        }
    }

    /**
     * Creates a tree in a canonical way: Just returns the tree associated in the given match with
     * this pattern's name.
     *
     * @param match the match to get the tree from
     * @return the tree contained in the match
     * @throws CreateException if the match object does not provide enough information to create a
     *     tree the described way
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected MatchedT createTreeByMatchAndName(@NotNull final Match<MatchedT> match) {
        if (isNamed() && match.defines(name)) {
            return match.getDefinedTree(name).get();
        }
        throw new CreateException(this, match);
    }

    /**
     * Create a forest in a canonical way: Just returns the forest associated in the match with this
     * pattern's name.
     *
     * @param match the match to get the forest from
     * @return the forest contained in the match
     * @throws CreateException if the match object does not provide enough information to create a
     *     forest the described way
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected List<MatchedT> createForestByMatchAndName(@NotNull final Match<MatchedT> match) {
        if (isNamed() && match.defines(name)) {
            return match.getDefinedForest(name).get();
        }
        throw new CreateException(this, match);
    }

    /**
     * Creates a forest in a canonical way: Just returns the forest formed of the trees (or forests)
     * created by this patterns' subpatterns.
     *
     * @param match the match to get the forest from
     * @return the forest contained in the match
     * @throws CreateException if the match object does not provide enough information to create a
     *     forest the described way
     */
    protected List<MatchedT> createForestByMatchAndSubpatterns(
            @NotNull final Match<MatchedT> match) {
        List<MatchedT> forest = new ArrayList<>();
        for (TreePattern<MatchedT> pattern : getChildren()) {
            forest.addAll(pattern.createForest(match));
        }
        return forest;
    }

    /**
     * Creates a forest in a canonical way: Just returns the forest associated in the match with the
     * pattern's name, if present, and otherwise return the forest formed of the trees (or forests)
     * created by this pattern's subpatterns.
     *
     * @param match the match to get the forest from
     * @return the forest contained in the match
     * @throws CreateException if the match object does not provide enough information to create a
     *     forest in either of the two described ways
     */
    protected List<MatchedT> createForestByMatchAndNameOrSubpatterns(
            @NotNull final Match<MatchedT> match) {
        try {
            return createForestByMatchAndName(match);
        } catch (CreateException e) {
            return createForestByMatchAndSubpatterns(match);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // EQUALS & Co
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((eqTester == null) ? 0 : eqTester.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && Objects.equals(eqTester, ((TreePattern<?>) obj).eqTester);
    }

    @Override
    public String toString() {
        return "TreePattern";
    }

    public abstract TreePattern<MatchedT> clone();

    /**
     * Returns a clone of this pattern with iterated names.
     *
     * <p>To calculate the new name {@link #iterateName(IndexedSymbol, int)} can be used. Iterated
     * names are used in the {@link RepeatForestPattern}.
     *
     * @param iteration the iteration to use
     * @return a clone of this pattern with iterated names
     */
    protected abstract TreePattern<MatchedT> cloneWithIteratedName(int iteration);

    /**
     * Utility method that converts an indexed symbol like <code>xyz[][]^1_2</code> to <code>
     * xyz[iteration][]^1_2</code> where iteration is the value of the parameter.
     *
     * @param symbol the symbol to convert
     * @param iteration the iteration to insert
     * @return the new symbol, or (a new instance of) the same symbol if the symbol's name does not
     *     contain any <code>[]</code>, or null if the given symbol is null
     */
    protected static IndexedSymbol iterateName(final IndexedSymbol symbol, int iteration) {
        if (symbol == null) return null;
        String name = symbol.getName();
        name = name.replaceFirst("\\[]", "[" + iteration + "]");
        return new IndexedSymbol(name, symbol.getSubscript(), symbol.getSuperscript());
    }

    /**
     * Utility method that returns a list of this pattern's children with iterated names according
     * to {@link #cloneWithIteratedName(int)}.
     *
     * @param iteration the iteration to use
     * @return a new list of clones of this pattern's children with (potentially) modified names.
     */
    protected List<TreePattern<MatchedT>> getClonedChildrenWithIteratedNames(int iteration) {
        List<TreePattern<MatchedT>> clonedChildren = new ArrayList<>();
        children.forEach(child -> clonedChildren.add(child.cloneWithIteratedName(iteration)));
        return clonedChildren;
    }
}
