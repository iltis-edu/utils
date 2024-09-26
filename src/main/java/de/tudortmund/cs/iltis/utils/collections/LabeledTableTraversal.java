package de.tudortmund.cs.iltis.utils.collections;

import java.util.ListIterator;
import java.util.Optional;

public abstract class LabeledTableTraversal<RowLabel, ColumnLabel, CellLabel> {
    public enum HorizontalDisplay {
        None,
        OnlyLeft,
        OnlyRight,
        Both
    };

    public enum VerticalDisplay {
        None,
        OnlyTop,
        OnlyBottom,
        Both
    };

    public enum HorizontalDirection {
        LeftToRight,
        RightToLeft
    };

    public enum VerticalDirection {
        TopToBottom,
        BottomToTop
    };

    private HorizontalDisplay horizontalDisplay;
    private VerticalDisplay verticalDisplay;
    private HorizontalDirection horizontalDirection;
    private VerticalDirection verticalDirection;

    public LabeledTableTraversal() {
        this(
                HorizontalDisplay.OnlyLeft,
                VerticalDisplay.OnlyTop,
                HorizontalDirection.LeftToRight,
                VerticalDirection.TopToBottom);
    }

    public LabeledTableTraversal(
            HorizontalDisplay horizontalDisplay, VerticalDisplay verticalDisplay) {
        this(
                horizontalDisplay,
                verticalDisplay,
                HorizontalDirection.LeftToRight,
                VerticalDirection.TopToBottom);
    }

    public LabeledTableTraversal(
            HorizontalDisplay horizontalDisplay,
            VerticalDisplay verticalDisplay,
            HorizontalDirection horizontalDirection) {
        this(
                horizontalDisplay,
                verticalDisplay,
                horizontalDirection,
                VerticalDirection.TopToBottom);
    }

    public LabeledTableTraversal(
            HorizontalDisplay horizontalDisplay,
            VerticalDisplay verticalDisplay,
            HorizontalDirection horizontalDirection,
            VerticalDirection verticalDirection) {
        this.horizontalDisplay = horizontalDisplay;
        this.verticalDisplay = verticalDisplay;
        this.horizontalDirection = horizontalDirection;
        this.verticalDirection = verticalDirection;
    }

    public void setHorizontalDisplay(HorizontalDisplay horizontalDisplay) {
        this.horizontalDisplay = horizontalDisplay;
    }

    public void setVerticalDisplay(VerticalDisplay verticalDisplay) {
        this.verticalDisplay = verticalDisplay;
    }

    public void setHorizontalDirection(HorizontalDirection horizontalDirection) {
        this.horizontalDirection = horizontalDirection;
    }

    public void setVerticalDirection(VerticalDirection verticalDirection) {
        this.verticalDirection = verticalDirection;
    }

    public void traverse(LabeledTable<RowLabel, ColumnLabel, CellLabel> table) {
        this.enterTable();

        this.traverseColumnLabelsAtTop(table);
        this.traverseRows(table);

        this.traverseColumnLabelsAtBottom(table);
        this.leaveTable();
    }

    protected void traverseColumnLabelsAtTop(LabeledTable<RowLabel, ColumnLabel, CellLabel> table) {
        if (this.displayColumnLabelAtTop()) {
            this.enterColumnLabelsAtTop();
            this.enterRow();
            if (this.displayRowLabelAtLeft()) this.visitColumnLabel(Optional.<ColumnLabel>empty());

            ListIterator<Integer> columnIt;
            if (this.horizontalDirection == HorizontalDirection.LeftToRight)
                columnIt = table.getColumnPositions().listIterator();
            else columnIt = new ReverseListIterator<Integer>(table.getColumnPositions());
            while (columnIt.hasNext())
                this.visitColumnLabel(
                        Optional.<ColumnLabel>of(table.getColumnByPosition(columnIt.next())));

            if (this.displayRowLabelAtRight()) this.visitColumnLabel(Optional.<ColumnLabel>empty());
            this.leaveRow();
            this.leaveColumnLabelsAtTop();
        }
    }

