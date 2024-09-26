package de.tudortmund.cs.iltis.utils.io.writer.general;

public class DefaultWriter<T> implements Writer<T> {
    public String write(T object) {
        return object.toString();
    }
}
