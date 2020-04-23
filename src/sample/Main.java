package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        TabPane root = new TabPane();
        Data.init();
        root.getTabs().add(new Tab("Teilnehmer", ContestantTab.generate()));
        root.getTabs().add(new Tab("Prüfungen"));
        root.getTabs().add(new Tab("Bücher"));
        root.getTabs().add(new Tab("Verlosung"));
        root.getTabs().add(new Tab("Einstellungen"));
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        primaryStage.setTitle("LesePreisUI");
        primaryStage.setScene(new Scene(root, 1280, 1000));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
