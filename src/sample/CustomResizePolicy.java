package sample;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.util.Callback;

public class CustomResizePolicy implements Callback<ResizeFeatures, Boolean> {
    private double mTVWidth;

    @Override
    public Boolean call(TableView.ResizeFeatures prop) {
        TableView<?> tv = prop.getTable();
        double tvWidth = tv.getWidth();

        if (tvWidth <= 0.0) {
            return false;
        }

        if (mTVWidth != tvWidth && prop.getColumn() == null) {
            mTVWidth = tvWidth;
            double restWidth = 0;
            int numColsToSize = 0;

            for (TableColumn<?, ?> col : tv.getColumns()) {
                restWidth += col.getWidth();

                if(col.isResizable()) {
                    numColsToSize++;
                }
            }

            if (numColsToSize == 0) {
                return TableView.CONSTRAINED_RESIZE_POLICY.call(prop);
            }

            double newWidth = Math.floor((tvWidth - restWidth) / numColsToSize);
            double realWidth = 0;

            for (TableColumn<?, ?> col : tv.getColumns()) {
                if (col.isResizable() && col.isVisible()) {
                    col.setPrefWidth(col.getWidth() + newWidth);
                    realWidth += col.getWidth();
                }
            }

            if(tvWidth - realWidth != 0) {
                TableColumn<?, ?> col = tv.getColumns().get(tv.getColumns().size() - 1);
                col.setPrefWidth(col.getWidth() + (tvWidth - realWidth));
            }

            return true;
        } else {
            return TableView.CONSTRAINED_RESIZE_POLICY.call(prop);
        }
    }
}
