package de.tudortmund.cs.iltis.utils;

import java.io.Serializable;

public class SubscriptCounter implements NamingStrategy, Serializable {
    private String name;
    private int startIndex;

    public SubscriptCounter(String name) {
        this(name, 1);
    }

    public SubscriptCounter(String name, int startIndex) {
        this.name = name;
        this.startIndex = startIndex;
    }

    private SubscriptCounter() {}

    public IndexedSymbol getFirst() {
        return new IndexedSymbol(this.name, Integer.toString(startIndex), "");
    }

    public IndexedSymbol getNext(IndexedSymbol current) {
        int currentValue = Integer.valueOf(current.getSubscript());
        return new IndexedSymbol(this.name, Integer.toString(currentValue + 1), "");
    }
}
