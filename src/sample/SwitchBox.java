package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SwitchBox extends VBox {
    private StackPane stackPane;
    private IntegerProperty state;
    private int stateCount;
    private BooleanProperty editable;
    private ImageView[] images;

    SwitchBox(String size) {
        this(2, size, true, true);
    }

    SwitchBox(IntegerProperty stateProperty, String size, boolean editable, boolean canBeEmpty) {
        this(stateProperty.get(), size, editable, canBeEmpty);
        state.bindBidirectional(stateProperty);
    }

    SwitchBox(int initialState, String size, boolean editable, boolean canBeEmpty) {
        int height = size.equals("small") ? 15 : 50;

        images = new ImageView[3];
        images[0] = new ImageView("file:no.png");
        images[0].setFitHeight(height);
        images[0].setPreserveRatio(true);
        images[1] = new ImageView("file:yes.png");
        images[1].setFitHeight(height);
        images[1].setPreserveRatio(true);
        images[2] = new ImageView("file:empty.png");
        images[2].setFitHeight(height);
        images[2].setPreserveRatio(true);

        stackPane = new StackPane(images);
        this.state = new SimpleIntegerProperty(initialState);
        state.addListener((observable, oldValue, newValue) -> updateImages());
        this.editable = new SimpleBooleanProperty(editable);
        stackPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(size.equals("small") ? 1 : 3))));
        stackPane.setOnMouseClicked(e -> mouseAction());
        setCanBeEmpty(canBeEmpty);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(stackPane);

        updateImages();
    }

    public int getState() {
        return state.get();
    }

    public IntegerProperty stateProperty() {
        return state;
    }

    public void setState(int state) {
        if(state >= 0 && state <= stateCount) {
            this.state.set(state);
        }
    }

    public boolean getEditable() {
        return editable.get();
    }

    public BooleanProperty editableProperty() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable.set(editable);
    }

    public boolean canBeEmpty() {
        return stateCount == 3;
    }

    public void setCanBeEmpty(boolean canBeEmpty) {
        stateCount = canBeEmpty ? 3 : 2;
    }

    private void nextState() {
        state.set((state.get() + 1) % stateCount);
    }

    private void updateImages() {
        for(int i = 0; i < images.length; i++) {
            images[i].setVisible(i == state.get());
        }
    }

    private void mouseAction() {
        if(editable.get()) {
            nextState();
        }
    }
}
