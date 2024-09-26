package de.tudortmund.cs.iltis.utils.general.viewcopier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * Create a flat copy of an object of some type {@code Type}. But copy only those fields whose
 * "label" matches the "selector". The copying process is determined by the "copy mode" of each
 * matching field.
 *
 * <p><b>Notes:</b> The class to be copied needs to have a default constructor.
 *
 * <p>As an example, consider a class like the following.
 *
 * <pre>{@code
 * public class InternalDatum {
 *     @ViewCopy(label="student")
 *     private String question;
 *
 *     @ViewCopy(label="student", mode=ViewCopier.CopyMode.CLONE)
 *     private ArrayList<String> answers;
 *
 *     private int correctAnswer;
 *
 *     // constructors and methods
 * }
 * }</pre>
 *
 * For an object {@code d} of type {@code InternalDatum}, the call {@code ViewCopier.copy(d,
 * "student")} creates a new instance of {@code InternalDatum} where the field {@code question}
 * points to the same reference as in {@code d}, while the field {@code answers} points to a new
 * list (with the same entries as in {@code d}). Lastly, the field {@code correctAnswer} in the copy
 * has the default value 0, regardless of the value of {@code d.correctAnswer}.
 */
public class ViewCopier {
    /** Describes how fields are copied. */
    public enum CopyMode {
        /** Simply copy the reference. */
        REFERENCE,

        /**
         * Call the clone method of the object. Requires that the type implements {@code Cloneable}.
         */
        CLONE
    };

    /**
     * Returns an view copy of all {@code filter} attributes in {@code input}. If {@code input} is
     * {@code null}, then the view copy is {@code null} too. Otherwise, only those fields are copied
     * that have a {@code ViewCopy} annotation and, if {@code selector} is not {@code null}, then
     * the field also has to have a label equal to {@code selector}.
     *
     * @param input the object whose fields are to be copied
     * @param selector the selector
     * @param <Type> the type of the input and copy object
     * @return the view copy
     */
    public static <Type> Type copy(@Nullable Type input, @Nullable String selector) {
        if (input == null) return null;

        @SuppressWarnings("unchecked")
        Class<Type> clazz = (Class<Type>) input.getClass();
        Constructor<Type> ctor = ViewCopier.<Type>getDefaultConstructor(clazz);
        ctor.setAccessible(true);
        Type copy = ViewCopier.<Type>newDefaultInstance(ctor);
        ViewCopier.<Type>copyFields(clazz, input, copy, selector);
        return copy;
    }

    /**
     * Returns an view copy of all {@code filter} attributes in {@code input}. If {@code input} is
     * {@code null}, then the view copy is {@code null} too. Otherwise, only those fields are copied
     * that have a {@code ViewCopy} annotation, regardless of their labels.
     *
     * @param input the object whose fields are to be copied
     * @param <Type> the type of the input and copy object
     * @return the view copy
     */
    public static <Type> Type copy(@Nullable Type input) {
        return ViewCopier.<Type>copy(input, null);
    }

    /**
     * Returns the default constructor of class {@code clazz} if available and throws an {@code
     * IllegalArgumentException} otherwise.
     */
    private static <Type> Constructor<Type> getDefaultConstructor(@NotNull Class<Type> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Required default constructor is missing.", e);
        }
    }

    /**
     * Returns a new instance from constructor without parameters if accessible. Otherwise, a {@code
     * IllegalArgumentException} is thrown.
     */
    private static <Type> Type newDefaultInstance(@NotNull Constructor<Type> ctor) {
        try {
            return ctor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot invoke default constructor.", e);
        }
    }

    /**
     * Calls method {@code copyField()} for every field in the class and passes the field as well as
     * {@code input}, {@code copy} and {@code selector}.
     */
    private static <Type> void copyFields(
            @NotNull Class<Type> clazz,
            @NotNull Type input,
            @NotNull Type copy,
            @Nullable String selector) {
        for (Field field : clazz.getDeclaredFields()) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            ViewCopier.<Type>copyField(field, input, copy, selector);
            field.setAccessible(accessible);
        }
    }

    /**
     * Processes every {@code ViewCopy} annotation for the {@code field} and passes them to {@code
     * processViewCopyAnnotation()}.
     */
    private static <Type> void copyField(
            @NotNull Field field,
            @NotNull Type input,
            @NotNull Type copy,
            @Nullable String selector) {
        ViewCopy[] annotations = field.getAnnotationsByType(ViewCopy.class);
        for (ViewCopy a : annotations) {
            ViewCopier.<Type>processViewCopyAnnotation(a, field, input, copy, selector);
        }
    }

    /** Process a single view copy annotation: if the label matches, then copy. */
    private static <Type> void processViewCopyAnnotation(
            @NotNull ViewCopy annotation,
            @NotNull Field field,
            @NotNull Type input,
            @NotNull Type copy,
            @Nullable String selector) {
        if (!matchesLabel(annotation.labels(), selector)) {
            return;
        }
        Object fieldObj = ViewCopier.<Type>getFieldObject(field, input);
        ViewCopier.<Type>copyFieldObject(field, fieldObj, copy, annotation.mode());
    }

    /** Read the field in the {@code input} object if accessible. Otherwise throw an exception. */
    private static <Type> Object getFieldObject(@NotNull Field field, @NotNull Type input) {
        try {
            return field.get(input);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot read field.", e);
        }
    }

    /**
     * Determine a copy based on the copy mode {@code mode} and the field object {@code fieldObj}.
     * Set the field in the {@code copy} object if accessible. Otherwise throw an exception.
     */
    private static <Type> void copyFieldObject(
            @NotNull Field field, Object fieldObj, @NotNull Type copy, CopyMode mode) {
        try {
            switch (mode) {
                case REFERENCE:
                    copyFieldReference(field, fieldObj, copy);
                    break;
                case CLONE:
                    copyFieldClone(field, fieldObj, copy);
                    break;
                default:
                    throw new UnsupportedOperationException(
                            "Copy mode '" + mode + "' is not implemented.");
            }
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot write field.", e);
        }
    }

    /**
     * Let the {@code field} in {@code copy} point to {@code fieldObj}, that is, copy only the
     * reference.
     *
     * @throws IllegalAccessException
     */
    private static <Type> void copyFieldReference(
            @NotNull Field field, Object fieldObj, @NotNull Type copy)
            throws IllegalAccessException {
        field.set(copy, fieldObj);
    }

    /** Clone the {@code fieldObj} and let {@code field} in {@code copy} point to this clone. */
    private static <Type> void copyFieldClone(
            @NotNull Field field, Object fieldObj, @NotNull Type copy)
            throws IllegalArgumentException, IllegalAccessException {
        if (!(fieldObj instanceof Cloneable)) {
            throw new IllegalArgumentException("Field '" + field.getName() + "' is not clonable.");
        }

        Object clonedObject = null;
        clonedObject = CloneHelper.clone(fieldObj);
        field.set(copy, clonedObject);
    }

    /** Determine whether {@code label} matches {@code selector}. */
    private static boolean matchesLabel(String[] labels, @Nullable String selector) {
        return selector == null || Arrays.<String>asList(labels).contains(selector);
    }
}
