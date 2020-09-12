package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Data {
    static ObservableList<Contestant> contestants;
    static ObservableList<Book> books;
    static HashMap<String, Group> groups;
    static String currentUser;
    static Settings settings;
    private static String path = ".\\";

    public static void init() {
        contestants = FXCollections.observableArrayList();
        books = FXCollections.observableArrayList();
        groups = new HashMap<>();
        settings = new Settings();

        Xml.getSettings(path + "settings.xml");
        Xml.getBooks(path + "books.xml");
        Xml.getContestants(path + "contestants.xml");

        //Temporary static lists, replace with files
        contestants.add(new Contestant("Manuel", "Ploner", "4AT"));
        contestants.add(new Contestant("Mattia", "Galiani", "4AT"));
        contestants.add(new Contestant("Maximilian", "Mitterrutzner", "4AT"));
        contestants.add(new Contestant("Nadine","Mitterrutzner", "4AS"));
        books.add((new Book("1984", "George", "Orwell", "Deutsch", 5)));
        books.add(new Book("The Million Pound Bank Note", "Mark", "Twain", "Englisch", 4));
        books.add(new Book("Harry Potter und die Heiligtümer des Todes", "Joanne", "Rowling", "Deutsch", 4));
        books.add(new Book("Harry Potter und der Stein der Weisen","Joanne","Rowling","Deutsch", 7));
        contestants.get(0).addExam(new Exam(books.get(0), new int[]{1,1,0,1,1,2}, "Dorothea", LocalDate.parse("10.04.2020", DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        contestants.get(0).addExam(new Exam(books.get(1), new int[]{0,1,1,0,1,1}, "Dorothea", LocalDate.parse("11.04.2020", DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        contestants.get(0).addExam(new Exam(books.get(2), new int[]{0,1,1,0,0,0}, "Dorothea", LocalDate.parse("12.04.2020", DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        contestants.get(3).addExam(new Exam(books.get(3), new int[]{1,1,1,1,2,2}, "Dorothea", LocalDate.parse("10.01.2020", DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        contestants.get(1).addExam(new Exam(books.get(3), new int[]{1,1,0,1,1,2}, "Dorothea", LocalDate.parse("25.04.2020", DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        contestants.get(1).addExam(new Exam(books.get(3), new int[]{1,1,0,1,1,2}, "Dorothea", LocalDate.parse("25.04.2020", DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        contestants.get(2).addExam(new Exam(books.get(3), new int[]{1,1,0,1,1,2}, "Dorothea", LocalDate.parse("25.04.2020", DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        contestants.get(2).addExam(new Exam(books.get(2), new int[]{0,0,1,1,1,1}, "Dorothea", LocalDate.parse("25.04.2020", DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        contestants.get(3).addExam(new Exam(books.get(2), new int[]{1,0,1,1,1,2}, "Dorothea", LocalDate.parse("22.04.2020", DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }

    public static void save() {
        Xml.set(path + "settings.xml", "Settings");
        Xml.set(path + "books.xml", "Books");
        Xml.set(path + "contestants.xml", "Contestants");
    }

    static Group getGroupByGrade(String grade) {
        if(groups.containsKey(grade)) {
            return groups.get(grade);
        }
        Group toAdd = new Group();
        groups.put(grade, toAdd);
        return toAdd;
    }
}
