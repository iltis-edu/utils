package de.tudortmund.cs.iltis.utils.io.parsable;

import de.tudortmund.cs.iltis.utils.collections.immutable.ImmutableSet;
import java.util.Collection;
import java.util.Optional;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/** Provides validation checks for integer values with infinity option. */
@XmlJavaTypeAdapter(ParsableIntegerOrInfinity.Adapter.class)
public class ParsableIntegerOrInfinity extends ParsableInteger {
    private Collection<String> infinityKeywords;
    private static final ImmutableSet<String> defaultInfinityKeywords =
            new ImmutableSet<>("infinity", "infinite", "inf", "Inf");

    /** Create a parsable integer with infinity option with a {@code null} reference. */
    public ParsableIntegerOrInfinity() {
        super();
        infinityKeywords = defaultInfinityKeywords.toUnmodifiableSet();
    }

    /**
     * Create a parsable integer with infinity option with specified {@code keywords} and a {@code
     * null} reference.
     *
     * @param keywords {@code Collection} of infinity keywords that can be used in specification
     */
    public ParsableIntegerOrInfinity(Collection<String> keywords) {
        super();
        infinityKeywords = keywords;
    }

    /**
     * Create a parsable integer with infinity option with the provided {@code input}.
     *
     * @param input the input string to parse to an {@code Integer} with infinity option
     */
    public ParsableIntegerOrInfinity(String input) {
        super(input);
        infinityKeywords = defaultInfinityKeywords.toUnmodifiableSet();
    }

    /**
     * Create a parsable integer with infinity option with specified {@code keywords} and the
     * provided {@code input}.
     *
     * @param input the input string to parse to an {@code Integer} with infinity option
     * @param keywords {@code Collection} of infinity keywords that can be used in specification
     */
    public ParsableIntegerOrInfinity(String input, Collection<String> keywords) {
        super(input);
        infinityKeywords = keywords;
    }

    @Override
    protected Integer parse(Optional<Object> context) {
        int v;
        try {
            v = Integer.parseInt(this.getSource());
        } catch (NumberFormatException e) {
            if (infinityKeywords.contains(this.getSource())) {
                return Integer.MAX_VALUE;
            } else {
                throw new ParsableInvalidValue(
                        this,
                        "Cannot parse '"
                                + this.getSource()
                                + "' as an integer or infinity with the following"
                                + " infinity keywords: "
                                + infinityKeywords.toString(),
                        e);
            }
        }
        return v;
    }

    public static class Adapter extends XmlAdapter<String, ParsableIntegerOrInfinity> {
        public ParsableIntegerOrInfinity unmarshal(String input) {
            return new ParsableIntegerOrInfinity(input);
        }

        public String marshal(ParsableIntegerOrInfinity entry) throws RuntimeException {
            throw new UnsupportedOperationException();
        }
    }
}
