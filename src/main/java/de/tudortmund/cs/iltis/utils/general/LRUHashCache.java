package de.tudortmund.cs.iltis.utils.general;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Optional;

public class LRUHashCache<I, O> extends AbstractCache<I, O> {
    private ArrayDeque<I> inputs;
    private HashMap<I, O> values;

    public LRUHashCache(int capacity) {
        super(capacity);
        this.inputs = new ArrayDeque<>();
        this.values = new HashMap<>();
    }

    public void free() {
        I lruInput = this.inputs.getLast();
        values.remove(lruInput);
        inputs.removeLast();
    }

    public Optional<O> get(I input) {
        if (this.values.containsKey(input)) {
            this.inputs.remove(input);
            this.inputs.addFirst(input);
            return Optional.of(this.values.get(input));
        }
        return Optional.empty();
    }

    public int getSize() {
        return inputs.size();
    }

    public void put(I input, O output) {
        this.inputs.remove(input);
        super.put(input, output);
        this.inputs.addFirst(input);
        this.values.put(input, output);
    }
}
