package de.tudortmund.cs.iltis.utils.io.writer.collections;

import de.tudortmund.cs.iltis.utils.io.writer.general.DefaultWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;
import java.util.Set;

public class SetWriter<T> implements Writer<Set<T>> {
    private String leftBrace;
    private String rightBrace;
    private IterableWriter<T> iterableWriter;

    public SetWriter() {
        this(new DefaultWriter<T>());
    }

    public SetWriter(Writer<T> elementWriter) {
        this.leftBrace = "{";
        this.rightBrace = "}";
        this.iterableWriter = new IterableWriter<T>(elementWriter);
    }

    public void setLeftBrace(String leftBrace) {
        this.leftBrace = leftBrace;
    }

    public void setRightBrace(String rightBrace) {
        this.rightBrace = rightBrace;
    }

    public void setSeparator(String separator) {
        this.iterableWriter.setDelimiter(separator);
    }

    @Override
    public String write(Set<T> object) {
        return this.leftBrace + this.iterableWriter.write(object) + this.rightBrace;
    }

    public SetWriter<T> clone() {
        SetWriter<T> clone = new SetWriter<>();
        clone.iterableWriter = this.iterableWriter.clone();
        return clone;
    }
}
