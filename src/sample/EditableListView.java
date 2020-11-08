package sample;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

class EditableListView extends BorderPane {
    private ListView<String> listView;
    private int index;

    EditableListView(String titleText, String newText, ObservableList<String> observableList, String iconPath) {
        index = 1;
        ColorAdjust hoverEffect = new ColorAdjust(0, 0, 0.3, 0);

        //Top
        ImageView titleIcon = new ImageView(new Image(iconPath));
        titleIcon.setFitWidth(20);
        titleIcon.setPreserveRatio(true);
        HBox titleBox = new HBox(titleIcon, new Label(titleText));
        titleBox.setSpacing(10);
        this.setTop(titleBox);

        //Center
        listView = new ListView<>(observableList);
        listView.setEditable(true);
        listView.setCellFactory(TextFieldListCell.forListView());
        listView.setPrefHeight(200);
        this.setCenter(listView);

        //Bottom
        HBox bottomItems = new HBox();
        ImageView btn_add = new ImageView(new Image("icons8-add-100.png"));
        btn_add.setFitWidth(25);
        btn_add.setPreserveRatio(true);
        btn_add.setOnMouseClicked(e -> observableList.add(newText + " " + index++));
        btn_add.setOnMouseEntered(e -> btn_add.setEffect(hoverEffect));
        btn_add.setOnMouseExited(e -> btn_add.setEffect(null));
        ImageView btn_remove = new ImageView(new Image("icons8-minus-100.png"));
        btn_remove.setFitWidth(25);
        btn_remove.setPreserveRatio(true);
        btn_remove.setOnMouseClicked(e -> {
            if(!listView.getSelectionModel().isEmpty()) {
                observableList.remove(listView.getSelectionModel().getSelectedItem());
            }
        });
        btn_remove.setOnMouseEntered(e -> btn_remove.setEffect(hoverEffect));
        btn_remove.setOnMouseExited(e -> btn_remove.setEffect(null));

        bottomItems.setAlignment(Pos.CENTER);
        bottomItems.setPadding(new Insets(10));
        bottomItems.getChildren().addAll(btn_add, btn_remove);
        bottomItems.setSpacing(5);
        HBox.setHgrow(btn_add, Priority.ALWAYS);
        HBox.setHgrow(btn_remove, Priority.ALWAYS);
        this.setBottom(bottomItems);
    }
}
