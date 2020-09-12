package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.Optional;

class ButtonController {
    private static Button btn_back;
    private static Button btn_save;
    private static Button btn_discard;
    private static Callable<Boolean> canLeave;

    static void init(TabPane tabPane) {
        btn_back = new Button("Zurück");
        btn_back.setMinWidth(100);
        btn_back.setOnAction(e -> {
            if(canLeave.call()) {
                Main.getCurrentContentStack().pop();
            }
        });
        btn_back.managedProperty().bind(btn_back.visibleProperty());
        btn_save = new Button("Änderungen speichern");
        btn_discard = new Button("Änderungen verwerfen");

        tabPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if(event.getTarget().getClass().getName().contains("TabPaneSkin")
            || (event.getTarget() instanceof Text && event.getY() <= 33)) { //To make sure the text is in the tab header
                if(!canLeave.call()) {
                    event.consume();
                }
            }
        });
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue != null) {
                ((ContentStack) oldValue.getContent()).onDeselected();
                ((ContentStack) newValue.getContent()).onSelected();
            }
        });
    }

    static HBox getButtons() {
        HBox buttons = new HBox(5);

        buttons.setTranslateX(-5);
        buttons.setTranslateY(4);
        buttons.getChildren().addAll(btn_save, btn_discard, btn_back);
        buttons.setAlignment(Pos.TOP_RIGHT);
        buttons.setPickOnBounds(false);

        disableButtons();

        return buttons;
    }

    static void enableBack() {
        btn_back.setVisible(true);
    }

    static void enableSaveDiscardSystem(Saveable saveable) {
        Saveable saveableCopy = saveable.getCopy();

        btn_save.setOnAction(e -> saveableCopy.setValues(saveable));
        btn_save.setVisible(true);
        btn_discard.setOnAction(e -> saveable.setValues(saveableCopy));
        btn_discard.setVisible(true);

        canLeave = () -> {
            if(!saveable.equals(saveableCopy)) {
                SaveAlert saveAlert = new SaveAlert();
                Optional<ButtonType> picked = saveAlert.showAndWait();

                if(picked.isPresent()) {
                    switch (picked.get().getButtonData()) {
                        case YES:
                            saveableCopy.setValues(saveable);
                            break;
                        case NO:
                            saveable.setValues(saveableCopy);
                            break;
                        case CANCEL_CLOSE:
                            return false;
                    }
                }
            }
            return true;
        };
    }

    static void disableButtons() {
        btn_back.setVisible(false);
        btn_save.setVisible(false);
        btn_discard.setVisible(false);
        canLeave = () -> true;
    }
}