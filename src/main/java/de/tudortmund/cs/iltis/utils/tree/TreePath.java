package de.tudortmund.cs.iltis.utils.tree;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import javax.annotation.Nullable;

@SuppressWarnings("serial")
public class TreePath extends Stack<Integer> implements Comparable<TreePath>, Serializable {

    public TreePath() {
        super();
    }

    public TreePath(final TreePath other) {
        this();
        for (int id : other) this.push(id);
    }

    public TreePath(final String pathDescription) {
        this();
        String[] ids = pathDescription.split("-");
        for (String id : ids) if (!id.isEmpty()) this.push(Integer.parseInt(id));
    }

    public TreePath clone() {
        return new TreePath(this);
    }

    public String toString() {
        String pathDescription = "";
        for (int id : this) pathDescription += "-" + id;
        return pathDescription;
    }

    public TreePath down(final int levels) {
        if (levels < 0) throw new IllegalArgumentException();
        for (int i = 0; i < levels; i++) this.push(0);
        return this;
    }

    public TreePath down() {
        this.push(0);
        return this;
    }

    public TreePath left() {
        this.push(0);
        return this;
    }

    public TreePath right() {
        this.push(1);
        return this;
    }

    public TreePath child(final int index) {
        this.push(index);
        return this;
    }

    public TreePath up(final int levels) {
        if (levels < 0) throw new IllegalArgumentException();
        for (int i = 0; i < levels; i++) this.pop();
        return this;
    }

    public TreePath up() {
        this.pop();
        return this;
    }

    public TreePath concatenate(TreePath other) {
        for (int id : other) {
            this.push(id);
        }
        return this;
    }

    public TreePath getParent() {
        TreePath parent = new TreePath(this);
        parent.pop();
        return parent;
    }

    /**
     * Removes all items down to and including index
     *
     * @param index items to remove up to
     * @return shortened TreePath
     */
    public TreePath getPathUpTo(int index) {
        TreePath ancestor = new TreePath(this);

        if (index >= size()) return ancestor;

        for (int i = size() - 1; i >= index; i--) {
            ancestor.remove(i);
        }

        return ancestor;
    }

    public TreePath getPathFrom(int index) {
        TreePath child = new TreePath();

        if (index >= size()) return child;

        for (int i = index; i < size(); i++) {
            child.add(get(i));
        }

        return child;
    }

    public TreePath getLowestCommonAncestor(final TreePath other) {
        TreePath lcaPath = new TreePath();
        Iterator<Integer> thisIt = this.iterator();
        Iterator<Integer> otherIt = other.iterator();
        while (thisIt.hasNext() && otherIt.hasNext()) {
            int id = thisIt.next();
            if (otherIt.next() == id) lcaPath.push(id);
            else break;
        }
        return lcaPath;
    }

    public static @Nullable TreePath getLowestCommonAncestor(Collection<TreePath> paths) {
        if (paths.isEmpty()) return null;

        return getLowestCommonAncestor(paths, 0);
    }

    private static @Nullable TreePath getLowestCommonAncestor(
            Collection<TreePath> paths, int index) {
        int id = -1;
        for (TreePath path : paths) {
            if (index >= path.size()) return path.clone();

            if (id == -1) {
                id = path.get(index);
                continue;
            }

            if (id != path.get(index)) return path.getPathUpTo(index);
        }

        return getLowestCommonAncestor(paths, index + 1);
    }

    @Override
    public int compareTo(TreePath o) {
        TreePath other = (TreePath) o;
        for (int i = 0; i < this.size() && i < other.size(); i++) {
            if (this.get(i) < other.get(i)) return -1;
            if (this.get(i) > other.get(i)) return +1;
        }
        if (this.size() < other.size()) return -1;
        if (this.size() > other.size()) return +1;
        return 0;
    }

    public boolean isAncestorOf(final TreePath other) {
        if (other.size() <= this.size()) return false;
        for (int i = 0; i < this.size(); i++) if (!other.get(i).equals(this.get(i))) return false;
        return true;
    }

    public boolean isDescendantOf(final TreePath other) {
        return other.isAncestorOf(this);
    }

    public boolean isParentOf(final TreePath other) {
        return (other.size() == this.size() + 1) && this.isAncestorOf(other);
    }

    public boolean isChildOf(final TreePath other) {
        return other.isParentOf(this);
    }

    public boolean isSiblingOf(final TreePath other) {
        final TreePath lcaPath = this.getLowestCommonAncestor(other);
        return this.isChildOf(lcaPath) && other.isChildOf(lcaPath) && this.peek() != other.peek();
    }
}
