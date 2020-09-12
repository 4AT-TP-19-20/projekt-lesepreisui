package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

class BookDetailWindow extends CustomStage {
    BookDetailWindow(Book book) {
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
        ComboBox<String> cbx_languages = new ComboBox<>(Data.settings.getLanguages());
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
        txt_points.textProperty().bindBidirectional(book.pointsProperty(), StringToInt.getInstance());
        gridPane.add(txt_points,1,4,2,1);

        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        this.enableBlocking();
        this.setTitle("Buch bearbeiten");
        this.setScene(gridPane, 450, 170);
    }
}
