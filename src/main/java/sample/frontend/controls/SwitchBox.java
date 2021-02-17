package sample.frontend.controls;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import sample.utils.BooleanIntegerBinding;

public class SwitchBox extends StackPane implements ObservableValue<StackPane> {
    private static final ColorAdjust disabledEffect = new ColorAdjust(0, 0, -0.1, 0);
    private final IntegerProperty state;
    private final BooleanProperty editable;
    private final ImageView[] images;
    private int stateCount;

    public SwitchBox(String size) {
        this(2, size, true, true);
    }

    public SwitchBox(BooleanProperty booleanState, String size, boolean editable) {
        this(booleanState.get() ? 1 : 0, size, editable, false);
        new BooleanIntegerBinding(state, booleanState);
    }

    public SwitchBox(IntegerProperty stateProperty, String size, boolean editable, boolean canBeEmpty) {
        this(stateProperty.get(), size, editable, canBeEmpty);
        state.bindBidirectional(stateProperty);
    }

    SwitchBox(int initialState, String sizeMode, boolean editable, boolean canBeEmpty) {
        super();
        int size = sizeMode.equals("small") ? 15 : 50;

        images = new ImageView[3];
        images[0] = new ImageView(new Image(getClass().getResourceAsStream("/images/switchbox/no.png")));
        images[0].setFitHeight(size);
        images[0].setPreserveRatio(true);
        images[1] = new ImageView(new Image(getClass().getResourceAsStream("/images/switchbox/yes.png")));
        images[1].setFitHeight(size);
        images[1].setPreserveRatio(true);
        images[2] = new ImageView(new Image(getClass().getResourceAsStream("/images/switchbox/empty.png")));
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
        disabledProperty().addListener((observable, oldValue, newValue) -> {
            Effect newEffect = newValue ? SwitchBox.disabledEffect : null;
            for(ImageView imageView : images) {
                imageView.setEffect(newEffect);
            }
        });

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