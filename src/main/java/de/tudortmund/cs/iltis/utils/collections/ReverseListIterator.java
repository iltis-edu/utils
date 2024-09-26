package de.tudortmund.cs.iltis.utils.collections;

import java.util.List;
import java.util.ListIterator;

public class ReverseListIterator<T> implements ListIterator<T> {
    public List<T> list;
    public int index;

    public ReverseListIterator(List<T> list) {
        this.list = list;
        this.index = list.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return this.index >= 0;
    }

    @Override
    public T next() {
        return this.list.get(this.index--);
    }

    @Override
    public boolean hasPrevious() {
        return this.index < this.list.size();
    }

    @Override
    public T previous() {
        return this.list.get(this.index++);
    }

    @Override
    public int nextIndex() {
        return this.index - 1;
    }

    @Override
    public int previousIndex() {
        return this.index + 1;
    }

    @Override
    public void remove() {
        this.list.remove(this.index);
    }

    @Override
    public void set(T t) {
        this.list.set(this.index, t);
    }

    @Override
    public void add(T t) {
        this.list.add(this.index, t);
    }
}
