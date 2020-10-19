package sample;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

class ContentStack extends StackPane {
    ContentStack(Node initialContent) {
        this.getChildren().add(initialContent);

        open(initialContent);
    }

    void push(Node content) {
        close(getLast());

        this.getChildren().add(content);

        open(content);
    }

    void pop() {
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

    private Node getLast() {
        if(this.getChildren().size() > 0) {
            return this.getChildren().get(this.getChildren().size() - 1);
        }
        else {
            throw new RuntimeException("ContentStack is empty!");
        }
    }

    private void open(Node toOpen) {
        if(toOpen instanceof ChildSaveable) {
            ButtonController.enableSaveDiscardSystem(((ChildSaveable) toOpen).getSaveable());
        }

        if(this.getChildren().size() > 1) {
            ButtonController.enableBack();
        }

        toOpen.setVisible(true);
    }

    private void close(Node toClose) {
        ButtonController.disableButtons();

        toClose.setVisible(false);
    }
}
