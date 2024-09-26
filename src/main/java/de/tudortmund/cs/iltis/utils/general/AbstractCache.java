package de.tudortmund.cs.iltis.utils.general;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractCache<I, O> {
    private int capacity;

    public AbstractCache(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Cache size is expected to be positive");
        this.capacity = capacity;
    }

    public abstract void free();

    public abstract Optional<O> get(I input);

    public Optional<O> get(I input, Supplier<O> compute) {
        Optional<O> result = get(input);
        if (!result.isPresent()) {
            this.put(input, compute.get());
            result = get(input);
        }
        return result;
    }

    public Optional<O> get(I input, Function<I, O> compute) {
        Optional<O> result = get(input);
        if (!result.isPresent()) {
            this.put(input, compute.apply(input));
            result = get(input);
        }
        return result;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public abstract int getSize();

    public boolean hasHit(I input) {
        return this.get(input).isPresent();
    }

    public void put(I input, O output) {
        if (this.getSize() >= this.capacity) this.free();
    }
}
