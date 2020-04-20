package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contestant {
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty grade;
    private BooleanProperty isGroupMember;
    private ObservableList<Exam> exams;

    public Contestant(String firstName, String lastName, String grade) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.grade = new SimpleStringProperty(grade);

        this.isGroupMember = new SimpleBooleanProperty(false);

        exams = FXCollections.observableArrayList();
    }

    public Contestant() {
        this("", "", "");
    }


    public int getPoints() {
        int points = 0;

        for(Exam exam : exams) {
            points += exam.getPoints();
        }

        return points;
    }

    public int getBookCount() {
        int bookCount = 0;

        for(Exam exam : exams) {
            if(exam.isPassed()) {
                bookCount++;
            }
        }

        return bookCount;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getGrade() {
        return grade.get();
    }

    public StringProperty gradeProperty() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
    }

    public boolean isGroupMember() {
        return isGroupMember.get();
    }

    public BooleanProperty groupMemberProperty() {
        return isGroupMember;
    }

    public void setGroupMember(boolean groupMember) {
        isGroupMember.set(groupMember);
    }

    public ObservableList<Exam> getExams() {
        return exams;
    }

    public void setExams(ObservableList<Exam> exams) {
        this.exams = exams;
    }

    public void addExam(Exam exam) {
        exams.add(exam);
    }
}
