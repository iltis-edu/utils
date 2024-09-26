package de.tudortmund.cs.iltis.utils.explainedresult;

import de.tudortmund.cs.iltis.utils.collections.SerializablePair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @param <S> the computation state
 * @param <R> the intermediate results
 */
public class ComputationLog<S extends ComputationState, R extends Serializable>
        implements Iterable<SerializablePair<S, R>> {

    private List<SerializablePair<S, R>> log;

    public ComputationLog() {
        this.log = new ArrayList<>();
    }

    public Iterator<SerializablePair<S, R>> iterator() {
        return this.log.iterator();
    }

    public SerializablePair<S, R> getEntry(int index) {
        return this.log.get(index);
    }

    public SerializablePair<S, R> getLastEntry() {
        return this.log.get(log.size() - 1);
    }

    public void log(S state, R result) {
        this.log.add(new SerializablePair<>(state, result));
    }

    public int size() {
        return this.log.size();
    }

    public String toString() {
        String result = "";

        for (SerializablePair<S, R> entry : log) {
            result += entry.toString() + "\n";
        }

        return result;
    }
}
