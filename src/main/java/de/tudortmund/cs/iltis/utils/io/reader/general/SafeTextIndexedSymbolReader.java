package de.tudortmund.cs.iltis.utils.io.reader.general;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import de.tudortmund.cs.iltis.utils.IndexedSymbol;

/**
 * Creates a new indexed symbol with the name, subscript and superscript read from the argument.
 *
 * <p>The argument has to be of the form <code>w^s_t</code> or <code>w_t^s</code> where <code>w,s,t
 * </code> are non-empty strings not containing "^" and "_". The parts <code>^s</code> and <code>_t
 * </code> are optional.
 */
public class SafeTextIndexedSymbolReader implements Reader<IndexedSymbol> {

    private RegExp regExp;

    public SafeTextIndexedSymbolReader() {
        String pattern =
                "^([^\\^_]+)(?:((?:\\^([^\\^_]+))?(?:_([^\\^_]+))?)|((?:_([^\\^_]+))?(?:\\^([^\\^_]+))?))$";
        regExp = RegExp.compile(pattern);
    }

    /**
     * Creates an indexed symbol with the name, subscript and superscript read from the argument.
     *
     * <p>The argument has to be a string of the form <code>w^s_t</code> or <code>w_t^s</code> where
     * <code>w,s,t</code> are non-empty strings not containing "^" and "_". The parts <code>^s
     * </code> and <code>_t</code> are optional.
     *
     * @throws IllegalArgumentException if the argument is not of this form
     */
    @Override
    public IndexedSymbol read(Object o) throws RuntimeException {
        String parseString = o.toString();
        MatchResult matcher = regExp.exec(parseString);
        if (matcher == null) throw new IllegalArgumentException();

        String name = matcher.getGroup(1);
        String superscript, subscript;
        if (matcher.getGroup(2) != null) {
            superscript = matcher.getGroup(3);
            subscript = matcher.getGroup(4);
        } else {
            superscript = matcher.getGroup(7);
            subscript = matcher.getGroup(6);
        }
        return new IndexedSymbol(name, subscript, superscript);
    }
}
