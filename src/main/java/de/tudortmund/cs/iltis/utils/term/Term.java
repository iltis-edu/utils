package de.tudortmund.cs.iltis.utils.term;

import de.tudortmund.cs.iltis.utils.io.writer.term.TermWriter;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A representation of an abstract term.
 *
 * @param <SubtermT> type for subterms
 * @param <NameT> type for name
 */
public class Term<SubtermT extends Term<SubtermT, ? extends NameT>, NameT extends Serializable>
        extends Tree<SubtermT> {

    ///////////////////////////////////////////////////////////////////////////
    // CONSTANTS
    ///////////////////////////////////////////////////////////////////////////

    /** For serialization. */
    private static final long serialVersionUID = 1L;

    ///////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Flag, if the arity of this term is fixed; is supposed to be effectively final.
     *
     * <p>The arity of the term equals the size of <code>subterms</code>.
     */
    protected boolean arityFixed;

    /**
     * The name of a term; is supposed to be effectively final.
     *
     * <p>It is null iff the term is not named.
     */
    protected NameT name;

    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////

    public Term(boolean arityFixed) {
        this(arityFixed, (NameT) null);
    }

    @SafeVarargs
    public Term(boolean arityFixed, final SubtermT... subterms) {
        this(arityFixed, null, subterms);
    }

    public Term(boolean arityFixed, final Iterable<? extends SubtermT> subterms) {
        this(arityFixed, (NameT) null, subterms);
    }

    public Term(
            boolean arityFixed,
            final SubtermT subterm,
            final Iterable<? extends SubtermT> subterms) {
        this(arityFixed, null, subterm, subterms);
    }

    public Term(boolean arityFixed, final NameT name) {
        super();
        this.arityFixed = arityFixed;
        this.name = name;
    }

    @SafeVarargs
    public Term(boolean arityFixed, final NameT name, final SubtermT... subterms) {
        super(subterms);
        this.arityFixed = arityFixed;
        this.name = name;
    }

    public Term(boolean arityFixed, final NameT name, final Iterable<? extends SubtermT> subterms) {
        super(subterms);
        this.arityFixed = arityFixed;
        this.name = name;
    }

    public Term(
            boolean arityFixed,
            final NameT name,
            final SubtermT subterm,
            final Iterable<? extends SubtermT> subterms) {
        super(subterm, subterms);
        this.arityFixed = arityFixed;
        this.name = name;
    }

    /** for GTW serialization */
    protected Term() {}

    ///////////////////////////////////////////////////////////////////////////
    // METHODS
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @return The name of this term or <code>null</code> if this term is not named.
     */
    public NameT getName() {
        return name;
    }

    public boolean isNamed() {
        return name != null;
    }

    public boolean isArityFixed() {
        return arityFixed;
    }

    /** Synonym for {@link #getNumberOfChildren()}. */
    public int getArity() {
        return getNumberOfChildren();
    }

    /**
     * Add the specified subterm at the specified position.
     *
     * @param index The index to add this subterm at.
     * @param subterm The subterm to add.
     * @throws UnsupportedOperationException If this term has a fixed arity.
     * @throws IllegalArgumentException If index is lower than zero or greater than the arity of
     *     this term.
     */
    protected void addChild(int index, final SubtermT subterm) {
        if (arityFixed)
            throw new UnsupportedOperationException(
                    "Subterms may not be added to terms with fixed arity");
        super.addChild(index, subterm);
    }

    /**
     * Clones this term.
     *
     * <p><b>Important note:</b> the name of this term is <b>not</b> cloned.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Term<SubtermT, NameT> clone() {
        List<SubtermT> clonedChildren = new ArrayList<>();
        children.forEach(child -> clonedChildren.add((SubtermT) child.clone()));
        return new Term<>(arityFixed, name, clonedChildren);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj)
                && Objects.equals(arityFixed, ((Term<?, ?>) obj).arityFixed)
                && Objects.equals(name, ((Term<?, ?>) obj).name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (arityFixed ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        TermWriter<SubtermT, NameT> writer = new TermWriter<>();
        return writer.write(this);
    }
}
