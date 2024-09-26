package de.tudortmund.cs.iltis.utils.io.parsable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(ParsableBoolean.Adapter.class)
public class ParsableBoolean extends ParsableEntry<Boolean> {
    private static final Map<String, Boolean> inputsTrueAndFalse = mapBools("true", "false");
    private static final Map<String, Boolean> inputsYesAndNo = mapBools("yes", "no");
    private static final Map<String, Boolean> inputsOneAndZero = mapBools("1", "0");
    private static final Map<String, Boolean> inputsOnAndOff = mapBools("on", "off");

    private Map<String, Boolean> inputs = new HashMap<>();

    { // for each created object:
        addMap(inputs, inputsTrueAndFalse);
        trim();
        lowerCase();
    }

    public ParsableBoolean() {
        super();
    }

    public ParsableBoolean(String input) {
        super(input);
    }

    @XmlValue
    public String getSource() {
        return super.getSource();
    }

    public void setSource(String source) {
        super.setSource(source);
    }

    public ParsableBoolean allowInputsYesAndNo() {
        addMap(inputs, inputsYesAndNo);
        return this;
    }

    public ParsableBoolean allowInputsOneAndZero() {
        addMap(inputs, inputsOneAndZero);
        return this;
    }

    public ParsableBoolean allowInputsOnAndOff() {
        addMap(inputs, inputsOnAndOff);
        return this;
    }

    public ParsableBoolean allowInputs(Map<String, Boolean> inputs) {
        addMap(this.inputs, inputs);
        return this;
    }

    public ParsableBoolean allowInputs(String inputTrue, String inputFalse) {
        return this.allowInputs(mapBools(inputTrue, inputFalse));
    }

    public ParsableBoolean allowInputsGenerously() {
        return this.allowInputsYesAndNo().allowInputsOneAndZero().allowInputsOnAndOff();
    }

    public ParsableBoolean disallowAllInputs() {
        this.inputs.clear();
        return this;
    }

    @Override
    protected Boolean parse(Optional<Object> context) {
        final String key = this.getSource();
        if (this.inputs.containsKey(key)) {
            return this.inputs.get(key);
        }
        throw new ParsableInvalidValue(this, "Cannot parse '" + key + "' as a Boolean value.");
    }

    public static class Adapter extends XmlAdapter<String, ParsableBoolean> {
        public ParsableBoolean unmarshal(String input) {
            return new ParsableBoolean(input);
        }

        public String marshal(ParsableBoolean entry) throws RuntimeException {
            throw new UnsupportedOperationException();
        }
    }

    private static Map<String, Boolean> mapBools(String nameTrue, String nameFalse) {
        Map<String, Boolean> inputs = new HashMap<>();
        inputs.put(nameTrue, true);
        inputs.put(nameFalse, false);
        return inputs;
    }

    private static void addMap(Map<String, Boolean> map, Map<String, Boolean> otherMap) {
        for (Map.Entry<String, Boolean> e : otherMap.entrySet()) {
            map.put(e.getKey(), e.getValue());
        }
    }
}
