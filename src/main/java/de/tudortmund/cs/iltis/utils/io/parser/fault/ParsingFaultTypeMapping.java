package de.tudortmund.cs.iltis.utils.io.parser.fault;

import de.tudortmund.cs.iltis.utils.collections.FaultCollection;
import de.tudortmund.cs.iltis.utils.collections.TypeMapping;
import java.util.Map;

/**
 * A TypeMapping for fault collections which additionally contains an input field as string and a
 * output field (with the generic type).
 *
 * @param <OutputT> the type of the output field
 */
public class ParsingFaultTypeMapping<OutputT> extends TypeMapping<FaultCollection<?, ?>> {

    /** may be null */
    private String input;

    /** may be null */
    private OutputT output;

    /**
     * Creates a new object.
     *
     * @param input may be null
     * @param output may be null
     * @param objects may not be null
     * @throws NullPointerException if {@code objects} is null
     */
    public ParsingFaultTypeMapping(
            String input, OutputT output, Map<String, FaultCollection<?, ?>> objects) {
        super(objects);
        this.input = input;
        this.output = output;
    }

    /**
     * Creates a new object.
     *
     * @param input may be null
     * @param output may be null
     */
    public ParsingFaultTypeMapping(String input, OutputT output) {
        super();
        this.input = input;
        this.output = output;
    }

    /** Returns the input, which may be null */
    public String getInput() {
        return input;
    }

    /**
     * Returns the output. Independently of the presence of faults, the output may be null. While
     * for some faults the input may be repaired to produce a output, for other faults this may be
     * not possible.
     */
    public OutputT getOutput() {
        return output;
    }

    /** {@inheritDoc} */
    @Override
    public ParsingFaultTypeMapping<OutputT> with(FaultCollection<?, ?> o) {
        return (ParsingFaultTypeMapping<OutputT>) super.with(o);
    }

    /** {@inheritDoc} */
    @Override
    public ParsingFaultTypeMapping<OutputT> with(
            TypeMapping<? extends FaultCollection<?, ?>> mapping) {
        return (ParsingFaultTypeMapping<OutputT>) super.with(mapping);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((output == null) ? 0 : output.hashCode());
        result = prime * result + ((input == null) ? 0 : input.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        ParsingFaultTypeMapping<?> other = (ParsingFaultTypeMapping<?>) obj;
        if (output == null) {
            if (other.output != null) return false;
        } else if (!output.equals(other.output)) return false;
        if (input == null) {
            return other.input == null;
        } else return input.equals(other.input);
    }

    /**
     * Creates a shallow copy of this object.
     *
     * <p><b>Note:</b> the output is not cloned.
     *
     * @return a shallow copy of this object
     */
    @Override
    public ParsingFaultTypeMapping<OutputT> clone() {
        return new ParsingFaultTypeMapping<>(input, output, objects);
    }

    @Override
    public String toString() {
        return "ParsingFaultTypeMapping [input = "
                + input
                + ", output = "
                + output
                + ", objects = "
                + objects
                + "]";
    }

    /** For serialization */
    private static final long serialVersionUID = 1L;

    /** For serialization */
    @SuppressWarnings("unused")
    private ParsingFaultTypeMapping() {}
}
