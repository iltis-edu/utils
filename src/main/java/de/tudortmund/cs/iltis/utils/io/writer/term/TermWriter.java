package de.tudortmund.cs.iltis.utils.io.writer.term;

import de.tudortmund.cs.iltis.utils.io.writer.tree.TreeWriter;
import de.tudortmund.cs.iltis.utils.term.Term;
import java.io.Serializable;
import java.util.List;

/**
 * A default writer for terms.
 *
 * @param <SubtermT> The type of subterms.
 * @param <NameT> The type of the name.
 */
public class TermWriter<
                SubtermT extends Term<SubtermT, ? extends NameT>, NameT extends Serializable>
        extends TreeWriter<SubtermT> {

    public TermWriter() {
        super();
    }

    public TermWriter(String front, String back, String separator) {
        super(front, back, separator);
    }

    @Override
    public String inspect(SubtermT term, List<String> childrenOutput) {
        StringBuilder text = new StringBuilder();
        if (term.isNamed()) text.append(term.getName());
        if (term.getArity() > 0) text.append(super.inspect(term, childrenOutput));
        return text.toString();
    }
}
