package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Group {
    private ObservableList<Contestant> members;
    private IntegerProperty memberCount;
    private IntegerProperty points;
    private BooleanProperty qualified;

    public Group() {
        members = FXCollections.observableArrayList();
        memberCount = new SimpleIntegerProperty();
        memberCount.bind(Bindings.size(members));
        points = new SimpleIntegerProperty();
        qualified = new SimpleBooleanProperty();
        qualified.bind(Bindings.greaterThanOrEqual(memberCount, SettingsTab.minMembersProperty()));
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
                if(exam.getDate().isAfter(SettingsTab.getGroupContestStartDate()) || exam.getDate().isEqual(SettingsTab.getGroupContestStartDate())) {
                    newPoints += exam.getPoints();
                }
            }
        }

        points.set(newPoints);
    }
}
