package sample;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

class CustomTableView<S extends Searchable & Comparable<S>> extends TableView<S> {
    CustomTableView() {
        super();
    }
    
    <T> void addColumn(String title, Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> factory) {
        TableColumn<S, T> toAdd = new TableColumn<>(title);
        toAdd.setCellFactory(param -> new AlignedTableCell<>());
        toAdd.setCellValueFactory(factory);
        this.setColumnResizePolicy(new CustomResizePolicy());
        this.getColumns().add(toAdd);
    }
}
