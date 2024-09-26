package de.tudortmund.cs.iltis.utils.collections;

import java.io.Serializable;
import java.util.Objects;

/**
 * An abstract, <b>immutable</b> class to represent a fault. In simple words, a fault is an
 * (arbitrary) object with a reason of a certain type.
 *
 * <p>Faults and {@link FaultCollection}s are mostly used to report specific and multiple errors,
 * though other uses seem possible. Because, in this context, fault objects may be contained in
 * exceptions, extending classes are encouraged to be immutable.
 *
 * @param <ReasonT> the type of the reason
 */
public abstract class Fault<ReasonT extends Serializable> implements Cloneable, Serializable {

    private ReasonT reason;

    /**
     * Constructs a new Fault with the given reason.
     *
     * @throws NullPointerException if reason is null
     */
    public Fault(ReasonT reason) {
        Objects.requireNonNull(reason);
        this.reason = reason;
    }

    public ReasonT getReason() {
        return reason;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Fault<?> other = (Fault<?>) obj;
        if (reason == null) {
            return other.reason == null;
        } else return reason.equals(other.reason);
    }

    @Override
    public String toString() {
        return "Fault [reason = " + reason + "]";
    }

    protected abstract Object clone();

    /** For serialization */
    private static final long serialVersionUID = 1L;

    /** For serialization */
    protected Fault() {}
}
