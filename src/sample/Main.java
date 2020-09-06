package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    private static TabPane tabPane;

    @Override
    public void start(Stage unused) {
        Data.init();

        CustomStage mainStage = new CustomStage();
        CustomStage loginStage = new CustomStage();
        StackPane loginItems = new StackPane();
        StackPane root = new StackPane();
        tabPane = new TabPane();

        initializeTabs();

        tabPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if(event.getTarget().getClass().getName().contains("TabPaneSkin")
            || (event.getTarget() instanceof Text && event.getY() <= 33)) { //To make sure it is text in the tab header
                if(!ButtonController.canLeave()) {
                    event.consume();
                }
            }
        });

        root.setAlignment(Pos.TOP_RIGHT);
        root.getChildren().addAll(tabPane, ButtonController.getButtons());

        ImageView logo = new ImageView(new Image("login.png"));
        logo.setFitWidth(350);
        logo.setPreserveRatio(true);
        loginItems.getChildren().add(logo);

        HBox controls = new HBox(30);

        ComboBox<String> cbx_user = new ComboBox<>(Data.settings.getUsers());
        controls.getChildren().add(cbx_user);

        Button btn_login = new Button("Anmelden");
        btn_login.setOnAction(e->{
            if(!cbx_user.getSelectionModel().isEmpty()) {
                loginStage.close();
                Data.currentUser = cbx_user.getSelectionModel().getSelectedItem();
                mainStage.setScene(root, 1280, Screen.getPrimary().getVisualBounds().getHeight() - 30);
                mainStage.setTitle("LesePreisUI");
                mainStage.show();
                tabPane.requestFocus();
            }
        });
        controls.getChildren().add(btn_login);

        controls.setAlignment(Pos.CENTER);
        controls.setTranslateY(110);
        loginItems.getChildren().add(controls);

        loginStage.setScene(new Scene(loginItems));
        loginStage.setTitle("Anmelden");
        loginStage.setWidth(350);
        loginStage.setHeight(320);
        loginStage.setResizable(false);
        loginStage.show();
    }

    private static void initializeTabs() {
        Tab tab_contestants = new Tab("Teilnehmer");
        tab_contestants.setContent(new ContestantTab());
        tab_contestants.setOnSelectionChanged(e -> tab_contestants.setContent(new ContestantTab()));

        Tab tab_groups = new Tab("Gruppen");
        tab_groups.setContent(new GroupTab());
        tab_groups.setOnSelectionChanged(e -> tab_groups.setContent(new GroupTab()));

        Tab tab_exams = new Tab("Prüfungen", new ExamTab());
        tab_exams.setOnSelectionChanged(e -> tab_exams.setContent(new ExamTab()));

        Tab tab_books = new Tab("Bücher", new BookTab());
        tab_books.setOnSelectionChanged(e -> tab_books.setContent(new BookTab()));

        Tab tab_drawing = new Tab("Verlosung", new DrawingTab());
        tab_drawing.setOnSelectionChanged(e -> tab_drawing.setContent(new DrawingTab()));

        Tab tab_settings = new Tab("Einstellungen", new SettingsTab());
        tab_settings.setOnSelectionChanged(e -> tab_settings.setContent(new SettingsTab()));

        tabPane.getTabs().addAll(tab_contestants, tab_groups, tab_exams, tab_books, tab_drawing, tab_settings);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
