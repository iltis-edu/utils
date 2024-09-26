package de.tudortmund.cs.iltis.utils.tree.pattern;

import de.tudortmund.cs.iltis.utils.io.writer.tree.TreeWriter;
import java.util.List;

public class StringTreeWriter extends TreeWriter<StringTree> {
    @Override
    public String inspect(StringTree item, List<String> childrenOutput) {
        return item.getLabel() + super.inspect(item, childrenOutput);
    }
}
