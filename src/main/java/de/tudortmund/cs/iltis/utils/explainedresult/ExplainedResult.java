package de.tudortmund.cs.iltis.utils.explainedresult;

import java.util.Optional;

/**
 * @param <R> the result
 * @param <E> the explanation to the result
 */
public class ExplainedResult<R, E> {

    private R result;
    private Optional<E> explanation;

    public ExplainedResult(R result) {
        this.result = result;
        this.explanation = Optional.empty();
    }

    public ExplainedResult(R result, E explanation) {
        this.result = result;
        this.explanation = Optional.of(explanation);
    }

    public R getResult() {
        return this.result;
    }

    public Optional<E> getExplanation() {
        return this.explanation;
    }
}
