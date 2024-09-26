package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.pattern.TreePattern;

/** Abstract class that uses patterns for transformation */
public abstract class PatternTransformation<T extends Tree<T>> implements Transformation<T> {
    protected TreePattern<T> matchPattern;
    protected TreePattern<T> replacePattern;

    public PatternTransformation() { // Serialization
        this.matchPattern = null;
        this.replacePattern = null;
    }

    public PatternTransformation(TreePattern<T> match, TreePattern<T> replace) {

        this.matchPattern = match;
        this.replacePattern = replace;
    }

    public TreePattern<T> getMatchPattern() {
        return matchPattern;
    }

    public TreePattern<T> getReplacePattern() {
        return replacePattern;
    }

    @Override
    public String toString() {
        return this.matchPattern + " --> " + this.replacePattern;
    }
}
