package sample;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class BookTab extends BorderPane {
    private static CustomTableView<Book> tbv_books;

    BookTab() {
        //Top
        TextField txt_search = new TextField();
        txt_search.setPromptText("Suche nach Titel, Autor, Sprache, ...");
        txt_search.textProperty().addListener((observable, oldValue, newValue) -> textChangeListener(newValue));
        this.setTop(txt_search);

        //Center
        tbv_books = new CustomTableView<>();

        tbv_books.<String>addColumn("Titel", new PropertyValueFactory<>("title"));
        tbv_books.<String>addColumn("Vorname Author", new PropertyValueFactory<>("authorFirstName"));
        tbv_books.<String>addColumn("Nachname Author", new PropertyValueFactory<>("authorLastName"));
        tbv_books.<String>addColumn("Sprache", new PropertyValueFactory<>("language"));
        tbv_books.<Integer>addColumn("Punkte", new PropertyValueFactory<>("points"));

        tbv_books.getItems().addAll(Data.books);
        tbv_books.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbv_books.setOnMouseClicked((MouseEvent event) -> {
            if(event.getTarget() instanceof TableColumnHeader) {
                tbv_books.getSelectionModel().clearSelection();
            }
            else if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                if(!tbv_books.getSelectionModel().isEmpty()) {
                    onItemSelected();
                }
            }
        });
        this.setCenter(tbv_books);

        //Bottom
        VBox bottomItems = new VBox();
        Button btn_addBook = new Button("Buch hinzufügen");
        btn_addBook.setId("custom-button");
        btn_addBook.setOnAction(e->{
            Book toAdd = new Book();
            Data.books.add(toAdd);
            tbv_books.getItems().add(toAdd);
            BookDetailWindow bookDetailWindow = new BookDetailWindow(toAdd);
            bookDetailWindow.show();
        });
        Button btn_removeBook = new Button("Buch löschen");
        btn_removeBook.setId("red-button");
        btn_removeBook.setOnAction(e->{
            if(!tbv_books.getSelectionModel().isEmpty()) {
                Book selected = tbv_books.getSelectionModel().getSelectedItem();
                Data.books.remove(selected);
                tbv_books.getItems().remove(selected);
            }
        });
        bottomItems.getChildren().addAll(btn_addBook, btn_removeBook);
        bottomItems.setSpacing(5);
        this.setBottom(bottomItems);

        BorderPane.setMargin(this.getCenter(), new Insets(10, 0, 10, 0));
        this.setPadding(new Insets(10));
    }

    Book getSelectedBook() {
        return tbv_books.getSelectionModel().getSelectedItem();
    }

    private void textChangeListener(String newValue) {
        newValue = newValue.trim().toLowerCase();
        tbv_books.getItems().clear();

        if(newValue.trim().isEmpty()) {
            tbv_books.getItems().addAll(Data.books);
            return;
        }

        for(Book book : Data.books) {
            if(Searchables.contain(newValue, book)) {
                tbv_books.getItems().add(book);
            }
        }
    }

    protected void onItemSelected() {
        BookDetailWindow bookDetailWindow = new BookDetailWindow(tbv_books.getSelectionModel().getSelectedItem());
        bookDetailWindow.show();
    }
}
