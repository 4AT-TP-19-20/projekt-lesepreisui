package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

class SaveAlert extends Alert {
    SaveAlert() {
        super(Alert.AlertType.CONFIRMATION, "Es gibt ungespeicherte Änderungen, sollen sie gespeichert werden?");
        this.setHeaderText("");
        this.setTitle("Ungespeicherte Änderungen");
        this.setGraphic(null);
        ButtonType buttonSave = new ButtonType("Speichern", ButtonBar.ButtonData.YES);
        ButtonType buttonDiscard = new ButtonType("Verwerfen", ButtonBar.ButtonData.NO);
        ButtonType buttonCancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
        this.getButtonTypes().clear();
        this.getButtonTypes().addAll(buttonSave, buttonDiscard, buttonCancel);
    }
}
