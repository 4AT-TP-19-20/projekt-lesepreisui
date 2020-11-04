package sample;

import com.sun.javafx.tk.Toolkit;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;
import javafx.util.Callback;

public class CustomResizePolicy {

    public Boolean call(TableView.ResizeFeatures prop) {
        TableView<?> table = prop.getTable();
        double tableWidth = table.getWidth();

        if (tableWidth <= 0.0) {
            return false;
        }

        double totalColumnWidth = 0;

        if (table.getVisibleLeafColumns().size() == 0) {
            return TableView.CONSTRAINED_RESIZE_POLICY.call(prop);
        }

        for (TableColumn<?, ?> col : table.getVisibleLeafColumns()) {
            double contentWidth = getContentMaxWidth(col);
            double headerWidth = getHeadersWidth(col);

            totalColumnWidth += Math.max(contentWidth, headerWidth);
        }

        double delta = Math.floor((tableWidth - totalColumnWidth) / table.getVisibleLeafColumns().size());
        double realWidth = 0;

        for (TableColumn<?, ?> col : table.getVisibleLeafColumns()) {
            double contentWidth = getContentMaxWidth(col);
            double headerWidth = getHeadersWidth(col);

            double minWidth = Math.max(contentWidth, headerWidth);

            col.setPrefWidth(minWidth + delta);
            realWidth += col.getWidth();
        }

        if((tableWidth - realWidth) != 0) {
            TableColumn<?, ?> col = table.getVisibleLeafColumn(table.getVisibleLeafColumns().size() - 1);
            col.setPrefWidth(col.getWidth() + (tableWidth - realWidth));
        }

        return true;
    }

    private <S, T> double getContentMaxWidth(TableColumn<S, T> column) {
        double max = 0.0;
        TableView<S> table = column.getTableView();

        for(S s : table.getItems()) {
            Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> callback = column.getCellValueFactory();
            TableColumn.CellDataFeatures<S, T> features = new TableColumn.CellDataFeatures<>(column.getTableView(), column, s);

            ObservableValue<T> res = callback.call(features);

            double width = Toolkit.getToolkit().getFontLoader().computeStringWidth(res.getValue().toString(), new Font(14));

            if(max < width) {
                max = width;
            }
        }

        return max;
    }

    private double getHeadersWidth(TableColumn<?, ?> column) {
        return Toolkit.getToolkit().getFontLoader().computeStringWidth(column.getText(), new Font(18));
    }
}
