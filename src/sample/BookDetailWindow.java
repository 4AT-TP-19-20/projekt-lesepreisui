package sample;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BookDetailWindow {
    private static Stage stage;

    public static void showNewWindow(Book book) {
        stage = new Stage();

        GridPane gridPane = new GridPane();

        gridPane.add(new Label("Titel"),0,0);
        TextField txt_title = new TextField();
        txt_title.textProperty().bindBidirectional(book.titleProperty());
        gridPane.add(txt_title,1,0,2,1);
        gridPane.add(new Label("Vorname Autor"),0,1);
        TextField txt_authorFirstName = new TextField();
        txt_authorFirstName.textProperty().bindBidirectional(book.authorFirstNameProperty());
        gridPane.add(txt_authorFirstName,1,1,2,1);
        gridPane.add(new Label("Nachname Autor"),0,2);
        TextField txt_authorLastName = new TextField();
        txt_authorLastName.textProperty().bindBidirectional(book.authorLastNameProperty());
        gridPane.add(txt_authorLastName,1,2,2,1);
        gridPane.add(new Label("Sprache"),0,3);
        ComboBox<String> cbx_languages = new ComboBox<>(SettingsTab.getLanguages());
        cbx_languages.setPrefWidth(200);
        gridPane.add(cbx_languages,1,3);
        cbx_languages.getSelectionModel().select(book.getLanguage());
        cbx_languages.setOnAction(e->{
            if(!cbx_languages.getSelectionModel().isEmpty()) {
                book.setLanguage(cbx_languages.getSelectionModel().getSelectedItem());
            }
        });
        Button btn_editLanguages = new Button("Sprachen verwalten");
        gridPane.add(btn_editLanguages,2,3);
        gridPane.add(new Label("Lose"),0,4);
        TextField txt_points = new TextField();
        txt_points.textProperty().bindBidirectional(book.pointsProperty(), new StringToInt());
        gridPane.add(txt_points,1,4,2,1);

        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Stage stage = new Stage();
        stage.setTitle("Detailfenster Buch");
        stage.setScene(new Scene(gridPane, 450,170));
        stage.show();
    }

    public static BorderPane generateBar(BorderPane base){
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
            stage.close();
        });
        minimize.setOnMouseClicked(ActionEvent->{
            stage.setIconified(true);
        });

        return base;
    }
}
