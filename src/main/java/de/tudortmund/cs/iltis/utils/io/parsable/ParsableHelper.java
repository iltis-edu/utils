package de.tudortmund.cs.iltis.utils.io.parsable;

import de.tudortmund.cs.iltis.utils.collections.Pair;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;

class ParsableHelper {
    private static final String DEFAULT_NAME = "##default";

    public static <T, S extends Parsable<T, C>, C> List<T> parseList(
            Parsable<?, C> parent, Field field, Object fieldObj) {
        return parseList(parent, field, fieldObj, Optional.<C>empty());
    }

    public static <T, S extends Parsable<T, C>, C> List<T> parseList(
            Parsable<?, C> parent, Field field, Object fieldObj, Optional<C> context)
            throws ParsableInvalidSpecification {
        if (fieldObj == null) return Collections.<T>emptyList();

        List<S> parsableList = null;
        if (fieldObj instanceof List) parsableList = (List<S>) fieldObj;
        else
            throw new ParsableInvalidSpecification("Field '" + field.getName() + "' is not a list");

        return parsableList.stream()
                .peek(parsable -> parsable.getMeta().parent = parent)
                .map(parsable -> parsable.value(context))
                .collect(Collectors.toList());
    }

    public static void registerParentForAllPublicFields(
            Parsable<?, ?> parsable, Parsable<?, ?> parent) {
        try {
            parsable.forEveryAccessibleField((field, obj) -> registerParentInField(parent, obj));
        } catch (Exception e) {
            throw new RuntimeException("Cannot register parent for accessible fields.", e);
        }
    }

    protected static void registerParentInField(Parsable<?, ?> parent, Object obj) {
        if (obj instanceof Parsable) {
            Parsable<?, ?> p = (Parsable<?, ?>) obj;
            p.getMeta().parent = parent;
        } else if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            list.forEach(
                    item -> {
                        if (item instanceof Parsable) {
                            Parsable<?, ?> pitem = (Parsable<?, ?>) item;
                            pitem.getMeta().parent = parent;
                        }
                    });
        }
    }

    public static void nameAllPublicFieldsIn(Parsable<?, ?> parsable) {
        try {
            parsable.forEveryAccessibleField(ParsableHelper::nameField);
        } catch (ParsableInvalidSpecification e) {
            throw e;
        } catch (Exception e) {
            throw new ParsableInvalidSpecification(
                    "Cannot register parent for accessible fields", e);
        }
    }

    protected static void nameField(Field field, Object obj) {
        if (obj instanceof Parsable) {
            Parsable<?, ?> parsable = (Parsable<?, ?>) obj;
            ParsableHelper.nameParsableField(field, parsable);
        } else if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            list.forEach(
                    item -> {
                        if (item instanceof Parsable) {
                            Parsable<?, ?> pitem = (Parsable<?, ?>) item;
                            ParsableHelper.nameParsableField(field, pitem);
                        }
                    });
        }
    }

    private static void nameParsableField(Field field, Parsable<?, ?> parsable) {
        if (parsable == null) return;

        try {
            Pair<String, String> nameAndType = getNameAndType(parsable, field);
            parsable.getMeta().setName(nameAndType.first());
            parsable.getMeta().setType(nameAndType.second());
        } catch (ParsableInvalidSpecification e) {
            e.setCodeLocationDetails(
                    "Field '"
                            + field.getName()
                            + "' in class "
                            + parsable.getClass().getCanonicalName());
            throw e;
        }
    }

    public static Pair<String, String> getNameAndType(Parsable<?, ?> parsable, Field field) {
        return Stream.of(
                        getNameAndSetTypeIfXmlType(
                                parsable, "attribute", field.getAnnotation(XmlAttribute.class)),
                        getNameAndSetTypeIfXmlType(
                                parsable, "element", field.getAnnotation(XmlElement.class)),
                        getNameAndSetTypeIfXmlType(
                                parsable, "value", field.getAnnotation(XmlValue.class)))
                .filter(nameAndType -> nameAndType.first() != null)
                .findFirst()
                .orElseGet(() -> new Pair<String, String>(field.getName(), "Unknown type"));
    }

    // TODO: Replace type names by classes
    private static <A> Pair<String, String> getNameAndSetTypeIfXmlType(
            Parsable<?, ?> parsable, String xmlTypeName, A annotation) {
        String name = null;
        if (annotation != null) {
            if (parsable != null) {
                parsable.getMeta().type = xmlTypeName;
            }

            if (annotation instanceof XmlAttribute) {
                XmlAttribute att = (XmlAttribute) annotation;
                name = att.name();
                if (att.required()) {
                    throw new ParsableInvalidSpecification(
                            parsable,
                            messageForRequiredInAnnotation("Attribute", name, "@XmlAttribute"));
                }
            } else if (annotation instanceof XmlValue) {
                // nothing to do for values
            } else /* if (annotation instanceof XmlElement) */ {
                // elements are used if neither XmlAttribute nor XmlValue is used
                XmlElement elem = (XmlElement) annotation;
                name = elem.name();
                if (elem.required()) {
                    throw new ParsableInvalidSpecification(
                            parsable,
                            messageForRequiredInAnnotation("Element", name, "@XmlElement"));
                }
            }

            if (name != null && name.equals(DEFAULT_NAME)) {
                name = null;
            }
        }
        return new Pair<>(name, xmlTypeName);
    }

    private static String messageForRequiredInAnnotation(
            String type, String name, String annotationType) {
        return type
                + " "
                + ((name.equals(DEFAULT_NAME)) ? "" : (" '" + name + "'"))
                + "is marked as required in annotation "
                + annotationType
                + "(required=true).\n"
                + "This is disallowed with Parsable, use Parsable.required() instead!";
    }
}
