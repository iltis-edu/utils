package de.tudortmund.cs.iltis.utils.io.parsable;

import de.tudortmund.cs.iltis.utils.explainedresult.ExplainedResult;
import java.util.Optional;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/** Provides validation checks for double values. */
@XmlJavaTypeAdapter(ParsableDouble.Adapter.class)
public class ParsableDouble extends ParsableEntry<Double> {
    private final RangeCondition rangeCondition = new RangeCondition();

    { // for each created object:
        trim();
    }

    /** Create a parsable double with a {@code null} reference. */
    public ParsableDouble() {
        super();
    }

    /**
     * Create a parsable double with the provided {@code input}.
     *
     * @param input the input string to parse to a {@code Double}
     */
    public ParsableDouble(String input) {
        super(input);
    }

    @XmlValue
    public String getSource() {
        return super.getSource();
    }

    public void setSource(String source) {
        super.setSource(source);
    }

    public ParsableDouble atLeast(double value) {
        rangeCondition.setMinValue(value);
        return this;
    }

    public ParsableDouble atMost(double value) {
        rangeCondition.setMaxValue(value);
        return this;
    }

    public ParsableDouble between(double atLeast, double atMost) {
        return this.atLeast(atLeast).atMost(atMost);
    }

    @Override
    protected ExplainedResult<Boolean, String> isValid(Double value) {
        if (!rangeCondition.satisfiesLowerBound(value))
            return new ExplainedResult<>(false, "Value is < " + rangeCondition.minValue);
        if (!rangeCondition.satisfiesUpperBound(value))
            return new ExplainedResult<>(false, "Value is > " + rangeCondition.maxValue);
        return new ExplainedResult<>(true);
    }

    @Override
    protected Double parse(Optional<Object> context) {
        Double v;
        try {
            v = Double.parseDouble(this.getSource());
        } catch (NumberFormatException e) {
            throw new ParsableInvalidValue(
                    this, "Cannot parse '" + this.getSource() + "' as an double", e);
        }
        return v;
    }

    private static class RangeCondition {
        private Optional<Double> minValue = Optional.empty();
        private Optional<Double> maxValue = Optional.empty();

        public void setMinValue(double minValue) {
            this.minValue = Optional.<Double>of(minValue);
        }

        public void setMaxValue(double maxValue) {
            this.maxValue = Optional.<Double>of(maxValue);
        }

        public boolean satisfiesLowerBound(Double value) {
            return !minValue.isPresent() || (value != null && value >= minValue.get());
        }

        public boolean satisfiesUpperBound(Double value) {
            return !maxValue.isPresent() || (value != null && value <= maxValue.get());
        }
    }

    public static class Adapter extends XmlAdapter<String, ParsableDouble> {
        public ParsableDouble unmarshal(String input) {
            return new ParsableDouble(input);
        }

        public String marshal(ParsableDouble entry) throws RuntimeException {
            throw new UnsupportedOperationException();
        }
    }
}
