package de.tudortmund.cs.iltis.utils.tree.pattern;

/** Types for tree patterns */
public enum TreePatternType implements PatternType {
    AlternativePattern,
    ComplementPattern,
    PredicatePattern,
    EqualsPattern,
    AnyPattern,
    MultiConstraintPattern,
    ChildrenPattern,
    ContainsDescendantPattern,
    RepeatForestPattern,
    FixedArityForestPattern,
    FlexibleArityForestPattern;
}
