package sample.backend.data;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import sample.backend.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

public class Data {
    public static ObservableList<Contestant> contestants;
    public static ObservableList<Book> books;
    public static ObservableList<Group> groups;
    public static String currentUser;
    public static Settings settings;
    public static DateTimeFormatter dateFormatter;
    private static final String path = ".\\storage\\";

    public static void init() {
        contestants = FXCollections.observableArrayList();
        books = FXCollections.observableArrayList();
        groups = FXCollections.observableArrayList();
        settings = new Settings();
        dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        contestants.addListener((ListChangeListener<Contestant>) c -> {
            while (c.next()) {
                for(Contestant added : c.getAddedSubList()) {
                    added.groupMemberProperty().addListener((observable, oldValue, newValue) -> groupMemberListener(added, newValue));
                    if(added.isGroupMember()) {
                        getGroupByGrade(added.getGrade());
                    }
                }

                for(Contestant removed : c.getRemoved()) {
                    removed.groupMemberProperty().removeListener((observable, oldValue, newValue) -> groupMemberListener(removed, newValue));
                    Group groupOfRemoved = getGroupByGrade(removed.getGrade());
                    groupOfRemoved.removeMember(removed);
                    if(groupOfRemoved.getMemberCount() == 0) {
                        groups.remove(groupOfRemoved);
                    }
                }
            }
        });

        Xml.getSettings(path + "settings.xml");
        Xml.getBooks(path + "books.xml");
        Xml.getContestants(path + "contestants.xml");

        //Temporary static lists, replace with files
        /*
        contestants.add(new Contestant("Manuel", "Ploner", "4AT"));
        contestants.add(new Contestant("Mattia", "Galiani", "4AT"));
        contestants.add(new Contestant("Maximilian", "Mitterrutzner", "4AT"));
        contestants.add(new Contestant("Nadine","Mitterrutzner", "4AS"));
        books.add((new Book("1984", "George", "Orwell", "Deutsch", 5)));
        books.add(new Book("The Million Pound Bank Note", "Mark", "Twain", "Englisch", 4));
        books.add(new Book("Harry Potter und die Heiligtümer des Todes", "Joanne", "Rowling", "Deutsch", 4));
        books.add(new Book("Harry Potter und der Stein der Weisen","Joanne","Rowling","Deutsch", 7));
        contestants.get(0).addExam(new Exam(books.get(0), new int[]{1,1,0,1,1,2}, "Dorothea", LocalDate.parse("10.04.2020", dateFormatter)));
        contestants.get(0).addExam(new Exam(books.get(1), new int[]{0,1,1,0,1,1}, "Dorothea", LocalDate.parse("11.04.2020", dateFormatter)));
        contestants.get(0).addExam(new Exam(books.get(2), new int[]{0,1,1,0,0,0}, "Dorothea", LocalDate.parse("12.04.2020", dateFormatter)));
        contestants.get(3).addExam(new Exam(books.get(3), new int[]{1,1,1,1,2,2}, "Dorothea", LocalDate.parse("10.01.2020", dateFormatter)));
        contestants.get(1).addExam(new Exam(books.get(3), new int[]{1,1,0,1,1,2}, "Dorothea", LocalDate.parse("25.04.2020", dateFormatter)));
        contestants.get(1).addExam(new Exam(books.get(3), new int[]{1,1,0,1,1,2}, "Dorothea", LocalDate.parse("25.04.2020", dateFormatter)));
        contestants.get(2).addExam(new Exam(books.get(3), new int[]{1,1,0,1,1,2}, "Dorothea", LocalDate.parse("25.04.2020", dateFormatter)));
        contestants.get(2).addExam(new Exam(books.get(2), new int[]{0,0,1,1,1,1}, "Dorothea", LocalDate.parse("25.04.2020", dateFormatter)));
        contestants.get(3).addExam(new Exam(books.get(2), new int[]{1,0,1,1,1,2}, "Dorothea", LocalDate.parse("22.04.2020", dateFormatter)));
        */
    }

    public static void save() {
        if(!Files.exists(Paths.get(path))) {
            try {
                Files.createDirectory(Paths.get(path));
            } catch (IOException ex) {
                System.out.println("[System] Error while creating storage directory!");
            }
        }
        Xml.set(path + "settings.xml", "Settings");
        Xml.set(path + "books.xml", "Books");
        Xml.set(path + "contestants.xml", "Contestants");
    }

    public static Group getGroupByGrade(String grade) {
        for(Group group : groups) {
            if(group.getGrade().equals(grade)) {
                return group;
            }
        }

        Group newGroup = new Group(grade);
        groups.add(newGroup);
        return newGroup;
    }

    private static void groupMemberListener(Contestant contestant, boolean newValue) {
        Group group = getGroupByGrade(contestant.getGrade());
        if(newValue) {
            group.addMember(contestant);
        }
        else {
            group.removeMember(contestant);
            if(group.getMemberCount() == 0) {
                groups.remove(group);
            }
        }
    }
}
