package sample;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomStage extends Stage {
    private VBox container;
    private ImageView back;

    CustomStage() {
        container = new VBox();

        BorderPane topBar = new BorderPane();
        topBar.setId("topBar");
        ImageView smallLogo = new ImageView(new Image("ForcePlateLogo.png"));
        smallLogo.setId("smallLogo");
        smallLogo.setTranslateX(20);
        smallLogo.setTranslateY(10);
        smallLogo.setPreserveRatio(true);
        smallLogo.setFitWidth(130);
        topBar.setLeft(smallLogo);

        //WindowControls
        back = new ImageView(new Image("back.png"));
        back.setPreserveRatio(true);
        back.setFitHeight(20);
        back.setVisible(false);
        ImageView minimize = new ImageView(new Image("minimize.png"));
        minimize.setPreserveRatio(true);
        minimize.setFitHeight(20);
        ImageView close = new ImageView(new Image("close.png"));
        close.setPreserveRatio(true);
        close.setFitHeight(20);
        HBox controls = new HBox(back, minimize, close);
        controls.setSpacing(10);
        topBar.setRight(controls);
        BorderPane.setMargin(topBar.getRight(), new Insets(15,15,0,0));

        //Window Controls Handler
        minimize.setOnMouseClicked(ActionEvent -> this.setIconified(true));
        close.setOnMouseClicked(ActionEvent -> this.close());

        container.getChildren().add(topBar);
        VBox.setVgrow(topBar, Priority.ALWAYS);
        container.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        Scene scene = new Scene(container);
        scene.getStylesheets().add("stylesheet.css");
        this.setScene(scene);
        this.initStyle(StageStyle.UNDECORATED);
    }

    void setScene(Parent content, double width, double height) {
        if(container.getChildren().size() > 1) {
            container.getChildren().remove(container.getChildren().size() - 1);
        }
        container.getChildren().add(content);

        this.setWidth(width);
        this.setHeight(height);
    }

    void enableGoBack(EventHandler<? super MouseEvent> action) {
        back.setVisible(true);

        back.setOnMouseClicked(action);
    }

    void disableGoBack() {
        back.setVisible(false);
    }
}
