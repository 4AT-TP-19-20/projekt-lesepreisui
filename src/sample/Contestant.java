package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Contestant {
    private String firstName;
    private String lastName;
    private String grade;
    private boolean isGroupMember;
    private int points;
    private int bookCount;
    private ObservableList<Exam> exams;

    public Contestant(String firstName, String lastName, String grade) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;

        exams = FXCollections.observableArrayList();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public boolean isGroupMember() {
        return isGroupMember;
    }

    public void setGroupMember(boolean groupMember) {
        isGroupMember = groupMember;
    }

    public int getPoints() {
        updatePointsAndBookCount();
        return points;
    }

    public int getBookCount() {
        updatePointsAndBookCount();
        return bookCount;
    }

    public ObservableList<Exam> getExams() {
        return exams;
    }

    public void setExams(ObservableList<Exam> exams) {
        this.exams = exams;
        updatePointsAndBookCount();
    }

    public void addExam(Exam exam) {
        exams.add(exam);
        updatePointsAndBookCount();
    }

    //TODO implement multiply option
    private void updatePointsAndBookCount() {
        int receivedPoints;
        points = 0;
        bookCount = 0;
        for(Exam exam : exams) {
            receivedPoints = exam.getPoints();
            if(receivedPoints != 0)
            {
                bookCount++;
                points += receivedPoints;
            }
        }
    }
}
