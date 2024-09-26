package de.tudortmund.cs.iltis.utils.tree;

import de.tudortmund.cs.iltis.utils.general.Data;
import de.tudortmund.cs.iltis.utils.graph.Graph;
import de.tudortmund.cs.iltis.utils.io.writer.tree.TreeWriter;
import de.tudortmund.cs.iltis.utils.tree.transformations.Transformation;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Basic class for tree-like structures.
 *
 * <p>All methods, which can alter a tree object (currently the add-methods), are protected to
 * prevent modification from other classes. If the possibility of such modifications is desired,
 * these methods can be made public by overriding in subclasses.
 *
 * @param <T> The type of subtrees
 */
public class Tree<T extends Tree<T>> implements Serializable, Cloneable {

    ///////////////////////////////////////////////////////////////////////////
    // STATIC ATTRIBUTES AND METHODS
    ///////////////////////////////////////////////////////////////////////////

    /** For serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a chain, i.e. a tree of nodes with only one child each, out of the given nodes
     *
     * @param children the nodes to chain
     * @param <T> the type of nodes
     * @return the root of the constructed chain
     */
    // here "? super T" is used, to avoid an error in ModalTableau.saturate(FormulaNode,
    // Conjunction)
    public static <T extends Tree<? super T>> T chain(Iterable<? extends T> children) {
        Iterator<? extends T> it = children.iterator();
        T root = it.hasNext() ? it.next() : null;

        T last = root;
        while (it.hasNext()) {
            T node = it.next();
            last.addChild(node);
            last = node;
        }

        return root;
    }

    ///////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////

