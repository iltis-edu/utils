package de.tudortmund.cs.iltis.utils.graph.bisimulation;

import java.io.Serializable;

/**
 * @param <V> the type of vertex values
 */
public interface InsimilarityWitness<V> extends Serializable {

    boolean equals(Object o);
}
