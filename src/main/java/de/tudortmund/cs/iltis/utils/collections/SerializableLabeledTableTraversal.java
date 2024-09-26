package de.tudortmund.cs.iltis.utils.collections;

import java.io.Serializable;

public abstract class SerializableLabeledTableTraversal<
                RowLabel extends Serializable,
                ColumnLabel extends Serializable,
                CellLabel extends Serializable>
        extends LabeledTableTraversal<RowLabel, ColumnLabel, CellLabel> {}
