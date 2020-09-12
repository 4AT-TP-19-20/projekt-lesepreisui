package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.Map;

public class GroupTab extends BorderPane implements TabContent {
    private CustomTableView<Map.Entry<String, Group>> tbv_groups;

    GroupTab() {
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
        tbv_groups.setOnMouseClicked((MouseEvent event) -> {
            if(event.getTarget() instanceof TableColumnHeader) {
                tbv_groups.getSelectionModel().clearSelection();
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                if(tbv_groups.getSelectionModel().getSelectedItem() != null) {
                    Main.getCurrentContentStack().push(new GroupDetailView(tbv_groups.getSelectionModel().getSelectedItem()));
                }
            }
        });
        this.setCenter(tbv_groups);

        BorderPane.setMargin(this.getCenter(), new Insets(10, 0, 0, 0));
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

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {

    }
}
