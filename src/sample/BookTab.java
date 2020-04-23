package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class BookTab {
    private static TableView<Book> tbv_books = new TableView<>();

    public static BorderPane generate() {
        BorderPane borderPane = new BorderPane();

        //Top
        TextField txt_search = new TextField();
        txt_search.setPromptText("Suche nach Titel, Autor, Sprache, ...");
        txt_search.textProperty().addListener((observable, oldValue, newValue) -> textChangeListener(newValue));
        borderPane.setTop(txt_search);

        //Center
        TableColumn<Book, String> column_title = new TableColumn<>("Titel");
        column_title.setCellFactory(param -> new AlignedTableCell<>());
        column_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book, String> column_authorFirstName = new TableColumn<>("Vorname Author");
        column_authorFirstName.setCellFactory(param -> new AlignedTableCell<>());
        column_authorFirstName.setCellValueFactory(new PropertyValueFactory<>("authorFirstName"));
        TableColumn<Book, String> column_authorLastName = new TableColumn<>("Nachname Author");
        column_authorLastName.setCellFactory(param -> new AlignedTableCell<>());
        column_authorLastName.setCellValueFactory(new PropertyValueFactory<>("authorLastName"));
        TableColumn<Book, String> column_language = new TableColumn<>("Sprache");
        column_language.setCellFactory(param -> new AlignedTableCell<>());
        column_language.setCellValueFactory(new PropertyValueFactory<>("language"));
        TableColumn<Book, Integer> column_points = new TableColumn<>("Punkte");
        column_points.setCellFactory(param -> new AlignedTableCell<>());
        column_points.setCellValueFactory(new PropertyValueFactory<>("points"));
        tbv_books.getColumns().addAll(column_title, column_authorFirstName, column_authorLastName, column_language, column_points);
        tbv_books.getItems().addAll(Data.books);
        tbv_books.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        borderPane.setCenter(tbv_books);

        //Bottom
        Button btn_addBook = new Button("Buch hinzufügen");
        btn_addBook.setMaxWidth(10000);
        borderPane.setBottom(btn_addBook);

        BorderPane.setMargin(borderPane.getCenter(), new Insets(10, 0, 10, 0));
        borderPane.setPadding(new Insets(10));
        return borderPane;
    }

    private static void textChangeListener(String newValue) {
        tbv_books.getItems().clear();
        if(newValue.trim().isEmpty()) {
            tbv_books.getItems().addAll(Data.books);
            return;
        }

        for(Book book : Data.books) {
            if(book.getTitle().contains(newValue) || book.getAuthorFirstName().contains(newValue) || book.getAuthorLastName().contains(newValue) || book.getLanguage().contains(newValue)) {
                tbv_books.getItems().add(book);
            }
        }
    }
}
