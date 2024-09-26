package de.tudortmund.cs.iltis.utils.io.parsable;

import de.tudortmund.cs.iltis.utils.collections.Pair;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * A wrapper to make arbitrary references -- <tt>null</tt> references to in particular
 * required/optional and so on.
 *
 * @param <T> the target type
 * @param <S> the source type
 */
public class ParsableWrapper<T, S extends Parsable<T, C>, C> extends ParsableSource<T, S, C> {
    private S source = null;

    ParsableWrapper(Parsable<?, ?> parent, Field field) throws ParsableInvalidSpecification {
        try {
            this.source = (S) field.get(parent);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            ParsableInvalidSpecification pe = new ParsableInvalidSpecification(parent);
            pe.addSuppressed(e);
            throw pe;
        }

        Pair<String, String> nameAndType = ParsableHelper.getNameAndType(source, field);
        this.getMeta().name = nameAndType.first();
        this.getMeta().type = nameAndType.second();
        this.getMeta().parent = parent;
    }

    @Override
    public S getSource() {
        return source;
    }

    @Override
    public T value(Optional<C> context) {
        return super.value(context);
    }

    @Override
    protected T parse(Optional<C> context) {
        S source = getSource();
        if (source == null) return null;
        return source.value(context);
    }
}
