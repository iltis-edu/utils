package de.tudortmund.cs.iltis.utils.io.parsable;

import de.tudortmund.cs.iltis.utils.explainedresult.ExplainedResult;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Abstract parsable object that may yield a datum of type T. A <tt>Parsable</tt> object contains
 * <tt>options</tt> that specify whether an input is required or optional and, in the latter case,
 * whether a default value should be provided if no input has been specified. Additionally,
 * <tt>Parsable</tt> object stores meta data that is intended to help localizing possibly errors
 * (missing inputs, invalid inputs). The meta data comprises a type description of the input
 * (element, attribute, value), and, if available, a name and a reference to a parent object.
 *
 * @param <T> the type of the possible result.
 * @param <C> the type of the context
 */
@XmlTransient
public abstract class Parsable<T, C> {
    private final Parsable<T, C>.Meta meta = new Meta();
    private final Parsable.Options<T> options = new Options<T>();

    public Parsable() {
        setUpIfRootElement();
    }

    /**
     * Get all meta information (name, type, parent).
     *
     * @return meta information object
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * Get all options (required, defaultValue).
     *
     * @return options object
     */
    public Options<T> getOptions() {
        return options;
    }

    /**
     * Set the name (meta information) of this parsable object.
     *
     * @param name new name
     * @return reference to this parsable object.
     */
    public final Parsable<T, C> name(String name) {
        this.meta.name = name;
        return this;
    }

    /**
     * Mark this parsable object as required.
     *
     * @return reference to this parsable object.
     */
    public final Parsable<T, C> required() {
        return this.required(true);
    }

    /**
     * Mark this parsable object as required -- but only if {@code condition} is {@code true}.
     * <b>Note:</b> Providing {@code false} is <i>not</i> equivalent to providing {@code true} to
     * {@link #optional(boolean)}; it just means that this call is basically ineffective.
     *
     * @return reference to this parsable object.
     */
    public final Parsable<T, C> required(boolean condition) {
        if (condition) {
            this.options.required = true;
        }
        return this;
    }

    /**
     * Mark this parsable object as optional.
     *
     * @return reference to this parsable object.
     */
    public final Parsable<T, C> optional() {
        return this.optional(true);
    }

    /**
     * Mark this parsable object as optional -- but only if {@code condition} is {@code true}.
     * <b>Note:</b> Providing {@code false} is <i>not</i> equivalent to providing {@code true} to
     * {@link #required(boolean)}; it just means that this call is basically ineffective.
     *
     * @return reference to this parsable object.
     */
    public final Parsable<T, C> optional(boolean condition) {
        if (condition) {
            this.options.required = false;
        }
        return this;
    }

    /**
     * Use the provided default value if no input has been provided.
     *
     * @param defaultValue the default value
     * @return reference to this parsable object.
     */
    public final Parsable<T, C> withDefault(T defaultValue) {
        this.options.defaultValue = Optional.<T>ofNullable(defaultValue);
        return this;
    }

    /**
     * Wrap a complex object so that it can be marked as required/optional and more.
     *
     * @param fieldName name of the attribute in the calling object
     * @param valueType the value type
     * @param parsableType the parsable type
     * @param <T> the value type
     * @param <S> the parsable type
     * @return wrapper object
     * @throws ParsableInvalidSpecification
     */
    public <T, S extends Parsable<T, C>, C> ParsableWrapper<T, S, C> wrap(
            String fieldName, Class<T> valueType, Class<S> parsableType)
            throws ParsableInvalidSpecification {
        return new ParsableWrapper<T, S, C>(this, getField(fieldName));
    }

    /**
     * Transform a list of <tt>Parsable</tt> entries into a list of the corresponding values.
     *
     * @param fieldName name of the attribute in the calling object
     * @param <T> the value type
     * @param <S> the parsable type
     * @return list of values resulting from the parsable items
     */
    public <T, S extends Parsable<T, C>> List<T> parseList(String fieldName) {
        Field field = getField(fieldName);
        return ParsableHelper.<T, S, C>parseList(
                this, field, getFieldObject(field), Optional.<C>empty());
    }

    public <T, S extends Parsable<T, C>> List<T> parseList(String fieldName, Optional<C> context) {
        Field field = getField(fieldName);
        return ParsableHelper.<T, S, C>parseList(this, field, getFieldObject(field), context);
    }

    public Field getField(String fieldName) throws ParsableInvalidSpecification {
        Objects.requireNonNull(fieldName);
        try {
            return this.getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new ParsableInvalidSpecification(
                    this, "Field '" + fieldName + "' does not exist", e);
        }
    }

    public Object getFieldObject(Field field) throws ParsableInvalidSpecification {
        try {
            return field.get(this);
        } catch (IllegalAccessException e) {
            throw new ParsableInvalidSpecification("Cannot access field '" + field.getName() + "'");
        }
    }

    public void forEveryAccessibleField(BiConsumer<Field, Object> func) {
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                func.accept(field, field.get(this));
            } catch (IllegalAccessException e) {
                /* ignore this field */
            }
        }
    }

    /**
     * Get value based on input and options.
     *
     * @return the value
     */
    public T value() {
        return value(Optional.<C>empty());
    }

    public T value(Optional<C> context) {
        getMeta().setUp();
        return determineValue(context);
    }

    protected T determineValue(Optional<C> context) {
        return parse(context);
    }

    protected abstract T parse(Optional<C> context);

    protected ExplainedResult<Boolean, String> isValid(T value) {
        return new ExplainedResult<Boolean, String>(true);
    }

    private void setUpIfRootElement() {
        XmlRootElement ann = getClass().getAnnotation(XmlRootElement.class);
        if (ann != null) {
            getMeta().setType("root element");
            String name = ann.name();
            if (name == null) name = getClass().getName();
            getMeta().setName(name);
        }
    }

    public class Meta {
        public String type = null;
        public String name = null;
        public Parsable<?, ?> parent = null;

        public void setName(String newName) {
            if (name == null) name = newName;
        }

        public void setType(String newType) {
            if (name == null) type = newType;
        }

        public void setParent(Parsable<?, ?> newParent) {
            if (parent == null) parent = newParent;
        }

        public boolean isElement() {
            return type != null && type.equals("element");
        }

        public boolean isRootElement() {
            return type != null && type.equals("root element");
        }

        public boolean isValue() {
            return type != null && type.equals("value");
        }

        public String toString() {
            return typeToString() + " '" + nameToString() + "'";
        }

        public String typeToString() {
            return (type == null) ? "Unknown object" : type;
        }

        public String nameToString() {
            return (name == null) ? "<unnamed>" : name;
        }

        public void setUp() {
            setUp(Parsable.this);
        }

        public void setUp(Parsable<?, ?> alternativeParent) {
            ParsableHelper.nameAllPublicFieldsIn(Parsable.this);
            ParsableHelper.registerParentForAllPublicFields(Parsable.this, alternativeParent);
        }
    }

    public static class Options<T> {
        public boolean required = false;
        public Optional<T> defaultValue = Optional.<T>empty();

        public String toString() {
            if (required) return "required";
            String detailsDefault =
                    (defaultValue.isPresent()) ? "" : " with default value '" + defaultValue + "'";
            return "optional" + detailsDefault;
        }
    }
}
