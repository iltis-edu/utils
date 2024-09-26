package de.tudortmund.cs.iltis.utils.io.writer.general;

public class EmptyWriter<T> implements Writer<T> {
    @Override
    public String write(T object) {
        return "";
    }
}
