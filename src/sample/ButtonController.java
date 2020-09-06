package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.concurrent.Callable;

class ButtonController {
    private static boolean isInitialized = false;
    private static Button btn_back;
    private static Button btn_save;
    private static Button btn_discard;
    private static Callable<Boolean> canLeave;

    private static void init() {
        btn_back = new Button("Zurück");
        btn_back.setMinWidth(100);
        btn_back.managedProperty().bind(btn_back.visibleProperty());
        btn_save = new Button("Änderungen speichern");
        btn_discard = new Button("Änderungen verwerfen");

        isInitialized = true;
    }

    static HBox getButtons() {
        if(!isInitialized) {
            init();
        }

        HBox buttons = new HBox(5);

        buttons.setTranslateX(-5);
        buttons.setTranslateY(4);
        buttons.getChildren().addAll(btn_save, btn_discard, btn_back);
        buttons.setAlignment(Pos.TOP_RIGHT);
        buttons.setPickOnBounds(false);

        disableButtons();

        return buttons;
    }

    static void enableBack(EventHandler<ActionEvent> action) {
        btn_back.setOnAction(action);
        btn_back.setVisible(true);
    }

    static void enableSaveDiscardSystem(EventHandler<ActionEvent> saveAction,
                                            EventHandler<ActionEvent> discardAction,
                                            Callable<Boolean> canLeave,
                                            boolean hasBackButton) {
        btn_save.setOnAction(saveAction);
        btn_save.setVisible(true);
        btn_discard.setOnAction(discardAction);
        btn_discard.setVisible(true);
        if(hasBackButton) {
            btn_back.setOnAction(e -> {
                try {
                    canLeave.call();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            btn_back.setVisible(true);
        }

        ButtonController.canLeave = canLeave;
    }

    static void disableButtons() {
        btn_back.setVisible(false);
        btn_save.setVisible(false);
        btn_discard.setVisible(false);
        canLeave = () -> true;
    }

    static boolean canLeave() {
        try {
            return canLeave.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}