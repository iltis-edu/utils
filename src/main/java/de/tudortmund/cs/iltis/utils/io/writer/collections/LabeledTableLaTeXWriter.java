package de.tudortmund.cs.iltis.utils.io.writer.collections;

import de.tudortmund.cs.iltis.utils.collections.LabeledTable;
import de.tudortmund.cs.iltis.utils.collections.LabeledTableTraversal;
import de.tudortmund.cs.iltis.utils.io.writer.general.DefaultWriter;
import de.tudortmund.cs.iltis.utils.io.writer.general.Writer;
import java.util.Optional;

public class LabeledTableLaTeXWriter<RowLabel, ColumnLabel, CellLabel>
        extends LabeledTableTraversal<RowLabel, ColumnLabel, CellLabel>
        implements Writer<LabeledTable<RowLabel, ColumnLabel, CellLabel>> {
    protected LabeledTable<RowLabel, ColumnLabel, CellLabel> table;
    protected Writer<RowLabel> rowLabelWriter;
    protected Writer<ColumnLabel> columnLabelWriter;
    protected Writer<CellLabel> cellLabelWriter;
    protected String latex;
    protected boolean firstColumn;
    protected String tableEnvironment;

    public LabeledTableLaTeXWriter(LabeledTable<RowLabel, ColumnLabel, CellLabel> table) {
        this.table = table;
        this.rowLabelWriter = new DefaultWriter<>();
        this.columnLabelWriter = new DefaultWriter<>();
        this.cellLabelWriter = new DefaultWriter<>();
        this.tableEnvironment = "tabular";
        this.reset();
    }

    public void reset() {
        this.latex = "";
        this.firstColumn = true;
    }

    public String getTableEnvironment() {
        return this.tableEnvironment;
    }

    public void setTableEnvironment(String tableEnvironment) {
        this.tableEnvironment = tableEnvironment;
    }

    public void setRowLabelWriter(Writer<RowLabel> rowLabelWriter) {
        this.rowLabelWriter = rowLabelWriter;
    }

    public void setColumnLabelWriter(Writer<ColumnLabel> columnLabelWriter) {
        this.columnLabelWriter = columnLabelWriter;
    }

    public void setCellLabelWriter(Writer<CellLabel> cellLabelWriter) {
        this.cellLabelWriter = cellLabelWriter;
    }

    public String write(LabeledTable<RowLabel, ColumnLabel, CellLabel> table) {
        if (this.table == table) {
            this.reset();
            return write();
        }
        LabeledTableLaTeXWriter<RowLabel, ColumnLabel, CellLabel> writer =
                new LabeledTableLaTeXWriter<>(table);
        return writer.write();
    }

    protected String write() {
        this.traverse(this.table);
        return latex;
    }

    @Override
    protected void enterTable() {
        StringBuilder builder = new StringBuilder();
        builder.append("\\begin{").append(this.tableEnvironment).append("}{");
        if (this.displayRowLabelAtLeft()) builder.append("l|");
        for (int i = 0; i < this.table.getColumnLabels().size(); i++) builder.append("c");
        if (this.displayRowLabelAtRight()) builder.append("|r");
        builder.append("}");
        this.latex += builder.toString();
    }

    @Override
    protected void leaveTable() {
        this.latex += "\n\\end{" + this.tableEnvironment + "}";
    }

    @Override
    protected void leaveColumnLabelsAtTop() {
        this.latex += "\\hline";
    }

    @Override
    protected void enterColumnLabelsAtBottom() {
        this.latex += "\\hline";
    }

    @Override
    protected void enterRow() {
        this.latex += "\n\t";
        this.firstColumn = true;
    }

    @Override
    protected void leaveRow() {
        this.latex += " \\\\";
    }

    @Override
    protected void visitColumnLabel(Optional<ColumnLabel> columnLabelOptional) {
        if (!this.firstColumn) this.latex += " & ";
        this.firstColumn = false;
        if (columnLabelOptional.isPresent())
            this.latex += this.columnLabelWriter.write(columnLabelOptional.get());
        else this.latex += " ";
    }

    @Override
    protected void visitRowLabel(Optional<RowLabel> rowLabelOptional) {
        if (!this.firstColumn) this.latex += " & ";
        if (rowLabelOptional.isPresent())
            this.latex += this.rowLabelWriter.write(rowLabelOptional.get());
        else this.latex += " ";
        this.firstColumn = false;
    }

    @Override
    protected void visitCellLabel(Optional<CellLabel> cellLabelOptional) {
        if (!this.firstColumn) this.latex += " & ";
        this.firstColumn = false;
        if (cellLabelOptional.isPresent())
            this.latex += this.cellLabelWriter.write(cellLabelOptional.get());
        else this.latex += " ";
    }
}
