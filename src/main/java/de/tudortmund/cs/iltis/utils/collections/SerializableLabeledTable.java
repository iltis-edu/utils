package de.tudortmund.cs.iltis.utils.collections;

import de.tudortmund.cs.iltis.utils.collections.exceptions.AmbiguousColumnLabelException;
import de.tudortmund.cs.iltis.utils.collections.exceptions.AmbiguousRowLabelException;
import de.tudortmund.cs.iltis.utils.general.Data;
import java.io.Serializable;
import java.util.*;

/**
 * Note: SerializableLabeledTable extends LabeledTable, but overrides everything from LabeledTable.
 * This is the case, because the content will be deleted in gwt serialization (possibly no access to
 * the inherited attributes). To avoid this the code from LabeledTable was copied.
 */
public class SerializableLabeledTable<
                RowLabel extends Serializable,
                ColumnLabel extends Serializable,
                CellLabel extends Serializable>
        extends LabeledTable<RowLabel, ColumnLabel, CellLabel> implements Serializable {

    protected List<RowLabel> rowLabels;
    protected List<ColumnLabel> columnLabels;
    protected Map<SerializablePair<Integer, Integer>, CellLabel> cells;

    public SerializableLabeledTable() {
        this.rowLabels = new ArrayList<RowLabel>();
        this.columnLabels = new ArrayList<ColumnLabel>();
        this.cells = new HashMap<SerializablePair<Integer, Integer>, CellLabel>();
    }

    public SerializableLabeledTable(List<RowLabel> rowLabels, List<ColumnLabel> columnLabels) {
        this.rowLabels = rowLabels;
        this.columnLabels = columnLabels;
        this.cells = new HashMap<SerializablePair<Integer, Integer>, CellLabel>();
    }

    public SerializableLabeledTable(
            List<RowLabel> rowLabels,
            List<ColumnLabel> columnLabels,
            Map<SerializablePair<Integer, Integer>, CellLabel> cells) {
        this.rowLabels = rowLabels;
        this.columnLabels = columnLabels;
        this.cells = cells;
    }

    public int getWidth() {
        return this.columnLabels.size();
    }

    public List<Integer> getColumnPositions() {
        return Data.range(0, this.getWidth());
    }

    public int getHeight() {
        return this.rowLabels.size();
    }

    public List<Integer> getRowPositions() {
        return Data.range(0, this.getHeight());
    }

    public ColumnLabel getColumnByPosition(int columnIndex) {
        return this.columnLabels.get(columnIndex);
    }

    public RowLabel getRowByPosition(int rowIndex) {
        return this.rowLabels.get(rowIndex);
    }

    public void addRow(RowLabel rowLabel, CellLabel... cellLabels) {
        int rowIndex = this.rowLabels.size();
        int columnIndex = 0;
        this.rowLabels.add(rowLabel);
        for (CellLabel cellLabel : cellLabels)
            this.setCellByPosition(rowIndex, columnIndex++, cellLabel);
    }

    public void addColumn(ColumnLabel columnLabel, CellLabel... cellLabels) {
        int columnIndex = this.columnLabels.size();
        int rowIndex = 0;
        this.columnLabels.add(columnLabel);
        for (CellLabel cellLabel : cellLabels)
            this.setCellByPosition(rowIndex, columnIndex++, cellLabel);
    }

    public List<ColumnLabel> getColumnLabels() {
        return this.columnLabels;
    }

    public List<RowLabel> getRowLabels() {
        return this.rowLabels;
    }

    public Optional<CellLabel> getCellOptionalByPosition(int rowIndex, int columnIndex) {
        SerializablePair<Integer, Integer> position = new SerializablePair(rowIndex, columnIndex);
        if (this.cells.containsKey(position))
            return Optional.<CellLabel>of(this.cells.get(position));
        return Optional.<CellLabel>empty();
    }

    public CellLabel getCellByPosition(int rowIndex, int columnIndex) {
        return this.getCellOptionalByPosition(rowIndex, columnIndex).get();
    }

    public Optional<CellLabel> getCellOptional(RowLabel rowLabel, ColumnLabel columnLabel) {
        SerializablePair<Integer, Integer> position = this.getPosition(rowLabel, columnLabel);
        if (this.cells.containsKey(position))
            return Optional.<CellLabel>of(this.cells.get(position));
        return Optional.<CellLabel>empty();
    }

    public CellLabel getCell(RowLabel rowLabel, ColumnLabel columnLabel) {
        return this.getCellOptional(rowLabel, columnLabel).get();
    }

    public void setCellByPosition(int rowIndex, int columnIndex, CellLabel cellLabel) {
        SerializablePair<Integer, Integer> position = new SerializablePair(rowIndex, columnIndex);
        this.cells.put(position, cellLabel);
    }

    public void setCell(RowLabel rowLabel, ColumnLabel columnLabel, CellLabel cellLabel) {
        SerializablePair<Integer, Integer> position = this.getPosition(rowLabel, columnLabel);
        this.cells.put(position, cellLabel);
    }

    public String toString() {
        String result = "{";
        boolean first = true;

        for (RowLabel row : rowLabels) {
            for (ColumnLabel column : columnLabels) {
                Optional<CellLabel> cell = this.getCellOptional(row, column);

                if (cell.isPresent()) {
                    if (first) {
                        first = false;
                    } else {
                        result += ",";
                    }

                    result += "(" + row.toString() + "," + column.toString() + ")";

                    result += " = " + cell.get().toString();
                }
            }
        }

        result += "}";

        return result;
    }

    private SerializablePair<Integer, Integer> getPosition(
            RowLabel rowLabel, ColumnLabel columnLabel) {
        int rowIndexFirst = this.rowLabels.indexOf(rowLabel);
        int rowIndexLast = this.rowLabels.indexOf(rowLabel);
        int columnIndexFirst = this.columnLabels.indexOf(columnLabel);
        int columnIndexLast = this.columnLabels.indexOf(columnLabel);

        if (rowIndexFirst != rowIndexLast) throw new AmbiguousRowLabelException(rowLabel);
        if (columnIndexFirst != columnIndexLast)
            throw new AmbiguousColumnLabelException(columnLabel);
        SerializablePair<Integer, Integer> position =
                new SerializablePair<Integer, Integer>(rowIndexFirst, columnIndexFirst);
        return position;
    }

    public SerializableLabeledTable<RowLabel, ColumnLabel, CellLabel> clone() {
        List<RowLabel> rowLabels = new ArrayList<>();
        List<ColumnLabel> columnLabels = new ArrayList<>();

        for (RowLabel row : this.rowLabels) {
            rowLabels.add(row);
        }

        for (ColumnLabel column : this.columnLabels) {
            columnLabels.add(column);
        }

        SerializableLabeledTable<RowLabel, ColumnLabel, CellLabel> result =
                new SerializableLabeledTable<>(rowLabels, columnLabels);

        for (RowLabel row : result.getRowLabels()) {
            for (ColumnLabel column : result.getColumnLabels()) {
                Optional<CellLabel> optCell = this.getCellOptional(row, column);

                if (optCell.isPresent()) {
                    result.setCell(row, column, optCell.get());
                }
            }
        }

        return result;
    }

    public Map<SerializablePair<Integer, Integer>, CellLabel> getCells() {
        return cells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerializableLabeledTable<?, ?, ?> that = (SerializableLabeledTable<?, ?, ?>) o;
        return Objects.equals(rowLabels, that.rowLabels)
                && Objects.equals(columnLabels, that.columnLabels)
                && Objects.equals(cells, that.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowLabels, columnLabels, cells);
    }
}