    protected void traverseColumnLabelsAtBottom(
            LabeledTable<RowLabel, ColumnLabel, CellLabel> table) {
        if (this.displayColumnLabelAtBottom()) {
            this.enterColumnLabelsAtBottom();
            this.enterRow();
            if (this.displayRowLabelAtLeft()) this.visitColumnLabel(Optional.<ColumnLabel>empty());
            ListIterator<Integer> columnIt;
            if (this.horizontalDirection == HorizontalDirection.LeftToRight)
                columnIt = table.getColumnPositions().listIterator();
            else columnIt = new ReverseListIterator<Integer>(table.getColumnPositions());
            while (columnIt.hasNext())
                this.visitColumnLabel(
                        Optional.<ColumnLabel>of(table.getColumnByPosition(columnIt.next())));

            if (this.displayRowLabelAtRight()) this.visitColumnLabel(Optional.<ColumnLabel>empty());
            this.leaveRow();
            this.leaveColumnLabelsAtBottom();
        }
    }

    protected void traverseRows(LabeledTable<RowLabel, ColumnLabel, CellLabel> table) {
        ListIterator<Integer> rowIt;
        if (this.verticalDirection == VerticalDirection.TopToBottom)
            rowIt = table.getRowPositions().listIterator();
        else rowIt = new ReverseListIterator<Integer>(table.getRowPositions());
        while (rowIt.hasNext()) {
            int rowIndex = rowIt.next();
            RowLabel rowLabel = table.getRowByPosition(rowIndex);
            this.enterRow();
            if (this.displayRowLabelAtLeft()) {
                this.enterRowLabelsAtLeft();
                this.visitRowLabel(Optional.<RowLabel>of(rowLabel));
                this.leaveRowLabelsAtLeft();
            }

            this.traverseCells(table, rowIndex);

            if (this.displayRowLabelAtRight()) {
                this.enterRowLabelsAtRight();
                this.visitRowLabel(Optional.<RowLabel>of(rowLabel));
                this.leaveRowLabelsAtRight();
            }
            this.leaveRow();
        }
    }

    protected void traverseCells(
            LabeledTable<RowLabel, ColumnLabel, CellLabel> table, int rowIndex) {
        ListIterator<Integer> columnIt;
        if (this.horizontalDirection == HorizontalDirection.LeftToRight)
            columnIt = table.getColumnPositions().listIterator();
        else columnIt = new ReverseListIterator<Integer>(table.getColumnPositions());
        while (columnIt.hasNext()) {
            CellLabel cellLabel = table.getCellByPosition(rowIndex, columnIt.next());
            this.visitCellLabel(Optional.<CellLabel>of(cellLabel));
        }
    }

    protected boolean displayColumnLabelAtTop() {
        return this.verticalDisplay == VerticalDisplay.OnlyTop
                || this.verticalDisplay == VerticalDisplay.Both;
    }

    protected boolean displayColumnLabelAtBottom() {
        return this.verticalDisplay == VerticalDisplay.OnlyBottom
                || this.verticalDisplay == VerticalDisplay.Both;
    }

    protected boolean displayRowLabelAtLeft() {
        return this.horizontalDisplay == HorizontalDisplay.OnlyLeft
                || this.horizontalDisplay == HorizontalDisplay.Both;
    }

    protected boolean displayRowLabelAtRight() {
        return this.horizontalDisplay == HorizontalDisplay.OnlyRight
                || this.horizontalDisplay == HorizontalDisplay.Both;
    }

    protected void enterTable() {}

    protected void leaveTable() {}

    protected void enterColumnLabelsAtTop() {}

    protected void leaveColumnLabelsAtTop() {}

    protected void enterColumnLabelsAtBottom() {}

    protected void leaveColumnLabelsAtBottom() {}

    protected void enterRowLabelsAtLeft() {}

    protected void leaveRowLabelsAtLeft() {}

    protected void enterRowLabelsAtRight() {}

    protected void leaveRowLabelsAtRight() {}

    protected void enterRow() {}

    protected void leaveRow() {}

    protected void visitColumnLabel(Optional<ColumnLabel> columnLabelOptional) {}

    protected void visitRowLabel(Optional<RowLabel> rowLabelOptional) {}

    protected abstract void visitCellLabel(Optional<CellLabel> cellLabelOptional);
}
