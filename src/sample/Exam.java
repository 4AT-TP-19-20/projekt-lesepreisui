package sample;

import java.util.Date;

public class Exam {
    private Book book;
    private String answers;
    private boolean passed;
    private String librarian;
    private Date date;

    public Exam(Book book, String answers, String librarian, Date date) {
        this.book = book;
        this.answers = answers;
        this.librarian = librarian;
        this.date = date;

        updatePassed();
    }

    public int getPoints() {
        if (passed) {
            return book.getPoints();
        }
        return 0;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
        updatePassed();
    }

    public boolean isPassed() {
        updatePassed();
        return passed;
    }

    public String getLibrarian() {
        return librarian;
    }

    public void setLibrarian(String librarian) {
        this.librarian = librarian;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private void updatePassed() {
        int correctAnswerCount = 0;

        for (char answer : answers.toCharArray()) {
            if (answer == 'r') {
                correctAnswerCount++;
            }
        }

        if (correctAnswerCount >= 4) {
            passed = true;
        } else {
            passed = false;
        }
    }
}
