package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Arrays;

public class Contestant implements Saveable, Comparable<Contestant> {
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty grade;
    private BooleanProperty isGroupMember;
    private BooleanProperty isQualified;
    private IntegerProperty points;
    private IntegerProperty bookCount;
    private ObservableList<Exam> exams;

    public Contestant(String firstName, String lastName, String grade) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.grade = new SimpleStringProperty(grade);
        this.points = new SimpleIntegerProperty();
        this.bookCount = new SimpleIntegerProperty();
        this.isGroupMember = new SimpleBooleanProperty();
        isGroupMember.addListener(e -> {
            if(isGroupMember.get()) {
                Data.getGroupByGrade(grade).addMember(this);
            }
            else {
                Data.getGroupByGrade(grade).removeMember(this);
            }
        });
        this.isQualified = new SimpleBooleanProperty();
        isQualified.bind(Bindings.greaterThanOrEqual(bookCount, Data.settings.minBookCountProperty()));
        exams = FXCollections.observableArrayList();
    }

    public Contestant() {
        this("Vorname", "Nachname", "Klasse");
    }

    @Override
    public Contestant getCopy() {
        Contestant copy = new Contestant(getFirstName(), getLastName(), getGrade());
        copy.setGroupMember(isGroupMember());
        copy.copyExams(getExams());
        return copy;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Contestant) {
            Contestant otherContestant = (Contestant) other;
            return this.getFirstName().equals(otherContestant.getFirstName())
                    && this.getLastName().equals(otherContestant.getLastName())
                    && this.getGrade().equals(otherContestant.getGrade())
                    && this.isGroupMember() == otherContestant.isGroupMember()
                    && this.examsEqual(otherContestant.getExams());
        }
        return false;
    }

    @Override
    public void setValues(Saveable other) {
        if(other instanceof Contestant) {
            Contestant otherContestant = (Contestant) other;
            this.setFirstName(otherContestant.getFirstName());
            this.setLastName(otherContestant.getLastName());
            this.setGrade(otherContestant.getGrade());
            this.setGroupMember(otherContestant.isGroupMember());
            this.copyExams(otherContestant.getExams());
        }
        else {
            throw new RuntimeException("Passed Saveable is not instance of Contestant");
        }
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

    public boolean isQualified() {
        return isQualified.get();
    }

    public BooleanProperty qualifiedProperty() {
        return isQualified;
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

    public void copyExams(ObservableList<Exam> examsToCopy) {
        clearExams();

        for(Exam exam : examsToCopy) {
            Exam newExam = exam.getCopy();
            newExam.pointsProperty().addListener(param -> pointsUpdate());
            newExam.passedProperty().addListener(param -> bookCountUpdate());
            newExam.setContestant(exam.getContestant());
            exams.add(newExam);
        }

        pointsUpdate();
        bookCountUpdate();
    }

    public void addExam(Exam exam) {
        exam.pointsProperty().addListener(param -> pointsUpdate());
        exam.passedProperty().addListener(param -> bookCountUpdate());
        exam.setContestant(this);
        exams.add(exam);
        pointsUpdate();
        bookCountUpdate();
    }

    public void removeExam(Exam exam) {
        exam.pointsProperty().removeListener(param -> pointsUpdate());
        exam.passedProperty().removeListener(param -> bookCountUpdate());
        exams.remove(exam);
        pointsUpdate();
        bookCountUpdate();
    }

    public void clearExams() {
        for(Exam exam : exams) {
            exam.pointsProperty().removeListener(param -> pointsUpdate());
            exam.passedProperty().removeListener(param -> bookCountUpdate());
        }
        exams.clear();
        pointsUpdate();
        bookCountUpdate();
    }

    public boolean examsEqual(ObservableList<Exam> otherExams) {
        Exam[] ownSorted = exams.toArray(new Exam[0]);
        Arrays.sort(ownSorted);
        Exam[] otherSorted = otherExams.toArray(new Exam[0]);
        Arrays.sort(otherSorted);

        if(ownSorted.length == otherSorted.length) {
            for (int i = 0; i < ownSorted.length; i++) {
                if(!ownSorted[i].equals(otherSorted[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
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

    @Override
    public int compareTo(Contestant other) {
        if(!this.getGrade().equals(other.getGrade())) {
            return this.getGrade().compareTo(other.getGrade());
        }
        if(!this.getLastName().equals(other.getLastName())) {
            return this.getLastName().compareTo(other.getLastName());
        }
        return this.getFirstName().compareTo(other.getFirstName());
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
