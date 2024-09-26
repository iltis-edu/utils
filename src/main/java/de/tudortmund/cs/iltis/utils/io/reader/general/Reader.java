package de.tudortmund.cs.iltis.utils.io.reader.general;

/**
 * Basic interface for readers, in particular for parsers.
 *
 * @param <T> target type
 */
public interface Reader<T> {
    T read(Object o) throws RuntimeException;
}
