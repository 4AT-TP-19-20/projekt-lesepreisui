package sample.frontend.detailviews;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import sample.backend.data.Group;
import sample.backend.data.Contestant;
import sample.frontend.controls.CustomTableView;
import sample.frontend.controls.SwitchBox;

public class GroupDetailView extends BorderPane {
    public GroupDetailView(Group group) {
        //Top
        HBox topItems = new HBox();
        topItems.getChildren().add(new Label("Klasse"));
        TextField txt_grade = new TextField(group.getGrade());
        txt_grade.setEditable(false);
        topItems.getChildren().add(txt_grade);
        topItems.getChildren().add(new Label("Qualifiziert"));
        SwitchBox sbx_qualified = new SwitchBox(group.qualifiedProperty(), "small", false);
        topItems.getChildren().add(sbx_qualified);
        topItems.setSpacing(10);
        topItems.setAlignment(Pos.CENTER_LEFT);
        this.setTop(topItems);

        //Center
        CustomTableView<Contestant> tbv_members = new CustomTableView<>();
        tbv_members.<String>addColumn("Vorname", new PropertyValueFactory<>("firstName"));
        tbv_members.<String>addColumn("Nachname", new PropertyValueFactory<>("lastName"));
        tbv_members.<Integer>addColumn("Gelesene Bücher", new PropertyValueFactory<>("bookCount"));
        tbv_members.<Integer>addColumn("Punkte", new PropertyValueFactory<>("points"));
        tbv_members.getItems().addAll(group.getMembers());
        this.setCenter(tbv_members);

        BorderPane.setMargin(this.getCenter(), new Insets(10,0,0,0));
        this.setPadding(new Insets(10));
    }
}
