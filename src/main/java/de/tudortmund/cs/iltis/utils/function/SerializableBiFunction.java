package de.tudortmund.cs.iltis.utils.function;

import java.io.Serializable;
import java.util.function.BiFunction;

public interface SerializableBiFunction<I1, I2, O> extends BiFunction<I1, I2, O>, Serializable {}
