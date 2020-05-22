package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.Map;

public class GroupTab extends BorderPane {
    private CustomTableView<Map.Entry<String, Group>> tbv_groups;

    public GroupTab() {
        //Top
        TextField txt_search = new TextField();
        txt_search.setPromptText("Suche nach Klasse, ...");
        txt_search.textProperty().addListener((observable, oldValue, newValue) -> textChangeListener(newValue));
        this.setTop(txt_search);

        //Center
        tbv_groups = new CustomTableView<>();
        tbv_groups.addColumn("Klasse", "", param -> new SimpleStringProperty(param.getValue().getKey()));
        tbv_groups.addColumn("Mitglieder", "", param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getMemberCount())));
        tbv_groups.addColumn("Qualifiziert", new StackPane(), param -> new SwitchBox(param.getValue().getValue().qualifiedProperty(), "small", false));
        tbv_groups.addColumn("Punkte", "", param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getPoints())));
        tbv_groups.getItems().addAll(Data.groups.entrySet());
        this.setCenter(tbv_groups);

        BorderPane.setMargin(this.getCenter(), new Insets(10, 0, 10, 0));
        this.setPadding(new Insets(10));
    }

    private void textChangeListener(String newValue) {
        newValue = newValue.trim().toLowerCase();
        tbv_groups.getItems().clear();

        if(newValue.trim().isEmpty()) {
            tbv_groups.getItems().addAll(Data.groups.entrySet());
            return;
        }

        for(Map.Entry<String, Group> entry : Data.groups.entrySet()) {
            if(entry.getKey().toLowerCase().contains(newValue)) {
                tbv_groups.getItems().add(entry);
            }
        }
    }
}