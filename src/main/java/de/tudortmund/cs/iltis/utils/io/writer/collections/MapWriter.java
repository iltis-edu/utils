package de.tudortmund.cs.iltis.utils.io.writer.collections;

import de.tudortmund.cs.iltis.utils.io.writer.general.EntryWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;
import java.util.Map;

public class MapWriter<K, V> implements Writer<Map<K, V>> {
    private EntryWriter<K, V> entryWriter;
    private SetWriter<Map.Entry<K, V>> setWriter;
    private String entrySeparator;

    public MapWriter() {
        this("=", ",");
    }

    public MapWriter(String mappingSymbol, String entrySeparator) {
        this.entrySeparator = entrySeparator;
        this.entryWriter = new EntryWriter<>(mappingSymbol);
        this.setWriter = new SetWriter<>(this.entryWriter);
        this.setWriter.setSeparator(this.entrySeparator);
    }

    public SetWriter<Map.Entry<K, V>> getSetWriter() {
        return this.setWriter;
    }

    public void setEntryWriter(EntryWriter<K, V> entryWriter) {
        this.entryWriter = entryWriter;
    }

    public String write(Map<K, V> map) {
        return this.setWriter.write(map.entrySet());
    }
}
