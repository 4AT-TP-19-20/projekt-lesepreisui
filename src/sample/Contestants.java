package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Contestants
{
    public static BorderPane generate()
    {
        Contestant c1 = new Contestant("Manuel", "Ploner", "4AT");
        Contestant c2 = new Contestant("Mattia", "Galiani", "4AT");
        Contestant c3 = new Contestant("Maximilian", "Mitterrutzner", "4AT");

        BorderPane borderPane = new BorderPane();

        //Top
        HBox topItems = new HBox();
        TextField txt_search = new TextField();
        txt_search.setPromptText("Suche nach Name, Klasse, ...");
        txt_search.setMaxWidth(300);
        Button btn_search = new Button("Suche");
        topItems.getChildren().addAll(txt_search, btn_search);
        topItems.setSpacing(5);
        borderPane.setTop(topItems);

        //Center
        TableView tbv_contestants = new TableView();
        ObservableList<Contestant> contestants = FXCollections.observableArrayList(c1, c2, c3);
        TableColumn column_firstName = new TableColumn("Vorname");
        column_firstName.setCellValueFactory(new PropertyValueFactory<Contestant, String>("firstName"));
        TableColumn column_lastName = new TableColumn("Nachname");
        column_lastName.setCellValueFactory(new PropertyValueFactory<Contestant, String>("lastName"));
        TableColumn column_grade = new TableColumn("Klasse");
        column_grade.setCellValueFactory(new PropertyValueFactory<Contestant, String>("grade"));
        tbv_contestants.setItems(contestants);
        tbv_contestants.getColumns().addAll(column_firstName, column_lastName, column_grade);
        borderPane.setCenter(tbv_contestants);

        //Bottom

        BorderPane.setMargin(borderPane.getCenter(), new Insets(10,0,10,0));
        borderPane.setPadding(new Insets(10));
        return borderPane;
    }
}
