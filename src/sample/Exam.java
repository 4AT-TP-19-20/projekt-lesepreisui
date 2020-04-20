package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class Exam {
    private Book book;
    private StringProperty answers;
    private StringProperty librarian;
    private Date date;

    public Exam(Book book, String answers, String librarian, Date date) {
        this.book = book;
        this.answers = new SimpleStringProperty(answers);
        this.librarian = new SimpleStringProperty(librarian);
        this.date = date;
    }

    public int getPoints() {
        if (isPassed()) {
            return book.getPoints();
        }
        return 0;
    }

    public boolean isPassed() {
        int correctAnswerCount = 0;

        for (char answer : answers.get().toCharArray()) {
            if (answer == 'R') {
                correctAnswerCount++;
            }
        }

        if (correctAnswerCount >= 4) {
            return true;
        } else {
            return false;
        }
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getAnswers() {
        return answers.get();
    }

    public StringProperty answersProperty() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers.set(answers);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
