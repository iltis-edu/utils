package de.tudortmund.cs.iltis.utils.io.writer.collections;

import de.tudortmund.cs.iltis.utils.general.Data;
import de.tudortmund.cs.iltis.utils.io.writer.general.DefaultWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;

public class IterableWriter<T> implements Writer<Iterable<T>> {
    private String delimiter;
    private Writer<T> elementWriter;

    public IterableWriter() {
        this.delimiter = ",";
        this.elementWriter = new DefaultWriter<T>();
    }

    public IterableWriter(Writer<T> elementWriter) {
        this.delimiter = ",";
        this.elementWriter = elementWriter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public String write(Iterable<T> object) {
        return String.join(
                this.delimiter,
                Data.map(Data.newArrayList(object), elem -> this.elementWriter.write(elem)));
    }

    public IterableWriter<T> clone() {
        return new IterableWriter<>();
    }
}
