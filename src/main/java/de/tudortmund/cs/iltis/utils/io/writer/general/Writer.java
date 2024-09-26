package de.tudortmund.cs.iltis.utils.io.writer.general;

import java.util.function.Function;

@FunctionalInterface
public interface Writer<T> extends Function<T, String> {
    String write(T object);

    @Override
    default String apply(T t) {
        return write(t);
    }
}
