package sample.frontend;

import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import sample.*;
import sample.frontend.controls.ContentStack;

public class ButtonController {
    private static Button btn_back;

    public static void init(TabPane tabPane) {
        btn_back = new Button("Zurück");
        btn_back.setMinWidth(100);
        btn_back.setOnAction(e -> Main.getCurrentContentStack().pop());
        btn_back.managedProperty().bind(btn_back.visibleProperty());

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue != null) {
                ((ContentStack) oldValue.getContent()).onDeselected();
                ((ContentStack) newValue.getContent()).onSelected();
            }
        });
    }

    //TODO alignment
    public static Button getButton() {
        btn_back.setTranslateX(-5);
        btn_back.setTranslateY(4);

        //Shouldn't be necessary
        //buttons.setPickOnBounds(false);

        disableBack();

        return btn_back;
    }

    public static void enableBack() {
        btn_back.setVisible(true);
    }

    public static void disableBack() {
        btn_back.setVisible(false);
    }
}