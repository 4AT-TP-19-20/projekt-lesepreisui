package sample;

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
        TableView tbv_contestants = new TableView();
        TableColumn column_firstName = new TableColumn("Vorname");
        column_firstName.setCellValueFactory(new PropertyValueFactory<Contestant, String>("firstName"));
        TableColumn column_lastName = new TableColumn("Nachname");
        column_lastName.setCellValueFactory(new PropertyValueFactory<Contestant, String>("lastName"));
        TableColumn column_grade = new TableColumn("Klasse");
        column_grade.setCellValueFactory(new PropertyValueFactory<Contestant, String>("grade"));
        tbv_contestants.getColumns().addAll(column_firstName, column_lastName, column_grade);
        tbv_contestants.setItems(Data.contestants);
        tbv_contestants.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                if(tbv_contestants.getSelectionModel().getSelectedItem() instanceof Contestant) {
                    ContestantDetailWindow.showNewWindow((Contestant) tbv_contestants.getSelectionModel().getSelectedItem());
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
