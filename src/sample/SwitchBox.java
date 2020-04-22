package sample;

import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SwitchBox extends StackPane {
    private int state;
    private int stateCount;
    private boolean enabled;
    private ImageView[] images;

    SwitchBox(String size) {
        this(0, size, true, true);
    }

    SwitchBox(int initialState, String size, boolean enabled, boolean canBeEmpty) {
        this.enabled = enabled;
        int height = size.equals("small") ? 15 : 50;
        setCanBeEmpty(canBeEmpty);

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

        this.state = initialState;
        updateImages();

        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(size.equals("small") ? 1 : 3))));
        this.getChildren().addAll(images);
        this.setOnMouseClicked(e -> {
            if(enabled) {
                nextState();
            }
        });
    }

    public void nextState() {
        state = (state + 1) % stateCount;
        updateImages();
    }

    private void updateImages() {
        for(int i = 0; i < images.length; i++) {
            images[i].setVisible(i == state);
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if(state >= 0 && state <= stateCount) {
            this.state = state;
            updateImages();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean canBeEmpty() {
        return stateCount == 3;
    }

    public void setCanBeEmpty(boolean canBeEmpty) {
        stateCount = canBeEmpty ? 3 : 2;
    }
}
