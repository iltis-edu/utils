package de.tudortmund.cs.iltis.utils.tree.transformations;

import de.tudortmund.cs.iltis.utils.tree.Tree;
import de.tudortmund.cs.iltis.utils.tree.TreePath;
import de.tudortmund.cs.iltis.utils.tree.pattern.TreePattern;
import de.tudortmund.cs.iltis.utils.tree.pattern.match.Match;
import java.util.Collection;
import java.util.Collections;

/** Transformation that uses patterns and arbitrary number of arguments in form of tree paths */
public class NAryPatternTransformation<T extends Tree<T>> extends PatternTransformation<T>
        implements NAryTransformation<T> {

    protected Collection<TreePath> paths;

    // protected TreePath transformationRoot;

    public NAryPatternTransformation() { // Serialization
    }

    public NAryPatternTransformation(TreePattern<T> match, TreePattern<T> replace) {

        this(Collections.emptyList(), match, replace);
    }

    public NAryPatternTransformation(
            final Collection<TreePath> paths, TreePattern<T> match, TreePattern<T> replace) {

        super(match, replace);
        setup(paths);
    }

    /**
     * Calculates the transformation root given all paths
     *
     * @param paths paths to do setup with
     */
    private void setup(final Collection<TreePath> paths) {
        this.paths = paths;

        //		if (!paths.isEmpty()) {
        //			TreePath smallestFormulaPath = smallestPath(paths);
        //			Collection<TreePath> varPaths = matchPattern.
        //					getSortedVariables().stream().filter(t -> t.isRelevant())
        //					.map(t -> t.getPath()).collect(Collectors.toList());
        //			TreePath smallestPatternPath = smallestPath(varPaths);
        //
        //			int difference = Math.max(smallestFormulaPath.size() - smallestPatternPath.size(), 0);
        //
        //			transformationRoot = smallestFormulaPath.getPathUpTo(difference);
        //		}
    }

    //	/**
    //	 * Returns path that is the least deep
    //	 *
    //	 * @param paths
    //	 *            paths to pick smallest from
    //	 * @return smallest path
    //	 */
    //	private static TreePath smallestPath(Collection<TreePath> paths) {
    //		TreePath smallest = null;
    //
    //		for (TreePath path : paths) {
    //			if (smallest == null || path.size() < smallest.size())
    //				smallest = path;
    //		}
    //
    //		return smallest;
    //	}

    @Override
    public boolean isApplicable(T tree) {
        //		ModalFormula rootFormula = formula.retrieve(transformationRoot);
        //		ImmutableMatch match = matchRelevantVars(rootFormula);
        //
        //		if (!match.matches())
        //			return false;
        //
        //		match.setImmutable(true);
        //		return matchPattern.matches(match, rootFormula);
        return matchPattern.matches(tree);
    }

    @Override
    public T apply(T tree) {
        //		ModalFormula rootFormula = formula.retrieve(transformationRoot);
        //		ImmutableMatch match = matchRelevantVars(rootFormula);
        //
        //		if (!match.matches())
        //			return null;
        //
        //		match.setImmutable(true);
        //		matchPattern.matches(match, rootFormula);
        //		return replaceSubformula(formula, transformationRoot, replacePattern.create(match));
        Match<T> match = matchPattern.getFirstMatchIfAny(tree).get();
        return (T) tree.transform(new TreePath(), replacePattern.createTree(match));
    }

    //	/**
    //	 * Matches relevant variables of this transformation with paths that the
    //	 * user supplied
    //	 *
    //	 * @param rootFormula
    //	 *            root formula of this transformation
    //	 * @return match containing relevant variables
    //	 */
    //	private ImmutableMatch matchRelevantVars(ModalFormula rootFormula) {
    //		ImmutableMatch match = new ImmutableMatch();
    //
    //		List<PatternVariableInfo> relevantVars = matchPattern.getSortedVariables().stream().filter(t
    // -> t.isRelevant())
    //				.collect(Collectors.toList());
    //
    //		Iterator<TreePath> pathIt = paths.iterator();
    //
    //		TreePath formulaPath = pathIt.next().getPathFrom(transformationRoot.size());
    //		ModalFormula selectedFormula = rootFormula.retrieve(formulaPath);
    //
    //		for (PatternVariableInfo var : relevantVars) {
    //			final TreePath varPath = var.getPath();
    //
    //			if (formulaPath == null) {
    //				match.fail();
    //				break;
    //			}
    //
    //			if (!var.isMultiple()) {
    //				if (formulaPath.size() != var.getPath().size()) {
    //					match.fail();
    //					break;
    //				}
    //
    //				match.setFormula(var.getId(), selectedFormula);
    //
    //				// next formula
    //				if (pathIt.hasNext()) {
    //					formulaPath = pathIt.next().getPathFrom(transformationRoot.size());
    //					selectedFormula = rootFormula.retrieve(formulaPath);
    //				} else {
    //					formulaPath = null;
    //				}
    //			} else {
    //				TreePath root = getMultiplyingRoot(matchPattern, varPath);
    //
    //				while (formulaPath != null && root.isAncestorOf(formulaPath)) {
    //					// belongs to this repeat pattern
    //					match.addFormula(var.getId(), selectedFormula);
    //
    //					// next formula
    //					if (pathIt.hasNext()) {
    //						formulaPath = pathIt.next().getPathFrom(transformationRoot.size());
    //						selectedFormula = rootFormula.retrieve(formulaPath);
    //					} else {
    //						formulaPath = null;
    //					}
    //				}
    //			}
    //		}
    //
    //		// too many selections
    //		if (pathIt.hasNext())
    //			match.fail();
    //		return match;
    //	}

    //	/**
    //	 * Returns the path to the pattern that enables a {@link RepeatPattern} to
    //	 * have an arbitrary number of children, namely a
    //	 * {@link PatternWithVariableSubpatterns}
    //	 *
    //	 * @param pattern
    //	 *            pattern to search in
    //	 * @param varPath
    //	 *            path to variable
    //	 * @return path to multiplying root
    //	 */
    //	private static TreePath getMultiplyingRoot(TreePattern<ModalFormula> pattern,
    //			TreePath varPath) {
    //
    //		TreePattern<ModalFormula> sub = pattern.retrieve(varPath);
    //		if (sub instanceof PatternWithVariableSubpatterns)
    //			return varPath;
    //		else if (varPath.isEmpty())
    //			return null;
    //		else
    //			return getMultiplyingRoot(pattern, varPath.getParent());
    //	}

    @Override
    public Transformation<T> forPath(TreePath path) {
        return new NAryPatternTransformation<>(
                Collections.singleton(path), matchPattern, replacePattern);
    }

    @Override
    public NAryTransformation<T> forPaths(Collection<TreePath> paths) {
        return new NAryPatternTransformation<>(paths, matchPattern, replacePattern);
    }
}
