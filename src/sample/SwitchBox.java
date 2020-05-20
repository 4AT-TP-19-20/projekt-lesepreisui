package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SwitchBox extends StackPane implements ObservableValue<StackPane> {
    private IntegerProperty state;
    private int stateCount;
    private BooleanProperty editable;
    private ImageView[] images;

    SwitchBox(String size) {
        this(2, size, true, true);
    }

    SwitchBox(BooleanBinding binding, String size, boolean editable, boolean canBeEmpty) {
        this(binding.get() ? 1 : 0, size, editable, canBeEmpty);
        binding.addListener(e -> state.set(binding.get() ? 1 : 0));
    }

    SwitchBox(IntegerProperty stateProperty, String size, boolean editable, boolean canBeEmpty) {
        this(stateProperty.get(), size, editable, canBeEmpty);
        state.bindBidirectional(stateProperty);
    }

    SwitchBox(int initialState, String sizeMode, boolean editable, boolean canBeEmpty) {
        super();
        int size = sizeMode.equals("small") ? 15 : 50;

        images = new ImageView[3];
        images[0] = new ImageView("file:no.png");
        images[0].setFitHeight(size);
        images[0].setPreserveRatio(true);
        images[1] = new ImageView("file:yes.png");
        images[1].setFitHeight(size);
        images[1].setPreserveRatio(true);
        images[2] = new ImageView("file:empty.png");
        images[2].setFitHeight(size);
        images[2].setPreserveRatio(true);

        this.state = new SimpleIntegerProperty(initialState);
        state.addListener((observable, oldValue, newValue) -> updateImages());
        this.editable = new SimpleBooleanProperty(editable);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(sizeMode.equals("small") ? 1 : 3))));
        this.setMaxSize(size, size);
        this.setOnMouseClicked(e -> mouseAction());
        setCanBeEmpty(canBeEmpty);
        this.getChildren().addAll(images);

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

    @Override
    public void addListener(ChangeListener<? super StackPane> listener) {

    }

    @Override
    public void removeListener(ChangeListener<? super StackPane> listener) {

    }

    @Override
    public StackPane getValue() {
        return this;
    }

    @Override
    public void addListener(InvalidationListener listener) {

    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }
}