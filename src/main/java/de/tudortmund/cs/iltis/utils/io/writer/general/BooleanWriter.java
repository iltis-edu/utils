package de.tudortmund.cs.iltis.utils.io.writer.general;

public class BooleanWriter implements Writer<Boolean> {
    private String trueText;
    private String falseText;

    public BooleanWriter() {
        this("1", "0");
    }

    public BooleanWriter(String trueText, String falseText) {
        this.trueText = trueText;
        this.falseText = falseText;
    }

    public String write(Boolean bool) {
        return bool ? this.trueText : this.falseText;
    }
}
