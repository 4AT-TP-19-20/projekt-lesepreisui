package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Settings implements Saveable<Settings> {
    //General
    private ObservableList<String> users = FXCollections.observableArrayList("Dorothea", "Petra");

    //Exam
    private IntegerProperty maxAnswersCount = new SimpleIntegerProperty(6);
    private IntegerProperty minCorrectAnswers = new SimpleIntegerProperty(4);

    //Drawing
    private IntegerProperty minBookCount = new SimpleIntegerProperty(1);
    private IntegerProperty maxBookCount = new SimpleIntegerProperty(40);
    private IntegerProperty maxPicks = new SimpleIntegerProperty(3);
    private IntegerProperty prizeCount = new SimpleIntegerProperty(10);

    //Group contest
    private LocalDate groupContestStartDate = LocalDate.parse("01.01.2020", DateTimeFormatter.ofPattern("dd.MM.uuuu"));
    private IntegerProperty minMembers = new SimpleIntegerProperty(2);

    //Book
    private ObservableList<String> languages = FXCollections.observableArrayList("Deutsch", "Englisch", "Italienisch", "Französisch", "Russisch");

    @Override
    public Settings getCopy() {
        Settings copy = new Settings();
        copy.setUsers(getUsers());

        copy.setMaxAnswersCount(getMaxAnswersCount());
        copy.setMinCorrectAnswers(getMinCorrectAnswers());

        copy.setMinBookCount(getMinBookCount());
        copy.setMaxBookCount(getMaxBookCount());
        copy.setMaxPicks(getMaxPicks());
        copy.setPrizeCount(getPrizeCount());

        copy.setGroupContestStartDate(getGroupContestStartDate());
        copy.setMinMembers(getMinMembers());

        copy.setLanguages(getLanguages());

        return copy;
    }

    @Override
    public boolean isEqualTo(Settings other) {
        return this.getUsers().equals(other.getUsers())
            && this.getMaxAnswersCount() == other.getMaxAnswersCount()
            && this.getMinCorrectAnswers() == other.getMinCorrectAnswers()
            && this.getMinBookCount() == other.getMinBookCount()
            && this.getMaxBookCount() == other.getMaxBookCount()
            && this.getMaxPicks() == other.getMaxPicks()
            && this.getPrizeCount() == other.getPrizeCount()
            && this.getGroupContestStartDate().equals(other.getGroupContestStartDate())
            && this.getMinMembers() == other.getMinMembers()
            && this.getLanguages().equals(other.getLanguages());
    }

    @Override
    public void setValues(Settings other) {
        this.setUsers(other.getUsers());

        this.setMaxAnswersCount(other.getMaxAnswersCount());
        this.setMinCorrectAnswers(other.getMinCorrectAnswers());

        this.setMinBookCount(other.getMinBookCount());
        this.setMaxBookCount(other.getMaxBookCount());
        this.setMaxPicks(other.getMaxPicks());
        this.setPrizeCount(other.getPrizeCount());

        this.setGroupContestStartDate(other.getGroupContestStartDate());
        this.setMinMembers(other.getMinMembers());

        this.setLanguages(other.getLanguages());
    }

    int getMaxAnswersCount() {
        return maxAnswersCount.get();
    }

    IntegerProperty maxAnswersCountProperty() {
        return maxAnswersCount;
    }

    void setMaxAnswersCount(int maxAnswersCount) {
        this.maxAnswersCount.set(maxAnswersCount);
    }

    int getMinCorrectAnswers() {
        return minCorrectAnswers.get();
    }

    IntegerProperty minCorrectAnswersProperty() {
        return minCorrectAnswers;
    }

    void setMinCorrectAnswers(int minCorrectAnswers) {
        this.minCorrectAnswers.set(minCorrectAnswers);
    }

    int getMinBookCount() {
        return minBookCount.get();
    }

    IntegerProperty minBookCountProperty() {
        return minBookCount;
    }

    void setMinBookCount(int minBookCount) {
        this.minBookCount.set(minBookCount);
    }

    int getMaxBookCount() {
        return maxBookCount.get();
    }

    IntegerProperty maxBookCountProperty() {
        return maxBookCount;
    }

    void setMaxBookCount(int maxBookCount) {
        this.maxBookCount.set(maxBookCount);
    }

    int getMaxPicks() {
        return maxPicks.get();
    }

    IntegerProperty maxPicksProperty() {
        return maxPicks;
    }

    void setMaxPicks(int maxPicks) {
        this.maxPicks.set(maxPicks);
    }

    int getPrizeCount() {
        return prizeCount.get();
    }

    IntegerProperty prizeCountProperty() {
        return prizeCount;
    }

    void setPrizeCount(int prizeCount) {
        this.prizeCount.set(prizeCount);
    }

    LocalDate getGroupContestStartDate() {
        return groupContestStartDate;
    }

    String getGroupContestStartDateAsString() {
        return groupContestStartDate.format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
    }

    void setGroupContestStartDate(LocalDate date) {
        groupContestStartDate = date;
    }

    ObservableList<String> getLanguages() {
        return languages;
    }

    void setLanguages(ObservableList<String> languages) {
        this.languages = languages;
    }

    ObservableList<String> getUsers(){
        return users;
    }

    void setUsers(ObservableList<String> users){
        this.users = users;
    }

    int getMinMembers() {
        return minMembers.get();
    }

    IntegerProperty minMembersProperty() {
        return minMembers;
    }

    void setMinMembers(int minMembers) {
        this.minMembers.set(minMembers);
    }

    Element getXMLNode(Document doc, Element settings){
        Element prizeCountElement = doc.createElement("PrizeCount");
        Element maxPicksElement = doc.createElement("MaxPicks");
        Element maxAnswersCountElement = doc.createElement("MaxAnswersCount");
        Element minCorrectAnswersElement = doc.createElement("MinCorrectAnswers");
        Element minBookCountElement = doc.createElement("MinBookCount");
        Element maxBookCountElement = doc.createElement("MaxBookCount");
        Element groupContestStartDateElement = doc.createElement("GroupContestStartDate");
        Element languagesElement = doc.createElement("Languages");
        Element usersElement = doc.createElement("Users");

        prizeCountElement.appendChild(doc.createTextNode("" + getPrizeCount()));
        maxPicksElement.appendChild(doc.createTextNode(getMaxPicks() + ""));
        maxAnswersCountElement.appendChild(doc.createTextNode(getMaxAnswersCount() + ""));
        minCorrectAnswersElement.appendChild(doc.createTextNode(getMinCorrectAnswers() + ""));
        minBookCountElement.appendChild(doc.createTextNode(getMinBookCount() + ""));
        maxBookCountElement.appendChild(doc.createTextNode(getMaxBookCount() + ""));
        groupContestStartDateElement.appendChild(doc.createTextNode(getGroupContestStartDateAsString()));

        ObservableList<String> languages = getLanguages();
        for (String language: languages) {
            Element languageElement = doc.createElement("Language");
            languageElement.appendChild(doc.createTextNode(language));
            languagesElement.appendChild(languageElement);
        }

        ObservableList<String> users = getUsers();
        for (String user: users) {
            Element userElement = doc.createElement("User");
            userElement.appendChild(doc.createTextNode(user));
            usersElement.appendChild(userElement);
        }

        settings.appendChild(prizeCountElement);
        settings.appendChild(maxPicksElement);
        settings.appendChild(maxAnswersCountElement);
        settings.appendChild(minCorrectAnswersElement);
        settings.appendChild(minBookCountElement);
        settings.appendChild(maxBookCountElement);
        settings.appendChild(groupContestStartDateElement);
        settings.appendChild(languagesElement);
        settings.appendChild(usersElement);

        return settings;
    }
}
