package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Exam {
    private Book book;
    private IntegerPropertyArray answers;
    private IntegerProperty passed;
    private IntegerProperty points;
    private StringProperty librarian;
    private LocalDate date;
    private Contestant contestant;

    public Exam(Book book, int[] initialAnswers, String librarian, LocalDate date) {
        this.book = book;
        this.answers = new IntegerPropertyArray(SettingsTab.getMaxAnswersCount(), initialAnswers);
        this.librarian = new SimpleStringProperty(librarian);
        this.date = date;

        passed = new SimpleIntegerProperty();
        answers.addListener((observable, oldValue, newValue) -> onAnswerChangeListener());
        onAnswerChangeListener();
        points = new SimpleIntegerProperty(passed.get() == 1 ? book.getPoints() : 0);
        points.bind(Bindings.when(passed.isEqualTo(1)).then(book.pointsProperty()).otherwise(0));
    }

    public Exam(Book book) {
        this(book, new int[]{-1}, Data.currentUser, LocalDate.now());
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int[] getAnswers() {
        return answers.get();
    }

    public IntegerPropertyArray answersProperty() {
        return answers;
    }

    public void setAnswers(int[] answers) {
        this.answers.set(answers);
    }

    public int getPassed() {
        return passed.get();
    }

    public IntegerProperty passedProperty() {
        return passed;
    }

    public int getPoints() {
        return points.get();
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public String getLibrarian() {
        return librarian.get();
    }

    public StringProperty librarianProperty() {
        return librarian;
    }

    public void setLibrarian(String librarian) {
        this.librarian.set(librarian);
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateAsString() {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setContestant(Contestant contestant) {
        this.contestant = contestant;
    }

    public Contestant getContestant() {
        return contestant;
    }

    private void onAnswerChangeListener() {
        int correct = 0;
        boolean allSet = true;

        for(int value : answers.get()) {
            if (value == 1) {
                correct++;
            }
            else if (value == 2) {
                allSet = false;
            }
        }

        if(correct >= 4) {
            passed.set(1);
        }
        else if(allSet) {
            passed.set(0);
        }
        else {
            passed.set(2);
        }
    }

    public Element appendExam(Document doc, Element nodeExams){
        Element examElement = doc.createElement("Exam");
        Element bookTitleElement = doc.createElement("BookTitle");
        Element answersElement = doc.createElement("Answers");
        Element librarianElement = doc.createElement("Librarian");
        Element dateElement = doc.createElement("Date");

        bookTitleElement.appendChild(doc.createTextNode(this.book.getTitle()));
        answersElement.appendChild(doc.createTextNode(this.answers.toCompactString()));
        librarianElement.appendChild(doc.createTextNode(this.getLibrarian()));
        dateElement.appendChild(doc.createTextNode(this.getDateAsString()));

        examElement.appendChild(bookTitleElement);
        examElement.appendChild(answersElement);
        examElement.appendChild(librarianElement);
        examElement.appendChild(dateElement);

        nodeExams.appendChild(examElement);

        return nodeExams;
    }
}
