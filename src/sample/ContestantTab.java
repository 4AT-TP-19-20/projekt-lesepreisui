package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class ContestantTab extends BorderPane {
    private CustomTableView<Contestant> tbv_contestants;
    private Contestant selected;
    private Contestant old;

    public ContestantTab(Tab parent) {
        tbv_contestants = new CustomTableView<>();

        //Top
        TextField txt_search = new TextField();
        txt_search.setPromptText("Suche nach Name, Klasse, ...");
        txt_search.setMaxWidth(10000);
        txt_search.textProperty().addListener((observable, oldValue, newValue) -> textChangeListener(newValue));
        this.setTop(txt_search);

        //Center
        tbv_contestants.addColumn("Vorname", "", new PropertyValueFactory<>("firstName"));
        tbv_contestants.addColumn("Nachname", "", new PropertyValueFactory<>("lastName"));
        tbv_contestants.addColumn("Klasse", "", new PropertyValueFactory<>("grade"));
        tbv_contestants.addColumn("Gelesene Bücher", 0, new PropertyValueFactory<>("bookCount"));
        tbv_contestants.addColumn("Punkte", 0, new PropertyValueFactory<>("points"));
        tbv_contestants.addColumn("Qualifiziert", new StackPane(), param -> new SwitchBox(param.getValue().qualifiedProperty(), "small", false));

        tbv_contestants.getItems().addAll(Data.contestants);
        tbv_contestants.setOnMouseClicked((MouseEvent event) -> {
            if(event.getTarget() instanceof TableColumnHeader) {
                tbv_contestants.getSelectionModel().clearSelection();
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                selected = tbv_contestants.getSelectionModel().getSelectedItem();
                if(selected != null) {
                    parent.setContent(new ContestantDetailView(selected, parent));
                    old = selected.getCopy();
                    Main.enableBack(e -> {
                        if(!selected.equals(old)) {
                            SaveAlert saveAlert = new SaveAlert();
                            Optional<ButtonType> picked = saveAlert.showAndWait();

                            if(picked.isPresent()) {
                                switch (picked.get().getButtonData()) {
                                    case YES:
                                        old.setValues(selected);
                                    break;
                                    case NO:
                                        selected.setValues(old);
                                    break;
                                    case CANCEL_CLOSE:
                                        return;
                                }
                            }
                        }
                        parent.setContent(new ContestantTab(parent));
                        Main.disableButtons();
                    });
                    Main.enableSaveDiscard(save -> old.setValues(selected), discard -> selected.setValues(old));
                }
            }
        });
        this.setCenter(tbv_contestants);

        //Bottom
        VBox bottomItems = new VBox();
        Button btn_addContestant = new Button("Neuer Teilnehmer");
        btn_addContestant.setId("custom-button");
        btn_addContestant.setOnAction(e->{
            Contestant toAdd = new Contestant();
            Data.contestants.add(toAdd);
            textChangeListener(txt_search.getText());
            parent.setContent(new ContestantDetailView(toAdd, parent));
            Main.enableBack(ee -> {
                parent.setContent(new ContestantTab(parent));
                Main.disableButtons();
            });
        });
        Button btn_removeContestant = new Button("Teilnehmer löschen");
        btn_removeContestant.setId("red-button");
        btn_removeContestant.setOnAction(e->{
            Contestant selected = tbv_contestants.getSelectionModel().getSelectedItem();
            if(selected != null) {
                Data.contestants.remove(selected);
                textChangeListener(txt_search.getText());
            }
        });
        bottomItems.getChildren().addAll(btn_addContestant, btn_removeContestant);
        bottomItems.setSpacing(5);
        this.setBottom(bottomItems);

        BorderPane.setMargin(this.getCenter(), new Insets(10, 0, 10, 0));
        this.setPadding(new Insets(10));
    }

    private void textChangeListener(String newValue) {
        newValue = newValue.trim().toLowerCase();
        tbv_contestants.getItems().clear();

        if(newValue.trim().isEmpty()) {
            tbv_contestants.getItems().addAll(Data.contestants);
            return;
        }

        for(Contestant contestant : Data.contestants) {
            if(contestant.getFirstName().toLowerCase().contains(newValue)
                    || contestant.getLastName().toLowerCase().contains(newValue)
                    || contestant.getGrade().toLowerCase().contains(newValue)) {
                tbv_contestants.getItems().add(contestant);
            }
        }
    }
}
