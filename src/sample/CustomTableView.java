package sample;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class CustomTableView<S> extends TableView<S> {
    public CustomTableView() {
        super();
    }

    //TODO find solution without T parameter
    public <T> void addColumn(String title, T t, Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> factory) {
        TableColumn<S, T> toAdd = new TableColumn<>(title);
        toAdd.setCellFactory(param -> new AlignedTableCell<>());
        toAdd.setCellValueFactory(factory);
        this.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        this.getColumns().add(toAdd);
    }
}
