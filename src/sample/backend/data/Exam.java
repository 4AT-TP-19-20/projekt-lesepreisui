package sample.backend.data;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sample.backend.IntegerPropertyArray;
import sample.backend.Saveable;
import sample.backend.Searchable;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class Exam implements Saveable, Comparable<Exam>, Searchable {
    private Book book;
    private final IntegerPropertyArray answers;
    private final IntegerProperty passed;
    private final IntegerProperty points;
    private final StringProperty librarian;
    private LocalDate date;
    private Contestant contestant;

    public Exam(Book book, int[] initialAnswers, String librarian, LocalDate date) {
        this.book = book;
        this.answers = new IntegerPropertyArray(Data.settings.getMaxAnswersCount(), initialAnswers);
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

    @Override
    public Exam getCopy() {
        Exam copy = new Exam(this.getBook(), this.getAnswers(), this.getLibrarian(), this.getDate());
        copy.setContestant(this.getContestant());
        return copy;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Exam) {
            Exam otherExam = (Exam) other;
            return this.getBook() == otherExam.getBook()
                    && Arrays.equals(this.getAnswers(), otherExam.getAnswers())
                    && this.getLibrarian().equals(otherExam.getLibrarian())
                    && this.getDate() == otherExam.getDate()
                    && this.getContestant() == otherExam.getContestant();
        }
        return false;
    }

    @Override
    public void setValues(Saveable other) {
        if(other instanceof Exam) {
            Exam otherExam = (Exam) other;
            this.setBook(otherExam.getBook());
            this.setAnswers(otherExam.getAnswers());
            this.setLibrarian(otherExam.getLibrarian());
            this.setDate(otherExam.getDate());
            this.setContestant(otherExam.getContestant());
        }
        else {
            throw new RuntimeException("Passed Saveable is not instance of Exam");
        }
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
        return date.format(Data.dateFormatter);
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

        if(correct >= Data.settings.getMinCorrectAnswers()) {
            passed.set(1);
        }
        else if(allSet) {
            passed.set(0);
        }
        else {
            passed.set(2);
        }
    }

    @Override
    public int compareTo(Exam other) {
        //For every book only one exam can be taken by every contestant,
        //which means every exam of a contestant has an unique book, so comparing the books is good enough
        return this.getBook().compareTo(other.getBook());
    }

    @Override
    public boolean search(String s) {
        if(getBook().search(s)
        || getLibrarian().toLowerCase().contains(s)) {
            return true;
        }

        try {
            if(s.contains("-")) { //Date range
                String[] dates = s.split("-");

                if(dates.length != 2) {
                    return false;
                }

                LocalDate date1 = LocalDate.parse(dates[0].trim(), Data.dateFormatter);
                LocalDate date2 = LocalDate.parse(dates[1].trim(), Data.dateFormatter);

                return getDate().isEqual(date1)
                        || getDate().isEqual(date2)
                        || (getDate().isAfter(date1) && getDate().isBefore(date2));
            }
            else { //Specific date
                return getDate().isEqual(LocalDate.parse(s.trim(), Data.dateFormatter));
            }
        } catch (DateTimeParseException ex) {
            return false;
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
