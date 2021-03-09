package sample.backend.data;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.backend.Saveable;
import sample.backend.Searchable;
import sample.backend.data.database.DatabaseEntry;

import java.util.ArrayList;
import java.util.Arrays;

public class Contestant extends DatabaseEntry implements Saveable, Comparable<Contestant>, Searchable {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty grade;
    private final BooleanProperty isGroupMember;
    private final BooleanProperty isQualified;
    private final IntegerProperty points;
    private final IntegerProperty bookCount;
    private final ObservableList<Exam> exams;

    public Contestant(String firstName, String lastName, String grade) {
        super("contestants");

        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.grade = new SimpleStringProperty(grade);
        this.points = new SimpleIntegerProperty();
        this.bookCount = new SimpleIntegerProperty();
        this.isGroupMember = new SimpleBooleanProperty();
        this.isQualified = new SimpleBooleanProperty();

        isQualified.bind(Bindings.greaterThanOrEqual(bookCount, Data.settings.minBookCountProperty()));
        isGroupMember.addListener((observable, oldValue, newValue) -> Data.getGroupByGrade(getGrade()).addMember(this));
        exams = FXCollections.observableArrayList();

        registerUpdateListeners();
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
        Exam maxGermanExam = null;
        Exam maxOtherExam = null;

        for(Exam exam : exams) {
            if(exam.getPoints() != 0) {
                if(exam.getBook().getLanguage().equals("Deutsch")) {
                    if(maxGermanExam == null) {
                        maxGermanExam = exam;
                        continue;
                    }
                    else if(exam.getPoints() > maxGermanExam.getPoints()) {
                        newPoints += maxGermanExam.getPoints();
                        maxGermanExam = exam;
                        continue;
                    }
                }
                else {
                    if(maxOtherExam == null) {
                        maxOtherExam = exam;
                        continue;
                    }
                    else if(exam.getPoints() > maxOtherExam.getPoints()) {
                        newPoints += maxOtherExam.getPoints();
                        maxOtherExam = exam;
                        continue;
                    }
                }
                newPoints += exam.getPoints();
            }
        }

        if(maxGermanExam != null && maxOtherExam != null) {
            newPoints += maxGermanExam.getPoints() * maxOtherExam.getPoints();
        }
        else if(maxGermanExam != null) {
            newPoints += maxGermanExam.getPoints();
        }
        else if(maxOtherExam != null) {
            newPoints += maxOtherExam.getPoints();
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

    @Override
    public boolean search(String s) {
        return getFirstName().toLowerCase().contains(s)
                || getLastName().toLowerCase().contains(s)
                || getGrade().toLowerCase().contains(s);
    }

    @Override
    public void onDelete() {
        isGroupMember.removeListener((observable, oldValue, newValue) -> Data.getGroupByGrade(getGrade()).addMember(this));
        if(isGroupMember()) {
            Group group = Data.getGroupByGrade(getGrade());
            group.removeMember(this);
            if(group.getMemberCount() == 0) {
                Data.groups.remove(group);
            }
        }

        //Foreach on duplicate because lists should not be modified while looping trough them
        new ArrayList<>(getExams()).forEach(DatabaseEntry::delete);

        Data.contestants.remove(getId());
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        object.put("firstname", getFirstName());
        object.put("lastname", getLastName());
        object.put("grade", getGrade());
        object.put("isgroupmember", isGroupMember());
        JSONArray examIDs = new JSONArray(getExams().size());
        getExams().forEach(exam -> examIDs.put(exam.getId()));
        object.put("exams", examIDs);
        return object;
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public void fromJson(JSONObject object) {
        setFirstName(object.getString("firstname"));
        setLastName(object.getString("lastname"));
        setGrade(object.getString("grade"));
        setGroupMember(object.getBoolean("isgroupmember"));
        object.getJSONArray("exams").forEach(examID -> addExam(Data.exams.get(examID)));
    }
}
