package sample.backend.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Settings {
    //General
    private final ObservableList<String> users = FXCollections.observableArrayList("Dorothea", "Petra");
    private final BooleanProperty useAndInSearch = new SimpleBooleanProperty(true);

    //Exam
    private final int maxAnswersCount = 6;
    private final IntegerProperty minCorrectAnswers = new SimpleIntegerProperty(4);

    //Drawing
    private final IntegerProperty minBookCount = new SimpleIntegerProperty(1);
    private final IntegerProperty maxBookCount = new SimpleIntegerProperty(40);
    private final IntegerProperty maxPicks = new SimpleIntegerProperty(3);
    private final IntegerProperty prizeCount = new SimpleIntegerProperty(10);

    //Group contest
    private LocalDate groupContestStartDate = LocalDate.parse("01.01.2020", DateTimeFormatter.ofPattern("dd.MM.uuuu"));
    private final IntegerProperty minMembers = new SimpleIntegerProperty(2);

    //Book
    private final ObservableList<String> languages = FXCollections.observableArrayList("Deutsch", "Englisch", "Italienisch", "Französisch", "Russisch");

    public int getMaxAnswersCount() {
        return maxAnswersCount;
    }

    int getMinCorrectAnswers() {
        return minCorrectAnswers.get();
    }

    public IntegerProperty minCorrectAnswersProperty() {
        return minCorrectAnswers;
    }

    public void setMinCorrectAnswers(int minCorrectAnswers) {
        this.minCorrectAnswers.set(minCorrectAnswers);
    }

    public int getMinBookCount() {
        return minBookCount.get();
    }

    public IntegerProperty minBookCountProperty() {
        return minBookCount;
    }

    public void setMinBookCount(int minBookCount) {
        this.minBookCount.set(minBookCount);
    }

    int getMaxBookCount() {
        return maxBookCount.get();
    }

    public IntegerProperty maxBookCountProperty() {
        return maxBookCount;
    }

    public void setMaxBookCount(int maxBookCount) {
        this.maxBookCount.set(maxBookCount);
    }

    int getMaxPicks() {
        return maxPicks.get();
    }

    public IntegerProperty maxPicksProperty() {
        return maxPicks;
    }

    public void setMaxPicks(int maxPicks) {
        this.maxPicks.set(maxPicks);
    }

    public int getPrizeCount() {
        return prizeCount.get();
    }

    public IntegerProperty prizeCountProperty() {
        return prizeCount;
    }

    public void setPrizeCount(int prizeCount) {
        this.prizeCount.set(prizeCount);
    }

    LocalDate getGroupContestStartDate() {
        return groupContestStartDate;
    }

    String getGroupContestStartDateAsString() {
        return groupContestStartDate.format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
    }

    public void setGroupContestStartDate(LocalDate date) {
        groupContestStartDate = date;
    }

    public ObservableList<String> getLanguages() {
        return languages;
    }

    public void setLanguages(ObservableList<String> languages) {
        this.languages.setAll(languages);
    }

    public ObservableList<String> getUsers(){
        return users;
    }

    public void setUsers(ObservableList<String> users){
        this.users.setAll(users);
    }

    int getMinMembers() {
        return minMembers.get();
    }

    public IntegerProperty minMembersProperty() {
        return minMembers;
    }

    public void setMinMembers(int minMembers) {
        this.minMembers.set(minMembers);
    }

    public boolean useAndInSearch() {
        return useAndInSearch.get();
    }

    public BooleanProperty useAndInSearchProperty() {
        return useAndInSearch;
    }

    public void setUseAndInSearch(boolean useAndInSearch) {
        this.useAndInSearch.set(useAndInSearch);
    }
}
