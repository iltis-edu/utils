package de.tudortmund.cs.iltis.utils.io.parsable;

import de.tudortmund.cs.iltis.utils.explainedresult.ExplainedResult;
import java.util.Optional;
import javax.xml.bind.annotation.XmlTransient;

/**
 * A datum of (source) type S that is a <tt>Parsable</tt> of (target) type T.
 *
 * @param <T> the target type
 * @param <S> the source type
 */
@XmlTransient
public abstract class ParsableSource<T, S, C> extends Parsable<T, C> {
    /**
     * Get the underlying source datum, which is {@code null} if it is not provided.
     *
     * @return the source datum
     */
    protected abstract S getSource();

    /**
     * Tests whether a source datum has been provided.
     *
     * @return {@code true} if a source datum is provided
     */
    public final boolean isPresent() {
        return getSource() != null;
    }

    /**
     * Tests whether a source datum has been provided.
     *
     * @return {@code true} if a source datum is not provided
     */
    public final boolean isMissing() {
        return getSource() == null;
    }

    /**
     * Determines the result value corresponding to the source value, depending on the {@code
     * context}.
     *
     * <p>If the source value is {@code null} and a default value is set, then this default value is
     * considered as the <i>possible</i> result value. Otherwise, A {@link ParsableMissingRequired}
     * exception is thrown if this {@code Parsable} is marked as required.
     *
     * <p>If the source value is not {@code null}, then the {@link Parsable#parse(Optional)} method
     * is called, to compute a <i>possible</i> result value from the source value.
     *
     * <p>If no exception has been thrown, the validity of the <i>possible</i> result value is
     * checked via method {@link Parsable#isValid()}. If the check is successful, then the
     * <i>possible</i> result value is the <i>actual</i> result value, which is returned. Otherwise,
     * a {@link ParsableInvalidValue} exception is thrown.
     *
     * @param context the context provided to {@link Parsable#parse(Optional)}
     * @return the result value determined by the configuration and the source value
     */
    @Override
    protected final T determineValue(Optional<C> context) {
        T value = null;
        if (getSource() == null) {
            if (getOptions().defaultValue.isPresent()) {
                value = getOptions().defaultValue.get();
            } else if (getOptions().required) {
                throw new ParsableMissingRequired(this);
            }
        } else value = parse(context);

        ExplainedResult<Boolean, String> valid = isValid(value);
        if (valid.getResult()) return value;
        else throw new ParsableInvalidValue(this, valid.getExplanation().orElse("<no details>"));
    }
}
