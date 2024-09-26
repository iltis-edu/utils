package de.tudortmund.cs.iltis.utils;

public interface NamingStrategy {
    public IndexedSymbol getFirst();

    public IndexedSymbol getNext(IndexedSymbol current);
}
