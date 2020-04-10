package sample;

import java.util.ArrayList;

public class Contestant
{
    private String firstName;
    private String lastName;
    private String grade;
    private boolean isGroupMember;
    private ArrayList<Exam> exams;

    public Contestant(String firstName, String lastName, String grade)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
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
}
