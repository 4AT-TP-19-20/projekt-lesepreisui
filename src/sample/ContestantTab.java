package sample;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

public class ContestantTab {
    public static BorderPane generate() {
        BorderPane borderPane = new BorderPane();

        //Top
        HBox topItems = new HBox();
        TextField txt_search = new TextField();
        txt_search.setPromptText("Suche nach Name, Klasse, ...");
        txt_search.setMaxWidth(10000);
        Button btn_search = new Button("Suche");
        HBox.setHgrow(txt_search, Priority.ALWAYS);
        topItems.getChildren().addAll(txt_search, btn_search);
        topItems.setSpacing(5);
        borderPane.setTop(topItems);

        //Center
        TableView<Contestant> tbv_contestants = new TableView<>();
        TableColumn<Contestant, String> column_firstName = new TableColumn<>("Vorname");
        column_firstName.setCellFactory(param -> new AlignedTableCell<>());
        column_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<Contestant, String> column_lastName = new TableColumn<>("Nachname");
        column_lastName.setCellFactory(param -> new AlignedTableCell<>());
        column_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        TableColumn<Contestant, String> column_grade = new TableColumn<>("Klasse");
        column_grade.setCellFactory(param -> new AlignedTableCell<>());
        column_grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        TableColumn<Contestant, Integer> column_bookCount = new TableColumn<>("Gelesene Bücher");
        column_bookCount.setCellFactory(param -> new AlignedTableCell<>());
        column_bookCount.setCellValueFactory(new PropertyValueFactory<>("bookCount"));
        TableColumn<Contestant, Integer> column_points = new TableColumn<>("Punkte");
        column_points.setCellFactory(param -> new AlignedTableCell<>());
        column_points.setCellValueFactory(new PropertyValueFactory<>("points"));
        tbv_contestants.getColumns().addAll(column_firstName, column_lastName, column_grade, column_bookCount, column_points);
        tbv_contestants.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbv_contestants.setItems(Data.contestants);
        tbv_contestants.setOnMouseClicked((MouseEvent event) -> {
            if(event.getTarget() instanceof TableColumnHeader) {
                tbv_contestants.getSelectionModel().clearSelection();
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                if(tbv_contestants.getSelectionModel().getSelectedItem() != null) {
                    ContestantDetailWindow.showNewWindow(tbv_contestants.getSelectionModel().getSelectedItem());
                }
            }
        });
        borderPane.setCenter(tbv_contestants);

        //Bottom
        Button btn_addContestant = new Button("Neuer Teilnehmer");
        btn_addContestant.setMaxWidth(10000);
        borderPane.setBottom(btn_addContestant);

        BorderPane.setMargin(borderPane.getCenter(), new Insets(10, 0, 10, 0));
        borderPane.setPadding(new Insets(10));
        return borderPane;
    }
}
