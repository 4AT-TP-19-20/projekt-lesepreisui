package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        TabPane root = new TabPane();
        Data.init();
        root.getTabs().add(new Tab("Teilnehmer", ContestantTab.generate()));
        root.getTabs().add(new Tab("Prüfungen"));
        root.getTabs().add(new Tab("Bücher", BookTab.generate()));
        root.getTabs().add(new Tab("Verlosung", DrawingTab.generate()));
        root.getTabs().add(new Tab("Einstellungen"));
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);


        primaryStage.setTitle("LesePreisUI");
        //Get Screen Height
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        //Loading Screen
        StackPane loadingPane = loadingScreen();
        Scene loading = new Scene(loadingPane, 1280, screenBounds.getHeight()-25);
        primaryStage.setScene(loading);
        primaryStage.show();

        //Link CSS Stylesheet
        loading.getStylesheets().add("stylesheet.css");

        //Button
        Button startBtn = new Button("LesePreisUI starten");
        startBtn.setId("drawing-button");
        startBtn.setTranslateY(350);

        //Button FadeIN
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(4));
        fadeIn.setNode(startBtn);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setAutoReverse(false);
        fadeIn.setCycleCount(1);
        fadeIn.play();
        loadingPane.getChildren().add(startBtn);
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Main Screen
                Scene main = new Scene(root, 1280, screenBounds.getHeight()-25);
                main.getStylesheets().add("stylesheet.css");
                primaryStage.setScene(main);
                root.requestFocus();
            }
        });

    }

    public static ProgressBar bar = new ProgressBar();
    public StackPane loadingScreen() throws InterruptedException {
        StackPane loadingPane = new StackPane();
        loadingPane.setId("loadingPane");
        //Logo
        ImageView logo = new ImageView(new Image("ForcePlateLogo.png"));
        logo.setFitWidth(900);
        logo.setPreserveRatio(true);

        //Logo FadeIN
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(3));
        fadeIn.setNode(logo);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setAutoReverse(false);
        fadeIn.setCycleCount(1);
        fadeIn.play();
        loadingPane.getChildren().add(logo);



        //ProgressBar
        bar.setProgress(0);
        bar.setStyle("-fx-accent: rgb(0,125,67)");
        bar.setId("progressBar");
        bar.setTranslateY(150);
        loadingPane.getChildren().add(bar);


        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                return null;
            }
        };
        bar.progressProperty().bind(sleeper.progressProperty());

        return loadingPane;
    }



    public static void main(String[] args) {
        launch(args);
    }
}
