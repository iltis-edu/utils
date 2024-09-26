package de.tudortmund.cs.iltis.utils.io.parsable;

import de.tudortmund.cs.iltis.utils.explainedresult.ExplainedResult;
import de.tudortmund.cs.iltis.utils.io.xml.XmlTextWriter;
import java.util.Optional;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3c.dom.Element;

/** Provides validation checks and cleansing tasks for string values. */
@XmlJavaTypeAdapter(ParsableString.Adapter.class)
public class ParsableString extends ParsableEntry<String> {
    private boolean allow_empty = true;
    private final LengthConditions lengthConditions = new LengthConditions();

    /** Create a parsable string with a {@code null} reference. */
    public ParsableString() {
        super();
    }

    /**
     * Create a parsable string with the provided {@code input}.
     *
     * @param input the input string for this {@code ParsableString} instance
     */
    public ParsableString(String input) {
        super(input);
    }

    /**
     * Mark the string as required and to be different from the empty string.
     *
     * @return this {@code ParsableString} instance
     */
    public ParsableString nonempty() {
        return this.nonempty(true);
    }

    /**
     * Mark the string as required and to be different from the empty string -- but only if {@code
     * condition} is {@code true}.
     *
     * @return this {@code ParsableString} instance
     */
    public ParsableString nonempty(boolean condition) {
        if (condition) {
            super.required();
            allow_empty = false;
        }
        return this;
    }

    /**
     * Require the input to be provided and to comprise at least {@code bound} characters.
     *
     * @param bound minimal number of characters
     * @return this {@code ParsableString} instance
     */
    public ParsableString lengthAtLeast(int bound) {
        required();
        lengthConditions.setLengthAtLeast(bound);
        return this;
    }

    /**
     * Require the input to be provided and to comprise at most {@code bound} characters.
     *
     * @param bound maximal number of characters
     * @return this {@code ParsableString} instance
     */
    public ParsableString lengthAtMost(int bound) {
        lengthConditions.setLengthAtMost(bound);
        return this;
    }

    /**
     * Require the input to be provided and to comprise at least {@code atLeast} and at most {@code
     * atMost} characters.
     *
     * @param atLeast minimal number of characters
     * @param atMost maximal number of characters
     * @return this {@code ParsableString} instance
     */
    public ParsableString length(int atLeast, int atMost) {
        return lengthAtLeast(atLeast).lengthAtMost(atMost);
    }

    /**
     * Require the input to be provided and to comprise exactly {@code length} characters.
     *
     * @param length exact number of characters
     * @return this {@code ParsableString} instance
     */
    public ParsableString length(int length) {
        return length(length, length);
    }

    @Override
    protected ExplainedResult<Boolean, String> isValid(String value) {
        ExplainedResult<Boolean, String> eresult = null;

        eresult = checkEmpty(value);
        if (eresult != null) return eresult;

        eresult = checkLength(value);
        if (eresult != null) return eresult;

        return new ExplainedResult<>(true);
    }

    private ExplainedResult<Boolean, String> checkEmpty(String input) {
        if (!allow_empty) {
            if (input == null) return new ExplainedResult<>(false, "Unspecified value");
            else if (input.isEmpty()) return new ExplainedResult<>(false, "Empty string");
        }
        return null;
    }

    private ExplainedResult<Boolean, String> checkLength(String input) {
        if (!lengthConditions.check(input)) {
            return new ExplainedResult<>(
                    false,
                    "Input "
                            + input
                            + " "
                            + "doesn't match length restrictions "
                            + lengthConditions);
        }
        return null;
    }

    /**
     * Checks validity of the input. If the input is valid, the result after the configured
     * cleansing tasks is returned. If the input is missing although required or if the input is
     * invalid, a parsable exception is thrown.
     *
     * @param context is unused
     * @return the parse result
     */
    @Override
    protected String parse(Optional<Object> context) {
        String result = getSource();
        return result;
    }

    private static class LengthConditions {
        private Optional<Integer> atLeast = Optional.empty();
        private Optional<Integer> atMost = Optional.empty();

        public void setLengthAtLeast(int bound) {
            atLeast = Optional.<Integer>of(bound);
        }

        public void setLengthAtMost(int bound) {
            atMost = Optional.<Integer>of(bound);
        }

        public boolean check(String input) {
            Integer length = input == null ? null : input.length();
            if (atLeast.isPresent()) {
                if (length == null || length < atLeast.get()) {
                    return false;
                }
            }
            if (atMost.isPresent()) {
                if (length == null || length > atMost.get()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return "["
                    + atLeast.orElseGet(() -> 0)
                    + ","
                    + atMost.orElseGet(() -> Integer.MAX_VALUE)
                    + "]";
        }
    }

    public static class Adapter extends XmlAdapter<String, ParsableString> {
        public ParsableString unmarshal(String input) {
            return new ParsableString(input);
        }

        public String marshal(ParsableString entry) throws RuntimeException {
            throw new UnsupportedOperationException();
        }
    }

    public static class TextContentAdapter extends XmlAdapter<Object, ParsableString> {
        public ParsableString unmarshal(Object input) {
            Element e = (Element) input;
            return new ParsableString(e.getTextContent());
        }

        public Object marshal(ParsableString entry) throws RuntimeException {
            throw new UnsupportedOperationException();
        }
    }

    public static class XmlAsTextAdapter extends XmlAdapter<Object, ParsableString> {
        public ParsableString unmarshal(Object input) throws Exception {
            Element e = (Element) input;
            final int tagsize = e.getTagName().length() + 2;
            String xmlAsText = XmlTextWriter.toCompactText(e);
            xmlAsText = xmlAsText.substring(tagsize, xmlAsText.length() - tagsize - 1);
            return new ParsableString(xmlAsText);
        }

        public Object marshal(ParsableString entry) throws RuntimeException {
            throw new UnsupportedOperationException();
        }
    }
}
