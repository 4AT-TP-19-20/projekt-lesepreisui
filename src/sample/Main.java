package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {

    public static BorderPane base;

    @Override
    public void start(Stage primaryStage) throws Exception {
        base = new BorderPane();
        TabPane root = new TabPane();
        Data.init();
        root.getTabs().add(new Tab("Teilnehmer", ContestantTab.generate()));
        root.getTabs().add(new Tab("Prüfungen"));
        root.getTabs().add(new Tab("Bücher", BookTab.generate()));
        root.getTabs().add(new Tab("Verlosung", DrawingTab.generate()));
        root.getTabs().add(new Tab("Einstellungen", SettingsTab.generate()));
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        base = generateTopBar(base, root, primaryStage);

        //CENTER
        base.setCenter(root);
        primaryStage.setTitle("LesePreisUI");

        //Get Screen Height
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        //Loading Screen
        StackPane loadingPane = loadingScreen();
        Scene loading = new Scene(loadingPane, 1280, screenBounds.getHeight());
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(loading);
        primaryStage.show();

        //Link CSS Stylesheet
        loading.getStylesheets().add("stylesheet.css");

        //Button
        Button startBtn = new Button("LesePreisUI starten");
        startBtn.setId("drawing-button");
        startBtn.setTranslateY(350);

        //Button FadeIN
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(3));
        fadeIn.setNode(startBtn);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setAutoReverse(false);
        fadeIn.setCycleCount(1);
        fadeIn.play();
        loadingPane.getChildren().add(startBtn);
        startBtn.setOnAction(actionEvent -> {
            //Main Screen
            Scene main = new Scene(base, 1280, screenBounds.getHeight() - 25);
            main.getStylesheets().add("stylesheet.css");
            primaryStage.setScene(main);
            root.requestFocus();
        });

    }

    public static ProgressBar bar = new ProgressBar();

    public StackPane loadingScreen() {
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

    public BorderPane generateTopBar(BorderPane base, TabPane root, Stage primaryStage){
        //TOP BAR
        HBox topBar = new HBox();
        topBar.setId("topBar");
        ImageView smallLogo = new ImageView(new Image("ForcePlateLogo.png"));
        smallLogo.setId("smallLogo");
        smallLogo.setTranslateX(20);
        smallLogo.setTranslateY(10);
        smallLogo.setPreserveRatio(true);
        smallLogo.setFitWidth(130);
        topBar.getChildren().add(smallLogo);
        base.setTop(topBar);

        //WindowControls
        ImageView minimize = new ImageView(new Image("minimize.png"));
        minimize.setPreserveRatio(true);
        minimize.setFitHeight(20);
        ImageView close = new ImageView(new Image("close.png"));
        close.setPreserveRatio(true);
        close.setFitHeight(20);
        close.setTranslateX(30);
        Group controls = new Group(close, minimize);
        controls.setTranslateX(1080);
        controls.setTranslateY(15);
        topBar.getChildren().add(controls);


        //Window Controls Handler
            //On Click
        close.setOnMouseClicked(ActionEvent->{
            primaryStage.close();
        });
        minimize.setOnMouseClicked(ActionEvent->{
            primaryStage.setIconified(true);
        });
            //On Hover
        close.setOnMouseEntered(ActionEvent ->{
            close.setScaleX(1.2);
            close.setScaleY(1.2);
        });
        close.setOnMouseExited(ActionEvent ->{
            close.setScaleX(1);
            close.setScaleY(1);
        });
        minimize.setOnMouseEntered(ActionEvent ->{
            minimize.setScaleX(1.2);
            minimize.setScaleY(1.2);
        });
        minimize.setOnMouseExited(ActionEvent ->{
            minimize.setScaleX(1);
            minimize.setScaleY(1);
        });



        return base;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
