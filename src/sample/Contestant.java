package sample;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Contestant {
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty grade;
    private BooleanProperty isGroupMember;
    private IntegerProperty points;
    private IntegerProperty bookCount;
    private ObservableList<Exam> exams;

    public Contestant(String firstName, String lastName, String grade) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.grade = new SimpleStringProperty(grade);
        this.points = new SimpleIntegerProperty();
        this.bookCount = new SimpleIntegerProperty();
        this.isGroupMember = new SimpleBooleanProperty(false);
        exams = FXCollections.observableArrayList();
    }

    public Contestant() {
        this("", "", "");
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

    public int getPoints() {
        return points.get();
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public int getBookCount() {
        int bookCount = 0;

        for(Exam exam : exams) {
            if(exam.getPassed() == 1) {
                bookCount++;
            }
        }

        return bookCount;
    }

    public IntegerProperty bookCountProperty() {
        return bookCount;
    }

    public ObservableList<Exam> getExams() {
        return exams;
    }

    public void setExams(ObservableList<Exam> exams) {
        for(Exam exam : exams) {
            exam.pointsProperty().addListener(param -> pointsUpdate());
            exam.passedProperty().addListener(param -> bookCountUpdate());
        }
        this.exams = exams;
    }

    public void addExam(Exam exam) {
        exam.pointsProperty().addListener(param -> pointsUpdate());
        exam.passedProperty().addListener(param -> bookCountUpdate());
        exams.add(exam);
        pointsUpdate();
        bookCountUpdate();
    }

    public void removeExam(Exam exam) {
        exam.pointsProperty().removeListener(param -> pointsUpdate());
        exam.passedProperty().removeListener(param -> bookCountUpdate());
        exams.remove(exam);
    }

    private void pointsUpdate() {
        int newPoints = 0;

        for(Exam exam : exams) {
            newPoints += exam.getPoints();
        }

        points.set(newPoints);
    }

    private void bookCountUpdate() {
        int newBookCount = 0;

        for(Exam exam : exams) {
            if(exam.getPassed() == 1) {
                newBookCount++;
            }
        }

        bookCount.set(newBookCount);
    }

    public Element getXMLNode(Document doc){
        Element contestantElement = doc.createElement("Contestant");
        Element firstNameElement = doc.createElement("FirstName");
        Element lastNameElement = doc.createElement("LastName");
        Element gradeElement = doc.createElement("Grade");
        Element isGroupMemberElement = doc.createElement("IsGroupMember");
        Element examsElement = doc.createElement("Exams");

        firstNameElement.appendChild(doc.createTextNode(this.getFirstName()));
        lastNameElement.appendChild(doc.createTextNode(this.getLastName()));
        gradeElement.appendChild(doc.createTextNode(this.getGrade()));
        isGroupMemberElement.appendChild(doc.createTextNode("" + this.isGroupMember()));
        for (Exam exam:exams) {
            examsElement=exam.appendExam(doc, examsElement);
        }

        contestantElement.appendChild(firstNameElement);
        contestantElement.appendChild(lastNameElement);
        contestantElement.appendChild(gradeElement);
        contestantElement.appendChild(isGroupMemberElement);
        contestantElement.appendChild(examsElement);

        return contestantElement;
    }
}
