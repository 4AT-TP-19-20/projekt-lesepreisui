package sample;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

class CustomStage extends Stage {
    CustomStage() {
        this.getIcons().add(new Image("icon.png"));
    }

    void setScene(Parent content, double width, double height) {
            Scene scene = new Scene(content, width, height);
            scene.getStylesheets().add("stylesheet.css");
            this.setScene(scene);
    }

    void enableBlocking() {
        initModality(Modality.APPLICATION_MODAL);
    }
}
