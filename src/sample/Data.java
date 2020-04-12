package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Data {
    public static ObservableList<Contestant> contestants;
    public static ObservableList<Book> books;

    public static void init() {
        contestants = FXCollections.observableArrayList();
        books = FXCollections.observableArrayList();

        //Temporary static lists, replace with files
        contestants.add(new Contestant("Manuel", "Ploner", "4AT"));
        contestants.add(new Contestant("Mattia", "Galiani", "4AT"));
        contestants.add(new Contestant("Maximilian", "Mitterrutzner", "4AT"));
        books.add((new Book("1984", "George", "Orwell", "Deutsch", 5)));
        books.add(new Book("The Million Pound Bank Note", "Mark", "Twain", "Englisch", 4));
        books.add(new Book("Harry Potter und die Heiligtümer des Todes", "Joanne", "Rowling", "Deutsch", 4));
        try {
            contestants.get(0).addExam(new Exam(books.get(0),"RRFRR", "Dorothea", new SimpleDateFormat("dd.MM.yyyy").parse("10.04.2020")));
            contestants.get(0).addExam(new Exam(books.get(1),"FRRFRR", "Dorothea", new SimpleDateFormat("dd.MM.yyyy").parse("11.04.2020")));
        } catch (ParseException ex) {
            ex.printStackTrace();
            System.out.println("Error on parse in Data generation");
        }
    }
}
