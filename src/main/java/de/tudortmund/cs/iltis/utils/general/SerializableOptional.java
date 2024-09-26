package de.tudortmund.cs.iltis.utils.general;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;

/** Simplified serializable implementation of {@link java.util.Optional} */
public class SerializableOptional<T extends Serializable> implements Serializable {

    private T value;

    // Empty instance
    private static SerializableOptional<?> EMPTY = new SerializableOptional<>();

    private SerializableOptional() {
        this.value = null;
    }

    private SerializableOptional(T value) {
        this.value = Objects.requireNonNull(value);
    }

    // Creation
    public static <T extends Serializable> SerializableOptional<T> of(T value) {
        return new SerializableOptional<>(value);
    }

    public static <T extends Serializable> SerializableOptional<T> ofNullable(T value) {
        return value != null ? of(value) : empty();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> SerializableOptional<T> empty() {
        return (SerializableOptional<T>) EMPTY;
    }

    // Checks
    public boolean isPresent() {
        return value != null;
    }

    public boolean isEmpty() {
        return !isPresent();
    }

    // Get
    public T get() throws NoSuchElementException {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public T getOrElse(T elseValue) {
        return value != null ? value : elseValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerializableOptional<?> that = (SerializableOptional<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
