package sample;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

class ContentStack extends StackPane {
    <T extends Node & TabContent> ContentStack(T initialContent) {
        this.getChildren().add(initialContent);

        open(initialContent);
    }

    <T extends Node & TabContent> void push(T content) {
        close(getLast());

        this.getChildren().add(content);

        open(content);
    }

    <T extends Node & TabContent> void pop() {
        close(getLast());

        this.getChildren().remove(this.getChildren().size() - 1);

        open(getLast());
    }

    void onSelected() {
        open(getLast());
    }

    void onDeselected() {
        close(getLast());
    }

    @SuppressWarnings("unchecked")
    private <T extends Node & TabContent> T getLast() {
        if(this.getChildren().size() > 0) {
            return (T) this.getChildren().get(this.getChildren().size() - 1);
        }
        else {
            throw new RuntimeException("ContentStack is empty!");
        }
    }

    private <T extends Node & TabContent> void open(T toOpen) {
        toOpen.onOpen();

        if(toOpen instanceof ChildSaveable) {
            ButtonController.enableSaveDiscardSystem(((ChildSaveable) toOpen).getSaveable());
        }

        if(this.getChildren().size() > 1) {
            ButtonController.enableBack();
        }

        toOpen.setVisible(true);
    }

    private <T extends Node & TabContent> void close(T toClose) {
        toClose.onClose();

        ButtonController.disableButtons();

        toClose.setVisible(false);
    }
}
