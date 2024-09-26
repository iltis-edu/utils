package de.tudortmund.cs.iltis.utils.term;

import de.tudortmund.cs.iltis.utils.function.SerializableBiFunction;
import de.tudortmund.cs.iltis.utils.function.SerializableBiPredicate;
import de.tudortmund.cs.iltis.utils.tree.Tree;
import java.util.List;

/**
 * Compares two given lists of terms. Two lists are considered equal, if they have the same length
 * and if the elements in the same positions are <b>named</b> terms and <b>share their name</b>.
 *
 * @param <MatchedT> the type of term to be matched
 */
public class NameOnlyEqualityFunction<MatchedT extends Tree<MatchedT>>
        implements SerializableBiFunction<List<MatchedT>, List<MatchedT>, Boolean>,
                SerializableBiPredicate<List<MatchedT>, List<MatchedT>> {

    /**
     * Compares two given lists of terms. Two lists are considered equal, if they have the same
     * length and if the elements in the same positions are <b>named</b> terms and <b>share their
     * name</b>.
     */
    @Override
    public Boolean apply(List<MatchedT> t, List<MatchedT> u) {
        return test(t, u);
    }

    /**
     * Compares two given lists of terms. Two lists are considered equal, if they have the same
     * length and if the elements in the same positions are <b>named</b> terms and <b>share their
     * name</b>.
     */
    @Override
    public boolean test(List<MatchedT> t, List<MatchedT> u) {
        if (t == null || u == null) return false;
        if (t.size() != u.size()) return false;
        for (int i = 0; i < t.size(); i++) {
            Tree<MatchedT> tTree = t.get(i);
            Tree<MatchedT> uTree = u.get(i);
            if (!(tTree instanceof Term) || !(uTree instanceof Term)) return false;
            Term<?, ?> tTerm = (Term<?, ?>) tTree;
            Term<?, ?> uTerm = (Term<?, ?>) uTree;
            if (!testSingleTerm(tTerm, uTerm)) return false;
        }
        return true;
    }

    private boolean testSingleTerm(Term<?, ?> t, Term<?, ?> u) {
        return t.isNamed() && u.isNamed() && t.getName().equals(u.getName());
    }
}
