package de.tudortmund.cs.iltis.utils;

import de.tudortmund.cs.iltis.utils.collections.Fault;
import java.io.Serializable;

/**
 * A specific fault to report an error concerning two indexed symbols.
 *
 * @param <ReasonT> the type of the reason
 */
public class SymbolFault<ReasonT extends Serializable> extends Fault<ReasonT> {

    /** no getters and setters to allow subclasses to use own names for symbols */
    protected IndexedSymbol firstSymbol;

    /** no getters and setters to allow subclasses to use own names for symbols */
    protected IndexedSymbol secondSymbol;

    /**
     * Constructs a new SymbolFault with the given properties. The two symbols may be null
     *
     * @throws NullPointerException if reason is null
     */
    protected SymbolFault(ReasonT reason, IndexedSymbol firstSymbol, IndexedSymbol secondSymbol) {
        super(reason);
        this.firstSymbol = firstSymbol;
        this.secondSymbol = secondSymbol;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((firstSymbol == null) ? 0 : firstSymbol.hashCode());
        result = prime * result + ((secondSymbol == null) ? 0 : secondSymbol.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        SymbolFault<?> other = (SymbolFault<?>) obj;
        if (firstSymbol == null) {
            if (other.firstSymbol != null) return false;
        } else if (!firstSymbol.equals(other.firstSymbol)) return false;
        if (secondSymbol == null) {
            return other.secondSymbol == null;
        } else return secondSymbol.equals(other.secondSymbol);
    }

    @Override
    public String toString() {
        return "SymbolFault [reason = "
                + getReason()
                + ", firstSymbol = "
                + firstSymbol
                + ", secondSymbol = "
                + secondSymbol
                + "]";
    }

    @Override
    protected Object clone() {
        return new SymbolFault<>(getReason(), firstSymbol, secondSymbol);
    }

    /** for serialization */
    private static final long serialVersionUID = 1L;

    /** for GWT serialization */
    protected SymbolFault() {}
}
