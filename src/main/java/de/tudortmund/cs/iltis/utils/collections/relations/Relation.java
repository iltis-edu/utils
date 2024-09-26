package de.tudortmund.cs.iltis.utils.collections.relations;

import de.tudortmund.cs.iltis.utils.collections.Tuple;
import java.io.Serializable;

/**
 * Basic interface for implementations of relations. Will be extended if needed.
 *
 * @param <T> The type of the elements of the underlying universe
 */
public interface Relation<T> extends Serializable {

    boolean contains(Tuple<T> element);

    int arity();
}
