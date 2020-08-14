package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    @Override
    public void start(Stage unused) {
        CustomStage primaryStage = new CustomStage();
        primaryStage.setTitle("LesePreisUI");
        TabPane root = new TabPane();
        Data.init();
        initializeTabs(root, primaryStage);

        //Get Screen Height
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        //Loading Screen
        VBox loadingPane = loadingScreen();
        primaryStage.setScene(loadingPane, 1280, screenBounds.getHeight() - 30);
        primaryStage.show();

        //Button
        Button startBtn = new Button("LesePreisUI starten");
        startBtn.setId("drawing-button");

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
            primaryStage.setScene(root, 1280, screenBounds.getHeight() - 30);
            root.requestFocus();

            //Login Pane
            Stage loginStage = new Stage();
            HBox loginItems = new HBox();
            ComboBox<String> cbx_user = new ComboBox<>(SettingsTab.getUsers());
            Button btn_login = new Button("Login");
            btn_login.setOnAction(e->{
                if(!cbx_user.getSelectionModel().isEmpty()) {
                    Data.currentUser = cbx_user.getSelectionModel().getSelectedItem();
                    loginStage.hide();
                }
            });
            loginItems.getChildren().addAll(cbx_user, btn_login);
            loginItems.setSpacing(10);
            loginStage.setScene(new Scene(loginItems,160,30));
            loginStage.show();
        });
    }

    private VBox loadingScreen() {
        VBox loadingPane = new VBox();
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
        ProgressBar bar = new ProgressBar();
        bar.setProgress(0);
        bar.setStyle("-fx-accent: rgb(0,125,67)");
        bar.setId("progressBar");
        loadingPane.getChildren().add(bar);

        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                return null;
            }
        };
        bar.progressProperty().bind(sleeper.progressProperty());

        loadingPane.setSpacing(200);
        loadingPane.setAlignment(Pos.CENTER);
        loadingPane.setPadding(new Insets(100));
        return loadingPane;
    }

    private static void initializeTabs(TabPane parent, Stage stage) {
        Tab tab_contestants = new Tab("Teilnehmer");
        tab_contestants.setContent(new ContestantTab(tab_contestants, stage));
        tab_contestants.setOnSelectionChanged(e -> tab_contestants.setContent(new ContestantTab(tab_contestants, stage)));

        Tab tab_groups = new Tab("Gruppen");
        tab_groups.setContent(new GroupTab(tab_groups, stage));
        tab_groups.setOnSelectionChanged(e -> tab_groups.setContent(new GroupTab(tab_groups, stage)));

        Tab tab_exams = new Tab("Prüfungen", new ExamTab());
        tab_exams.setOnSelectionChanged(e -> tab_exams.setContent(new ExamTab()));

        Tab tab_books = new Tab("Bücher", new BookTab());
        tab_books.setOnSelectionChanged(e -> tab_books.setContent(new BookTab()));

        Tab tab_drawing = new Tab("Verlosung", new DrawingTab());
        tab_drawing.setOnSelectionChanged(e -> tab_drawing.setContent(new DrawingTab()));

        Tab tab_settings = new Tab("Einstellungen", new SettingsTab());
        tab_settings.setOnSelectionChanged(e -> tab_settings.setContent(new SettingsTab()));

        parent.getTabs().addAll(tab_contestants, tab_groups, tab_exams, tab_books, tab_drawing, tab_settings);
        parent.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
