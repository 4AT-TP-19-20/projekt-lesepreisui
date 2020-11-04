package sample;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.Optional;
import java.util.stream.Stream;

class CustomTableView<S extends Searchable & Comparable<S>> extends TableView<S> {
    private final ContextMenu menu;

    CustomTableView() {
        super();
        menu = new ContextMenu();

        this.skinProperty().addListener((event) -> {
            TableHeaderRow header = getTableHeaderRow();
            if(header != null) {
                header.setOnMouseClicked((e) -> {
                    if(e.getButton() == MouseButton.SECONDARY) {
                        menu.show(header, e.getScreenX(), e.getScreenY());
                    }
                });

                header.addEventFilter(MouseEvent.MOUSE_CLICKED, (e) -> {
                    if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                        e.consume();
                    }
                });
            }
        });

        this.setColumnResizePolicy((features) -> new CustomResizePolicy().call(features));
    }
    
    <T> void addColumn(String title, Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> factory) {
        TableColumn<S, T> toAdd = new TableColumn<>(title);
        toAdd.setCellFactory(param -> new AlignedTableCell<>());
        toAdd.setCellValueFactory(factory);
        toAdd.setResizable(false);
        this.getColumns().add(toAdd);

        CheckMenuItem item = new CheckMenuItem(title);
        item.selectedProperty().bindBidirectional(toAdd.visibleProperty());
        menu.getItems().add(item);
    }

    private TableHeaderRow getTableHeaderRow() {
        TableViewSkin<?> tableSkin = (TableViewSkin<?>) this.getSkin();
        if (tableSkin == null) {
            return null;
        }

        Stream<Node> nodes = tableSkin.getChildren().stream();
        Optional<Node> res = nodes.filter(child -> child instanceof TableHeaderRow).findAny();

        return (TableHeaderRow) res.orElse(null);
    }
}
