package sample.backend.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sample.backend.Saveable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Settings implements Saveable {
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

    public Element getXMLNode(Document doc, Element settings){
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
