package de.tudortmund.cs.iltis.utils.function;

import java.io.Serializable;

public interface SerializableTriFunction<T, U, V, R>
        extends TriFunction<T, U, V, R>, Serializable {}
