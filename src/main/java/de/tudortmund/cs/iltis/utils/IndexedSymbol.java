package de.tudortmund.cs.iltis.utils;

import de.tudortmund.cs.iltis.utils.io.reader.general.Reader;
import de.tudortmund.cs.iltis.utils.io.reader.general.SafeTextIndexedSymbolReader;
import de.tudortmund.cs.iltis.utils.io.writer.general.HTMLIndexedSymbolWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.LatexIndexedSymbolWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.SafeTextIndexedSymbolWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.TextIndexedSymbolWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A symbol consisting of an arbitrary string as name with arbitrary strings as subscript and
 * superscript. Immutable class.
 */
public class IndexedSymbol implements Comparable<IndexedSymbol>, Serializable, Cloneable {

    /**
     * Creates a new indexed symbol with the specified name, subscript and superscript.
     *
     * @param name may not be null
     * @param subscript is interpreted as empty string if null
     * @param superscript is interpreted as empty string if null
     * @throws IllegalArgumentException if name is null or empty
     */
    public IndexedSymbol(final String name, final String subscript, final String superscript) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("name may be neither null nor empty.");
        this.name = name;
        this.subscript = subscript == null ? "" : subscript;
        this.superscript = superscript == null ? "" : superscript;
    }

    /**
     * Creates a new indexed symbol with the name, subscript and superscript read from the argument.
     *
     * <p>The argument has to be of the form <code>w^s_t</code> or <code>w_t^s</code> where <code>
     * w,s,t</code> are non-empty strings not containing "^" and "_". The parts <code>^s</code> and
     * <code>_t</code> are optional.
     *
     * @throws IllegalArgumentException if parseString is not of this form
     */
    public IndexedSymbol(final String parseString) {
        IndexedSymbol symbol = reader.read(parseString);
        this.name = symbol.name;
        this.subscript = symbol.subscript;
        this.superscript = symbol.superscript;
    }

    public String getName() {
        return name;
    }

    public boolean hasSubscript() {
        return !subscript.isEmpty();
    }

    public String getSubscript() {
        return subscript;
    }

    public boolean hasSuperscript() {
        return !superscript.isEmpty();
    }

    public String getSuperscript() {
        return superscript;
    }

    public IndexedSymbol clone() {
        return new IndexedSymbol(name, subscript, superscript);
    }

    /**
     * Checks if this symbol is a prefix of the other.
     *
     * <p>The implementation is agnostic of any ordering of sub- and superscripts. For example,
     * `a_1^k` is considered a prefix of `a_12^k`, because one could use another representation to
     * obtain `a^k_1` and `a^k_12` where the former is obviously a prefix of the latter.
     *
     * @param other the symbol to compare this one with
     * @return true if symbol is a prefix of other
     */
    public boolean isPrefixOf(IndexedSymbol other) {
        if (equals(other)) {
            return true;
        } else if (getName().equals(other.getName())
                && getSuperscript().equals(other.getSuperscript())) {
            // name^superscript is common prefix of both
            return other.getSubscript().startsWith(getSubscript());
        } else if (getName().equals(other.getName())
                && getSubscript().equals(other.getSubscript())) {
            // name_subscript is common prefix of both
            return other.getSuperscript().startsWith(getSuperscript());
        } else {
            // name is common prefix, but not equal => no sub- or superscript can follow, otherwise
            // this distinguishes both symbols
            return other.getName().startsWith(getName()) && !hasSubscript() && !hasSuperscript();
        }
    }

    /**
     * Checks that for no two IndexedSymbols one is the prefix of the other.
     *
     * @param indexedSymbols the symbols to test
     * @return true, if no symbol is the prefix of any other
     */
    public static boolean isPrefixFree(Collection<IndexedSymbol> indexedSymbols) {
        List<IndexedSymbol> symbols = new ArrayList<>(indexedSymbols);
        for (int i = 0; i < symbols.size(); i++) {
            IndexedSymbol previous = symbols.get(i);
            for (int j = i + 1; j < symbols.size(); j++) {
                IndexedSymbol current = symbols.get(j);
                if (current.isPrefixOf(previous) || previous.isPrefixOf(current)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + subscript.hashCode();
        result = prime * result + superscript.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof IndexedSymbol)) return false;

        IndexedSymbol other = (IndexedSymbol) o;
        return compareTo(other) == 0;
    }

    @Override
    public int compareTo(IndexedSymbol other) {
        final int namecomp = this.name.compareTo(other.name);
        final int supcomp = this.superscript.compareTo(other.superscript);
        final int subcomp = this.subscript.compareTo(other.subscript);

        if (namecomp != 0) return namecomp;
        if (supcomp != 0) return supcomp;
        return subcomp;
    }

    @Override
    public String toString() {
        return toSafeTextString();
    }

    public String toTextString() {
        return textWriter.write(this);
    }

    public String toSafeTextString() {
        return safeTextWriter.write(this);
    }

    public String toHTMLString() {
        return htmlWriter.write(this);
    }

    public String toLatexString() {
        return latexWriter.write(this);
    }

    /** never null */
    private String name;

    /** never null */
    private String subscript;

    /** never null */
    private String superscript;

    @SuppressWarnings("UnnecessaryModifier")
    private static final transient Reader<IndexedSymbol> reader = new SafeTextIndexedSymbolReader();

    @SuppressWarnings("UnnecessaryModifier")
    private static final transient Writer<IndexedSymbol> textWriter = new TextIndexedSymbolWriter();

    @SuppressWarnings("UnnecessaryModifier")
    private static final transient Writer<IndexedSymbol> safeTextWriter =
            new SafeTextIndexedSymbolWriter();

    @SuppressWarnings("UnnecessaryModifier")
    private static final transient Writer<IndexedSymbol> htmlWriter = new HTMLIndexedSymbolWriter();

    @SuppressWarnings("UnnecessaryModifier")
    private static final transient Writer<IndexedSymbol> latexWriter =
            new LatexIndexedSymbolWriter();

    /** Needed for serialization */
    private static final long serialVersionUID = 1L;

    /** Needed for GWT serialization */
    protected IndexedSymbol() {}
}
