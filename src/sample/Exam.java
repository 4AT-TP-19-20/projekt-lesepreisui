package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Exam {
    private Book book;
    private IntegerPropertyArray answers;
    private StringProperty librarian;
    private LocalDate date;

    public Exam(Book book, int[] answers, String librarian, LocalDate date) {
        this.book = book;
        this.answers = new IntegerPropertyArray(Data.answerCount, answers);
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

        for (int i = 0; i < Data.answerCount; i++) {
            if (answers.getByIndex(i).get() == 1) {
                correctAnswerCount++;
            }
        }

        return correctAnswerCount >= 4;
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

    public String getLibrarian() {
        return librarian.get();
    }

    public StringProperty librarianProperty() {
        return librarian;
    }

    public void setLibrarian(String librarian) {
        this.librarian.set(librarian);
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("10.04.2020"));
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
