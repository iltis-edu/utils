package de.tudortmund.cs.iltis.utils.term;

import java.io.Serializable;
import java.util.Objects;

/**
 * {@link ComparableTerm}s are terms that can be compared to one another. The comparison is based on
 * the name of the {@link Term} (which is required to be Comparable), the (fixed) arity, the
 * concrete class name and then recursively by all children.
 *
 * @param <SubtermT> the type of the subterms
 * @param <NameT> the type of the name of this term
 */
public class ComparableTerm<
                SubtermT extends ComparableTerm<SubtermT, NameT>,
                NameT extends Serializable & Comparable<NameT>>
        extends Term<SubtermT, NameT> implements Comparable<SubtermT> {

    public ComparableTerm(boolean arityFixed) {
        this(arityFixed, (NameT) null);
    }

    @SafeVarargs
    public ComparableTerm(boolean arityFixed, final SubtermT... subterms) {
        this(arityFixed, null, subterms);
    }

    public ComparableTerm(boolean arityFixed, final Iterable<? extends SubtermT> subterms) {
        this(arityFixed, (NameT) null, subterms);
    }

    public ComparableTerm(
            boolean arityFixed,
            final SubtermT subterm,
            final Iterable<? extends SubtermT> subterms) {
        this(arityFixed, null, subterm, subterms);
    }

    public ComparableTerm(boolean arityFixed, final NameT name) {
        super();
        this.arityFixed = arityFixed;
        this.name = name;
    }

    @SafeVarargs
    public ComparableTerm(boolean arityFixed, final NameT name, final SubtermT... subterms) {
        super();
        addChildren(subterms);
        this.arityFixed = arityFixed;
        this.name = name;
    }

    public ComparableTerm(
            boolean arityFixed, final NameT name, final Iterable<? extends SubtermT> subterms) {
        super();
        addChildren(subterms);
        this.arityFixed = arityFixed;
        this.name = name;
    }

    public ComparableTerm(
            boolean arityFixed,
            final NameT name,
            final SubtermT subterm,
            final Iterable<? extends SubtermT> subterms) {
        super();
        addChild(subterm);
        addChildren(subterms);
        this.arityFixed = arityFixed;
        this.name = name;
    }

    /** For serialization */
    protected ComparableTerm() {}

    /**
     * Compares this term to the given other term.
     *
     * <p>The following criteria are considered in this order: 1) the name of the term 2) the
     * (fixed) arity 3) the type 4) the children
     *
     * <p>Note: this method is used in the implementation of {@link ComparableTerm#equals(Object)}
     *
     * @param other the other term to compare this one with
     * @return -1 if this term is less, +1 if this term is more, 0 otherwise
     */
    @Override
    public int compareTo(SubtermT other) {
        Objects.requireNonNull(other);

        int orderByName = compareByName(other);
        if (orderByName != 0) return orderByName;

        int orderByArity = compareByArity(other);
        if (orderByArity != 0) return orderByArity;

        int orderByType = compareByType(other);
        if (orderByType != 0) return orderByType;

        return compareByChildren(other);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return 0 == compareTo((SubtermT) obj);
    }

    /**
     * Compare by the number of children (less children is less) and recursively by all children if
     * required
     */
    private int compareByChildren(SubtermT other) {
        int diffSize = getNumberOfChildren() - other.getNumberOfChildren();
        if (diffSize != 0) return diffSize;

        for (int i = 0; i < getNumberOfChildren(); i++) {
            int diffChild = getChild(i).compareTo(other.getChild(i));
            if (diffChild != 0) return diffChild;
        }
        return 0;
    }

    /** Compare types: types are sorted by their class names */
    private int compareByType(SubtermT other) {
        return hasSameClass(other) ? 0 : getClass().getName().compareTo(other.getClass().getName());
    }

    /** Compare if arity is fixed: fixed arity is less than flexible arity */
    private int compareByArity(SubtermT other) {
        if (arityFixed && !other.arityFixed) return -1;
        if (!arityFixed && other.arityFixed) return 1;
        return 0;
    }

    /**
     * Compare names: not named is less than named and if both are named the names themselves are
     * compared
     */
    private int compareByName(SubtermT other) {
        if (isNamed() && !other.isNamed()) return -1;
        if (!isNamed() && other.isNamed()) return 1;
        if (isNamed()) {
            return name.compareTo(other.name);
        }
        return 0;
    }
}
