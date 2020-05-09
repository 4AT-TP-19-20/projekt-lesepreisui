package sample;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomStage extends Stage {
    private VBox container;

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
        ImageView minimize = new ImageView(new Image("minimize.png"));
        minimize.setPreserveRatio(true);
        minimize.setFitHeight(20);
        ImageView close = new ImageView(new Image("close.png"));
        close.setPreserveRatio(true);
        close.setFitHeight(20);
        close.setTranslateX(30);
        Group controls = new Group(close, minimize);
        topBar.setRight(controls);
        BorderPane.setMargin(topBar.getRight(), new Insets(15,15,0,0));

        //Window Controls Handler
        close.setOnMouseClicked(ActionEvent-> this.close());
        minimize.setOnMouseClicked(ActionEvent-> this.setIconified(true));

        container.getChildren().add(topBar);
        container.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        this.initStyle(StageStyle.UNDECORATED);
    }

    void setScene(Pane content, int width, int height) {
        container.getChildren().add(content);
        Scene scene = new Scene(container, width, height);
        scene.getStylesheets().add("stylesheet.css");
        this.setScene(scene);
    }
}
