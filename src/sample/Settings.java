package sample;

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

public class Settings implements Saveable {
    //General
    private ObservableList<String> users = FXCollections.observableArrayList("Dorothea", "Petra");
    private BooleanProperty useAndInSearch = new SimpleBooleanProperty(true);

    //Exam
    private final int maxAnswersCount = 6;
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
        copy.setUseAndInSearch(useAndInSearch());

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
    public boolean equals(Object other) {
        if(other instanceof Settings) {
            Settings otherSettings = (Settings) other;
            return this.getUsers().equals(otherSettings.getUsers())
                    && this.useAndInSearch() == otherSettings.useAndInSearch()
                    && this.getMinCorrectAnswers() == otherSettings.getMinCorrectAnswers()
                    && this.getMinBookCount() == otherSettings.getMinBookCount()
                    && this.getMaxBookCount() == otherSettings.getMaxBookCount()
                    && this.getMaxPicks() == otherSettings.getMaxPicks()
                    && this.getPrizeCount() == otherSettings.getPrizeCount()
                    && this.getGroupContestStartDate().equals(otherSettings.getGroupContestStartDate())
                    && this.getMinMembers() == otherSettings.getMinMembers()
                    && this.getLanguages().equals(otherSettings.getLanguages());
        }
        return false;
    }

    @Override
    public void setValues(Saveable other) {
        if(other instanceof Settings) {
            Settings otherSettings = (Settings) other;
            this.setUsers(otherSettings.getUsers());
            this.setUseAndInSearch(otherSettings.useAndInSearch());

            this.setMinCorrectAnswers(otherSettings.getMinCorrectAnswers());

            this.setMinBookCount(otherSettings.getMinBookCount());
            this.setMaxBookCount(otherSettings.getMaxBookCount());
            this.setMaxPicks(otherSettings.getMaxPicks());
            this.setPrizeCount(otherSettings.getPrizeCount());

            this.setGroupContestStartDate(otherSettings.getGroupContestStartDate());
            this.setMinMembers(otherSettings.getMinMembers());

            this.setLanguages(otherSettings.getLanguages());
        }
        else {
            throw new RuntimeException("Passed Saveable is not instance of Settings");
        }
    }

    public int getMaxAnswersCount() {
        return maxAnswersCount;
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
        this.languages.setAll(languages);
    }

    ObservableList<String> getUsers(){
        return users;
    }

    void setUsers(ObservableList<String> users){
        this.users.setAll(users);
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

    boolean useAndInSearch() {
        return useAndInSearch.get();
    }

    BooleanProperty useAndInSearchProperty() {
        return useAndInSearch;
    }

    void setUseAndInSearch(boolean useAndInSearch) {
        this.useAndInSearch.set(useAndInSearch);
    }

    Element getXMLNode(Document doc, Element settings){
        Element prizeCountElement = doc.createElement("PrizeCount");
        Element maxPicksElement = doc.createElement("MaxPicks");
        Element minCorrectAnswersElement = doc.createElement("MinCorrectAnswers");
        Element minBookCountElement = doc.createElement("MinBookCount");
        Element maxBookCountElement = doc.createElement("MaxBookCount");
        Element groupContestStartDateElement = doc.createElement("GroupContestStartDate");
        Element minMembersElement = doc.createElement("MinMembers");
        Element languagesElement = doc.createElement("Languages");
        Element usersElement = doc.createElement("Users");
        Element useAndInSearchElement = doc.createElement("UseAndInSearch");

        prizeCountElement.appendChild(doc.createTextNode("" + getPrizeCount()));
        maxPicksElement.appendChild(doc.createTextNode(getMaxPicks() + ""));
        minCorrectAnswersElement.appendChild(doc.createTextNode(getMinCorrectAnswers() + ""));
        minBookCountElement.appendChild(doc.createTextNode(getMinBookCount() + ""));
        maxBookCountElement.appendChild(doc.createTextNode(getMaxBookCount() + ""));
        groupContestStartDateElement.appendChild(doc.createTextNode(getGroupContestStartDateAsString()));
        minMembersElement.appendChild(doc.createTextNode(getMinMembers() + ""));
        useAndInSearchElement.appendChild(doc.createTextNode(useAndInSearch() + ""));

        ObservableList<String> languages = getLanguages();
        for (String language : languages) {
            Element languageElement = doc.createElement("Language");
            languageElement.appendChild(doc.createTextNode(language));
            languagesElement.appendChild(languageElement);
        }

        ObservableList<String> users = getUsers();
        for (String user : users) {
            Element userElement = doc.createElement("User");
            userElement.appendChild(doc.createTextNode(user));
            usersElement.appendChild(userElement);
        }

        settings.appendChild(prizeCountElement);
        settings.appendChild(maxPicksElement);
        settings.appendChild(minCorrectAnswersElement);
        settings.appendChild(minBookCountElement);
        settings.appendChild(maxBookCountElement);
        settings.appendChild(groupContestStartDateElement);
        settings.appendChild(minMembersElement);
        settings.appendChild(languagesElement);
        settings.appendChild(usersElement);
        settings.appendChild(useAndInSearchElement);

        return settings;
    }
}
