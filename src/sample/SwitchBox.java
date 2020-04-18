package sample;

import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SwitchBox extends StackPane {
    private int state;
    private boolean enabled;
    private ImageView[] images;

    SwitchBox(int state) {
        enabled = true;

        images = new ImageView[3];
        images[0] = new ImageView("file:empty.png");
        images[1] = new ImageView("file:yes.png");
        images[2] = new ImageView("file:no.png");

        this.state = state;
        updateImages();

        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
        this.getChildren().addAll(images);
        this.setOnMouseClicked(e -> {
            if(enabled) {
                nextState();
            }
        });
    }

    SwitchBox() {
        this(0);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;

        updateImages();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void nextState() {
        state = (state + 1) % 3;
        updateImages();
    }

    private void updateImages() {
        for(int i = 0; i < images.length; i++) {
            images[i].setVisible(i == state);
        }
    }
}
