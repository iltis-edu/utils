package de.tudortmund.cs.iltis.utils.io.parsable;

import de.tudortmund.cs.iltis.utils.explainedresult.ExplainedResult;
import java.util.Optional;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/** Provides validation checks for integer values. */
@XmlJavaTypeAdapter(ParsableInteger.Adapter.class)
public class ParsableInteger extends ParsableEntry<Integer> {
    private final RangeCondition rangeCondition = new RangeCondition();

    { // for every created object:
        trim();
    }

    /** Create a parsable integer with a {@code null} reference. */
    public ParsableInteger() {
        super();
    }

    /**
     * Create a parsable integer with the provided {@code input}.
     *
     * @param input the input string to parse to an {@code Integer}
     */
    public ParsableInteger(String input) {
        super(input);
    }

    @XmlValue
    public String getSource() {
        return super.getSource();
    }

    public void setSource(String source) {
        super.setSource(source);
    }

    public ParsableInteger atLeast(int value) {
        rangeCondition.setMinValue(value);
        return this;
    }

    public ParsableInteger atMost(int value) {
        rangeCondition.setMaxValue(value);
        return this;
    }

    public ParsableInteger between(int atLeast, int atMost) {
        return this.atLeast(atLeast).atMost(atMost);
    }

    @Override
    protected ExplainedResult<Boolean, String> isValid(Integer value) {
        if (!rangeCondition.satisfiesLowerBound(value))
            return new ExplainedResult<>(false, "Value is < " + rangeCondition.minValue);
        if (!rangeCondition.satisfiesUpperBound(value))
            return new ExplainedResult<>(false, "Value is > " + rangeCondition.maxValue);
        return new ExplainedResult<>(true);
    }

    @Override
    protected Integer parse(Optional<Object> context) {
        int v;
        try {
            v = Integer.parseInt(this.getSource());
        } catch (NumberFormatException e) {
            throw new ParsableInvalidValue(
                    this, "Cannot parse '" + this.getSource() + "' as an integer", e);
        }
        return v;
    }

    private static class RangeCondition {
        private Optional<Integer> minValue = Optional.empty();
        private Optional<Integer> maxValue = Optional.empty();

        public void setMinValue(int minValue) {
            this.minValue = Optional.<Integer>of(minValue);
        }

        public void setMaxValue(int maxValue) {
            this.maxValue = Optional.<Integer>of(maxValue);
        }

        public boolean satisfiesLowerBound(Integer value) {
            return !minValue.isPresent() || (value != null && value >= minValue.get());
        }

        public boolean satisfiesUpperBound(Integer value) {
            return !maxValue.isPresent() || (value != null && value <= maxValue.get());
        }
    }

    public static class Adapter extends XmlAdapter<String, ParsableInteger> {
        public ParsableInteger unmarshal(String input) {
            return new ParsableInteger(input);
        }

        public String marshal(ParsableInteger entry) throws RuntimeException {
            throw new UnsupportedOperationException();
        }
    }
}
