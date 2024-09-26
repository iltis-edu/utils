package de.tudortmund.cs.iltis.utils.function;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * A normal {@link Consumer} which also implements the {@link Serializable} interface (to satisfy
 * GWT).
 */
@FunctionalInterface
public interface SerializableConsumer<T> extends Consumer<T>, Serializable {}
