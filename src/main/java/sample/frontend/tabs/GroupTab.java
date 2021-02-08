package sample.frontend.tabs;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import sample.*;
import sample.backend.data.Data;
import sample.backend.data.Group;
import sample.backend.Searchables;
import sample.frontend.controls.CustomTableView;
import sample.frontend.controls.SwitchBox;
import sample.frontend.detailviews.GroupDetailView;

public class GroupTab extends BorderPane {
    private final CustomTableView<Group> tbv_groups;

    public GroupTab() {
        //Top
        TextField txt_search = new TextField();
        txt_search.setPromptText("Suche nach Klasse, ...");
        txt_search.textProperty().addListener((observable, oldValue, newValue) -> textChangeListener(newValue));
        this.setTop(txt_search);

        //Center
        tbv_groups = new CustomTableView<>();
        tbv_groups.<String>addColumn("Klasse", new PropertyValueFactory<>("grade"));
        tbv_groups.<Integer>addColumn("Mitglieder", new PropertyValueFactory<>("memberCount"));
        tbv_groups.<StackPane>addColumn("Qualifiziert", param -> new SwitchBox(param.getValue().qualifiedProperty(), "small", false));
        tbv_groups.<Integer>addColumn("Punkte", new PropertyValueFactory<>("points"));
        tbv_groups.getItems().addAll(Data.groups);
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

        Data.groups.addListener(this::groupsListener);

        this.setCenter(tbv_groups);

        BorderPane.setMargin(this.getCenter(), new Insets(10, 0, 0, 0));
        this.setPadding(new Insets(10));
    }

    private void textChangeListener(String newValue) {
        newValue = newValue.trim().toLowerCase();
        tbv_groups.getItems().clear();

        if(newValue.isEmpty()) {
            tbv_groups.getItems().addAll(Data.groups);
            return;
        }

        for(Group group : Data.groups) {
            if(Searchables.contain(newValue, group)) {
                tbv_groups.getItems().add(group);
            }
        }
    }

    private void groupsListener(ListChangeListener.Change<? extends Group> c) {
        while (c.next()) {
            tbv_groups.getItems().addAll(c.getAddedSubList());
            tbv_groups.getItems().removeAll(c.getRemoved());
        }
    }
}
