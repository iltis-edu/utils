package de.tudortmund.cs.iltis.utils.graph.bisimulation;

import de.tudortmund.cs.iltis.utils.collections.relations.FiniteBinaryRelation;
import de.tudortmund.cs.iltis.utils.explainedresult.ComputationLog;
import de.tudortmund.cs.iltis.utils.explainedresult.ComputationState;
import de.tudortmund.cs.iltis.utils.explainedresult.ExplainedResult;
import java.io.Serializable;

/**
 * @param <V> The type of vertex values
 */
public interface ComputeBisimulation<V extends Serializable> {

    FiniteBinaryRelation<V> compute();

    ExplainedResult<
                    FiniteBinaryRelation<V>,
                    ComputationLog<ComputationState, BisimulationResult<V>>>
            computeWithExplanation();
}
