package de.tudortmund.cs.iltis.utils.io.writer.general;

import java.util.Map;

public class EntryWriter<K, V> implements Writer<Map.Entry<K, V>> {
    protected Writer<K> keyWriter;
    protected Writer<V> valueWriter;
    protected String mappingSymbol;

    public EntryWriter() {
        this("=");
    }

    public EntryWriter(String mappingSymbol) {
        this.mappingSymbol = mappingSymbol;
        this.keyWriter = new DefaultWriter<>();
        this.valueWriter = new DefaultWriter<>();
    }

    public void setKeyWriter(Writer<K> keyWriter) {
        this.keyWriter = keyWriter;
    }

    public void setValueWriter(Writer<V> valueWriter) {
        this.valueWriter = valueWriter;
    }

    public String write(Map.Entry<K, V> entry) {
        String output = this.keyWriter.write(entry.getKey());
        if (entry.getValue() != null) {
            output += this.mappingSymbol + this.valueWriter.write(entry.getValue());
        }
        return output;
    }
}
