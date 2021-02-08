package sample.frontend.controls;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SaveAlert extends Alert {
    public SaveAlert() {
        super(Alert.AlertType.CONFIRMATION, "Es gibt ungespeicherte Änderungen, sollen sie gespeichert werden?");
        this.setHeaderText("");
        this.setTitle("Ungespeicherte Änderungen");
        this.setGraphic(null);
        ((Stage) this.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("images/icon.png")));
        ButtonType buttonSave = new ButtonType("Speichern", ButtonBar.ButtonData.YES);
        ButtonType buttonDiscard = new ButtonType("Verwerfen", ButtonBar.ButtonData.NO);
        ButtonType buttonCancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
        this.getButtonTypes().clear();
        this.getButtonTypes().addAll(buttonSave, buttonDiscard, buttonCancel);
    }
}
