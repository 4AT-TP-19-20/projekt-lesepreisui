package sample;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CustomResizePolicy {
    private double mTVWidth;

    public Boolean call(TableView.ResizeFeatures<?> prop) {
        TableView<?> tv = prop.getTable();
        double tvWidth = tv.getWidth();

        if (tvWidth <= 0.0) {
            return false;
        }

        if (mTVWidth != tvWidth && prop.getColumn() == null) {
            mTVWidth = tvWidth;
            double totalColumnWidth = 0;
            int columnsToResize = 0;

            for (TableColumn<?, ?> col : tv.getColumns()) {
                if(col.isResizable() && col.isVisible()) {
                    totalColumnWidth += col.getWidth();
                    columnsToResize++;
                }
            }

            if (columnsToResize == 0) {
                return TableView.CONSTRAINED_RESIZE_POLICY.call(prop);
            }

            double newWidth = Math.floor((tvWidth - totalColumnWidth) / columnsToResize);
            double realWidth = 0;

            for (TableColumn<?, ?> col : tv.getVisibleLeafColumns()) {
                col.setPrefWidth(col.getWidth() + newWidth);
                realWidth += col.getWidth();
            }

            if(newWidth != 0) {
                TableColumn<?, ?> col = tv.getVisibleLeafColumn(tv.getVisibleLeafColumns().size() - 1);
                col.setPrefWidth(col.getWidth() + (tvWidth - realWidth));
            }

            return true;
        } else {
            return TableView.CONSTRAINED_RESIZE_POLICY.call(prop);
        }
    }

}
