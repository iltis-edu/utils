package de.tudortmund.cs.iltis.utils.collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An abstract, <b>immutable</b> class to represent a collection (list) of faults. In simple words,
 * a fault is an (arbitrary) object with a reason of a certain type. In respect to this reason, this
 * class offers some methods to filter the contained faults.
 *
 * <p>{@link Fault}s and FaultCollections are mostly used to report specific and multiple errors,
 * though other uses seem possible.
 *
 * @param <ReasonT> the type of the reason used in the collected faults
 * @param <FaultT> the type of the collected faults
 */
public abstract class FaultCollection<ReasonT extends Serializable, FaultT extends Fault<ReasonT>>
        implements Serializable, Cloneable {

    protected List<FaultT> faults;

    /** Constructs an empty fault collection. */
    protected FaultCollection() {
        faults = new ArrayList<>();
    }

    /**
     * Constructs a fault collection containing the given faults. The given list object is cloned
     * and therefore later changes of the list do not reflect on this object. Changes in the single
     * fault objects do reflect on this object, however.
     *
     * @throws NullPointerException if faults is null
     */
    protected FaultCollection(List<FaultT> faults) {
        Objects.requireNonNull(faults);
        this.faults = new ArrayList<>(faults);
    }

    /**
     * Constructs a new fault collection containing additionally the given fault.
     *
     * @throws NullPointerException if fault is null
     * @return the newly created fault collection
     */
    public FaultCollection<ReasonT, FaultT> withFault(FaultT fault) {
        Objects.requireNonNull(fault);
        FaultCollection<ReasonT, FaultT> clone = this.clone();
        clone.faults.add(fault);
        return clone;
    }

    /**
     * Constructs a new fault collection containing additionally the given faults.
     *
     * @throws NullPointerException if faults is null
     * @return the newly created fault collection
     */
    public FaultCollection<ReasonT, FaultT> withFaults(
            FaultCollection<? extends ReasonT, ? extends FaultT> faults) {
        Objects.requireNonNull(faults);
        FaultCollection<ReasonT, FaultT> clone = this.clone();
        clone.faults.addAll(faults.getFaults());
        return clone;
    }

    /**
     * Returns all faults of the given reason. The returned list is a copy of the internal list and
     * therefore later changes of the list do not reflect on this object. Changes in the single
     * fault objects do reflect on this object, however.
     */
    public List<FaultT> getFaults(ReasonT reason) {
        return faults.stream()
                .filter(fault -> fault.getReason() == reason)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns all faults. The returned list is a copy of the internal list and therefore later
     * changes of the list do not reflect on this object. Changes in the single fault objects do
     * reflect on this object, however.
     */
    public List<FaultT> getFaults() {
        return new ArrayList<>(faults);
    }

    /**
     * Returns a map of the contained fault. The keys of this map are the contained reasons and the
     * values are lists of the contained faults for this reason.
     */
    public Map<ReasonT, List<FaultT>> getFaultMap() {
        return faults.stream().collect(Collectors.groupingBy(Fault::getReason));
    }

    /** Checks, whether this fault collection contains any fault. */
    public boolean containsAnyFault() {
        return !faults.isEmpty();
    }

    /** Checks, whether this fault collection contains any fault of the given reason. */
    public boolean containsAnyFault(ReasonT reason) {
        return faults.stream().anyMatch(fault -> fault.getReason() == reason);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((faults == null) ? 0 : faults.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FaultCollection<?, ?> other = (FaultCollection<?, ?>) obj;
        if (faults == null) {
            return other.faults == null;
        } else return faults.equals(other.faults);
    }

    @Override
    public String toString() {
        return "FaultCollection [faults = " + faults + "]";
    }

    public abstract FaultCollection<ReasonT, FaultT> clone();

    /** for serialization */
    private static final long serialVersionUID = 1L;
}
