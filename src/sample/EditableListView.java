package sample;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class EditableListView extends BorderPane {
    private ListView<String> listView;
    private int index;

    public EditableListView(String titleText, String newText, ObservableList<String> observableList) {
        index = 1;

        //Top
        this.setTop(new Label(titleText));

        //Center
        listView = new ListView<>(observableList);
        listView.setEditable(true);
        listView.setCellFactory(TextFieldListCell.forListView());
        listView.setPrefHeight(200);
        this.setCenter(listView);

        //Bottom
        HBox bottomItems = new HBox();

        Button btn_add = new Button("Hinzufügen");
        btn_add.setMaxWidth(10000);
        btn_add.setOnAction(e->{
            observableList.add(newText + " " + index++);
        });
        Button btn_remove = new Button("Löschen");
        btn_remove.setMaxWidth(10000);
        btn_remove.setOnAction(e->{
            if(!listView.getSelectionModel().isEmpty()) {
                observableList.remove(listView.getSelectionModel().getSelectedItem());
            }
        });

        bottomItems.getChildren().addAll(btn_add, btn_remove);
        bottomItems.setSpacing(5);
        HBox.setHgrow(btn_add, Priority.ALWAYS);
        HBox.setHgrow(btn_remove, Priority.ALWAYS);
        this.setBottom(bottomItems);
    }
}