    /** List of all children of this tree. */
    protected List<T> children;

    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Constructs a tree with the specified children.
     *
     * <p>Internally, the given data structure is not used. That means, that changes in the given
     * data structure do not reflect on this tree. Changes in the single children do reflect on this
     * tree, however.
     *
     * <p>Be careful, when you pass a Tree-object to this constructor. Since Tree-objects itself are
     * iterable, all children of the passed tree are inserted. You can force the use of the
     * constructor {@link #Tree(Tree...)} by using <code>new Tree(new Tree[] {child})</code>.
     *
     * @param children The initial children of this tree
     * @throws NullPointerException if any child is {@code null}
     */
    public Tree(Iterable<? extends T> children) {
        this();
        addChildren(children);
    }

    /**
     * Constructs a tree with the specified children.
     *
     * <p>Be careful, when you pass only one Tree-object to this constructor. Since Tree-objects
     * itself are iterable, the constructor {@link #Tree(Iterable)} will then be used instead and
     * all children of the passed tree are inserted. You can force the use of this constructor by
     * using <code>new Tree(new Tree[] {child})</code>.
     *
     * @param children The initial children of this tree
     * @throws NullPointerException if any child is {@code null}
     */
    @SafeVarargs
    public Tree(T... children) {
        this();
        addChildren(children);
    }

    /** Constructs a tree without children. */
    public Tree() {
        this.children = new ArrayList<>();
    }

    /**
     * Constructs a tree with one child and other children.
     *
     * @param child the one child to add first
     * @param children the other children
     * @throws NullPointerException if child, children or any element of children is {@code null}
     */
    public Tree(T child, Iterable<? extends T> children) {
        this();
        addChild(child);
        addChildren(children);
    }

    ///////////////////////////////////////////////////////////////////////////
    // METHODS
    ///////////////////////////////////////////////////////////////////////////

    // Children management

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public int getNumberOfChildren() {
        return children.size();
    }

    /**
     * Returns the specified child.
     *
     * @param index The index specifying the child to return.
     * @return The child at the position specified by index.
     * @throws IllegalArgumentException If index is lower than zero or greater than or equal to the
     *     number of children of this tree.
     */
    public T getChild(int index) {
        if (index < 0)
            throw new IllegalArgumentException("Child index may not be negative: " + index);
        if (index >= children.size())
            throw new IllegalArgumentException(
                    "Child index exceeds last child index: " + index + "/" + children.size());
        return children.get(index);
    }

    /**
     * Return a list of all children of this tree.
     *
     * <p>The returned object is a copy of the internal children list. That means, that changes in
     * the returned list do not reflect on this tree. Changes in the single children do reflect on
     * this tree, however.
     *
     * @return A list of children.
     */
    public List<T> getChildren() {
        return Data.newArrayList(children);
    }

    /**
     * Add the specified subtree at last position.
     *
     * <p><b>Implementation note:</b> Delegates to {@link #addChild(int, Tree)}.
     *
     * @param subtree The subtree to add.
     * @throws NullPointerException if subtree is {@code null}
     */
    protected void addChild(final T subtree) {
        addChild(children.size(), subtree);
    }

    /**
     * Add the specified subtrees at last position.
     *
     * <p><b>Implementation note:</b> Delegates to {@link #addChild(int, Tree)}.
     *
     * @param subtrees The subtrees to add.
     * @throws NullPointerException if subtrees or any element of subtrees is {@code null}
     */
    protected void addChildren(final Iterable<? extends T> subtrees) {
        if (subtrees == null) throw new NullPointerException("subtrees may not be null");
        for (T subtree : subtrees)
            if (subtree == null) throw new NullPointerException("No subtree may be null");
        subtrees.forEach(this::addChild);
    }

    /**
     * Add the specified subtrees at last position.
     *
     * <p><b>Implementation note:</b> Delegates to {@link #addChild(int, Tree)}.
     *
     * @param subtrees The subtrees to add.
     * @throws NullPointerException if subtrees or any element of subtrees is {@code null}
     */
    @SafeVarargs
    protected final void addChildren(final T... subtrees) {
        if (subtrees == null) throw new NullPointerException("subtrees may not be null");
        for (T subtree : subtrees)
            if (subtree == null) throw new NullPointerException("No subtree may be null");
        for (T subtree : subtrees) addChild(subtree);
    }

    /**
     * Add the specified subtree at the specified position.
     *
     * @param index The index to add this subtree at.
     * @param subtree The subtree to add.
     * @throws IllegalArgumentException If index is lower than zero or greater than the number of
     *     children of this tree.
     * @throws NullPointerException if subtree is {@code null}
     */
    protected void addChild(int index, final T subtree) {
        if (index < 0)
            throw new IllegalArgumentException("Subtree index may not be negative: " + index);
        if (index > children.size())
            throw new IllegalArgumentException(
                    "Subtree index exceeds number of children: " + index + "/" + children.size());
        if (subtree == null) throw new NullPointerException("Subtree may not be null");
        children.add(index, subtree);
    }

    // Iterator management

    @SuppressWarnings("unchecked")
    public PostorderIterator<T> postorderDescendantIterator() {
        return new PostorderIterator<>((T) this);
    }

    @SuppressWarnings("unchecked")
    public PreorderIterator<T> preorderDescendantIterator() {
        return new PreorderIterator<>((T) this);
    }

    public Iterator<T> anyDescendantIterator() {
        return postorderDescendantIterator();
    }

    public List<T> getDescendants(Iterator<? extends T> iterator) {
        List<T> descendants = new ArrayList<>();
        iterator.forEachRemaining(descendants::add);
        return descendants;
    }

    public List<T> getDescendants() {
        return getDescendants(anyDescendantIterator());
    }

    public List<T> getLeaves() {
        return Data.filter(this.getDescendants(), Tree::isLeaf);
    }

    // Traversals

    /**
     * Executes a traversal of this tree according to the specified strategy.
     *
     * <ul>
     *   <li>{@link TraversalStrategy#inspect(Object, List)} is called in post-order after
     *       traversing the node and <em>all</em> of its children.
     *   <li>{@link TraversalStrategy#preinspect(Object, List)} is called in post-order after
     *       traversing the node and <em>any</em> of its children.
     *   <li>{@link TraversalStrategy#visit(Object)} is called in pre-order.
     *   <li>{@link TraversalStrategy#nextSibling()} is called after traversing a node.
     *   <li>{@link TraversalStrategy#nextLevel()} is called when entering a lower level, even it is
     *       does not contain any nodes.
     *   <li>{@link TraversalStrategy#previousLevel()} is called when returning to higher level.
     * </ul>
     *
     * @param strategy The strategy to use to calculate an output.
     * @return The output of this traversal.
     */
    @SuppressWarnings("unchecked")
    public <O> O traverse(TraversalStrategy<T, O> strategy) {
        List<O> childrenOutput = new ArrayList<>();
        strategy.visit((T) this);
        strategy.nextLevel();
        for (T child : children) {
            childrenOutput.add(child.traverse(strategy));
            Optional<O> preresult = strategy.preinspect((T) this, childrenOutput);
            if (preresult.isPresent()) return preresult.get();
            strategy.nextSibling();
        }
        strategy.previousLevel();
        return strategy.inspect((T) this, childrenOutput);
    }

    public Set<TreePath> getAllApplications(Transformation<T> transformation) {
        return this.traverse(new ApplicationCollector<>(transformation));
    }

    public T retrieve(final TreePath path) {
        return this.retrieve(path, 0);
    }

    @SuppressWarnings("unchecked")
    protected T retrieve(final TreePath path, final int index) {
        if (path.size() == index) return (T) this;
        return this.getChild(path.get(index)).retrieve(path, index + 1);
    }

    public Tree<T> transform(TreePath path, T newTree) {
        if (path.isEmpty()) {
            return newTree;
        }

        Tree<T> result = this.clone();
        path = path.clone();
        int childIndex = path.lastElement();
        T parent = result.retrieve(path.up());
        parent.children.set(childIndex, newTree);
        return result;
    }

    /** Clones this tree. */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Tree<T> clone() {
        return new Tree<>(getClonedChildren());
    }

    /*
     * Alternative implementation for clone(),
     * which would minimize effort for overwriting in subclasses
     * But GWT does not support use of reflection API

    	Tree<T> clone;
    	try {
    		@SuppressWarnings("rawtypes")
    		Constructor<? extends Tree> constr = this.getClass().getDeclaredConstructor();
    		constr.setAccessible(true);
    		clone = constr.newInstance();
    		constr.setAccessible(false);
    	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
    			| InvocationTargetException | NoSuchMethodException | SecurityException e) {
    		throw new RuntimeException("Cannot clone " + this, e);
    	}
    	List<T> clonedChildren = new ArrayList<>();
    	children.forEach(child -> clonedChildren.add((T) child.clone()));
    	clone.children = clonedChildren;
    	return clone;
    */

    @SuppressWarnings("unchecked")
    protected List<T> getClonedChildren() {
        List<T> clonedChildren = new ArrayList<>();
        children.forEach(child -> clonedChildren.add((T) child.clone()));
        return clonedChildren;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        return result;
    }

    /**
     * Checks equality of this tree against the given object.
     *
     * <p>This method should work on arbitrary subclasses of {@link Tree}. The given tree is
     * considered to be equal to this tree, if
     *
     * <ol>
     *   <li>it is the same object (same identity)
     *   <li>it has the exact same type
     *   <li>it is identical with regard to the structure (same number of children) and all children
     *       are identical wrt. structure
     *   <li>it is equal to the other tree and all children are also equal.
     * </ol>
     *
     * <p>If your subclass specifies more attributes, this method should of course be overridden. We
     * suggest the following manner:
     *
     * <pre>{@code
     * class MySubclass extends Tree<...> {
     *     int a;
     *     OtherClass b;
     *
     *     boolean equals(Object obj) {
     *         return super.equals(obj) &&                   // [1]
     *            a == ((MySubclass) obj).a) &&              // [2]
     *            Objects.equals(b, ((MySubclass) obj).b);   // [3]
     *     }
     * }
     *
     * // [1]: tests (this == null), (obj != null), tree structure (recursively), the type, e.g. Box != Diamond and tree equality (recursively)
     * // [2]: `==` for primitives and because of [1] the cast is safe
     * // [3]: `Objects.equals(x, y)` is null safe and because of [1] the cast is still safe
     * }</pre>
     *
     * <p>If you want multiple subclasses (e.g. A and B of Tree) and there are cases where {@code
     * a.equals(b)} may be {@code true} you should <b>not</b> call super, because this
     * implementation checks type equality.
     *
     * @param obj the object to compare equality against
     * @return whether this tree is equal to obj
     */
    @SuppressWarnings({"unchecked", "EqualsWhichDoesntCheckParameterClass"})
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !hasSameClass(obj)) return false;
        T other = (T) obj;

