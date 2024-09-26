package de.tudortmund.cs.iltis.utils.weblib;

import java.util.*;

public class WebLib {

    private final String name;
    private final Map<String, WebLibFunction> functions;

    public WebLib(String name, Collection<WebLibFunction> functions) {
        this.name = name;
        this.functions = new HashMap<>();
        functions.forEach(f -> this.functions.put(f.getName(), f));
    }

    public String getLibraryName() {
        return name;
    }

    public Optional<WebLibFunction> getFunction(String name) {
        return Optional.ofNullable(functions.get(name));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebLib webLib = (WebLib) o;
        return Objects.equals(name, webLib.name) && Objects.equals(functions, webLib.functions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, functions);
    }

    @Override
    public String toString() {
        return "WebLib " + name + " [" + String.join(", ", functions.keySet()) + "]";
    }
}
