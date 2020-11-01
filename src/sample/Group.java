package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Group implements Comparable<Group>, Searchable {
    private final String grade;
    private ObservableList<Contestant> members;
    private IntegerProperty memberCount;
    private IntegerProperty points;
    private BooleanProperty qualified;

    Group(String grade) {
        this.grade = grade;
        members = FXCollections.observableArrayList();
        memberCount = new SimpleIntegerProperty();
        memberCount.bind(Bindings.size(members));
        points = new SimpleIntegerProperty();
        qualified = new SimpleBooleanProperty();
        qualified.bind(Bindings.greaterThanOrEqual(memberCount, Data.settings.minMembersProperty()));
    }

    public String getGrade() {
        return grade;
    }

    public int getPoints() {
        return points.get();
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public ObservableList<Contestant> getMembers() {
        return members;
    }

    public void setMembers(ObservableList<Contestant> members) {
        for(Contestant member : members) {
            member.pointsProperty().addListener(e -> pointsUpdate());
        }
        this.members = members;
        pointsUpdate();
    }

    public void addMember(Contestant member) {
        member.pointsProperty().addListener(e -> pointsUpdate());
        members.add(member);
        pointsUpdate();
    }

    public void removeMember(Contestant member) {
        member.pointsProperty().removeListener(e -> pointsUpdate());
        members.remove(member);
        pointsUpdate();
    }

    public int getMemberCount() {
        return memberCount.get();
    }

    public IntegerProperty memberCountProperty() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount.set(memberCount);
    }

    public boolean isQualified() {
        return qualified.get();
    }

    public BooleanProperty qualifiedProperty() {
        return qualified;
    }

    private void pointsUpdate() {
        int newPoints = 0;

        for(Contestant member : members) {
            for(Exam exam : member.getExams()) {
                if(exam.getDate().isAfter(Data.settings.getGroupContestStartDate()) || exam.getDate().isEqual(Data.settings.getGroupContestStartDate())) {
                    newPoints += exam.getPoints();
                }
            }
        }

        points.set(newPoints);
    }

    @Override
    public int compareTo(Group other) {
        return this.getGrade().compareTo(other.getGrade());
    }

    @Override
    public boolean search(String s) {
        return getGrade().toLowerCase().contains(s);
    }
}