        if (getNumberOfChildren() != other.getNumberOfChildren()) return false;

        for (int i = 0; i < getNumberOfChildren(); ++i) {
            if (!getChild(i).equals(other.getChild(i))) return false;
        }
        return true;
    }

    public <V extends Tree<T>, E extends Serializable, G extends Graph<V, E>> G toGraph(
            Supplier<G> supplier,
            BiFunction<V, V, E> edgeGenerator,
            Function<Tree<T>, V> vertexCaster) {
        G graph = supplier.get();
        graph.addVertex(vertexCaster.apply(this));
        children.forEach(
                e -> e.addToGraph(graph, vertexCaster.apply(this), edgeGenerator, vertexCaster));
        return graph;
    }

    protected <V extends Tree<T>, E extends Serializable, G extends Graph<V, E>> void addToGraph(
            G graph,
            V parent,
            BiFunction<V, V, E> edgeGenerator,
            Function<Tree<T>, V> vertexCaster) {
        V parentNode = vertexCaster.apply(parent);
        V thisNode = vertexCaster.apply(this);

        graph.addVertex(thisNode);
        graph.addEdge(parentNode, thisNode, edgeGenerator.apply(parentNode, thisNode));
        children.forEach(e -> e.addToGraph(graph, thisNode, edgeGenerator, vertexCaster));
    }

    @Override
    public String toString() {
        TreeWriter<T> writer = new TreeWriter<>();
        return writer.write(this);
    }

    protected boolean hasSameClass(Object o) {
        return this.getClass() == o.getClass();
    }

    // ApplicationCollector -----------------------------------------
    private static class ApplicationCollector<T extends Tree<T>> extends PathCollector<T> {
        public ApplicationCollector(final Transformation<T> transformation) {
            this.transformation = transformation;
        }

        @Override
        protected boolean isSatisfying(T item) {
            return this.transformation.isApplicable(item);
        }

        private final Transformation<T> transformation;
    }
}
