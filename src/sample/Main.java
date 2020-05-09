package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    public void start(Stage primaryStage) {
        base = new BorderPane();
        TabPane root = new TabPane();
        Data.init();
        Tab tab_contestants = new Tab("Teilnehmer");
        tab_contestants.setContent(new ContestantTab(tab_contestants));
        tab_contestants.setOnSelectionChanged(e -> tab_contestants.setContent(new ContestantTab(tab_contestants)));
        root.getTabs().add(tab_contestants);
        Tab tab_exams = new Tab("Prüfungen", new ExamTab());
        tab_exams.setOnSelectionChanged(e -> tab_exams.setContent(new ExamTab()));
        root.getTabs().add(tab_exams);
        Tab tab_books = new Tab("Bücher", new BookTab());
        tab_books.setOnSelectionChanged(e -> tab_books.setContent(new BookTab()));
        root.getTabs().add(tab_books);
        Tab tab_drawing = new Tab("Verlosung", new DrawingTab());
        tab_drawing.setOnSelectionChanged(e -> tab_drawing.setContent(new DrawingTab()));
        root.getTabs().add(tab_drawing);
        Tab tab_settings = new Tab("Einstellungen", new SettingsTab());
        tab_settings.setOnSelectionChanged(e -> tab_settings.setContent(new SettingsTab()));
        root.getTabs().add(tab_settings);
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        base = generateTopBar(base, root, primaryStage);

        //CENTER
        base.setCenter(root);
        primaryStage.setTitle("LesePreisUI");

        //Get Screen Height
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene main = new Scene(base, 1280, screenBounds.getHeight());

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
            main.getStylesheets().add("stylesheet.css");
            primaryStage.setScene(main);
            root.requestFocus();

            //Login Pane
            Stage loginStage = new Stage();
            HBox loginItems = new HBox();
            ComboBox<String> cbx_user = new ComboBox<>(SettingsTab.getUsers());
            Button btn_login = new Button("Login");
            btn_login.setOnAction(e->{
                if(!cbx_user.getSelectionModel().isEmpty()) {
                    Data.currentUser = cbx_user.getSelectionModel().getSelectedItem();
                    base.setCenter(root);
                    loginStage.hide();
                }
            });
            loginItems.getChildren().addAll(cbx_user, btn_login);
            loginItems.setSpacing(10);
            loginStage.setScene(new Scene(loginItems,160,30));
            loginStage.show();
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
            Data.save();
            primaryStage.close();
        });
        minimize.setOnMouseClicked(ActionEvent->{
            primaryStage.setIconified(true);
        });

        return base;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
