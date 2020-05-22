package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SettingsTab extends VBox{
    //General
    public static ObservableList<String> users = FXCollections.observableArrayList("Dorothea", "Petra");

    //Exam
    private static IntegerProperty maxAnswersCount = new SimpleIntegerProperty(6);
    private static IntegerProperty minCorrectAnswers = new SimpleIntegerProperty(4);

    //Drawing
    private static IntegerProperty minBookCount = new SimpleIntegerProperty(1);
    private static IntegerProperty maxBookCount = new SimpleIntegerProperty(40);
    private static IntegerProperty maxPicks = new SimpleIntegerProperty(3);
    private static IntegerProperty prizeCount = new SimpleIntegerProperty(10);

    //Group contest
    private static LocalDate groupContestStartDate = LocalDate.parse("01.01.2020", DateTimeFormatter.ofPattern("dd.MM.uuuu"));
    private static IntegerProperty minMembers = new SimpleIntegerProperty(2);

    //Book
    private static ObservableList<String> languages = FXCollections.observableArrayList("Deutsch", "Englisch", "Italienisch", "Französisch", "Russisch");

    public SettingsTab() {
        Label einstellungen = new Label("Einstellungen");
        einstellungen.setId("h1");
        einstellungen.setPadding(new Insets(0,0,0,15));
        ImageView settingsIcon = new ImageView(new Image("icons8-settings-144.png"));
        settingsIcon.setFitWidth(40);
        settingsIcon.setPreserveRatio(true);
        HBox heading = new HBox(settingsIcon, einstellungen);
        heading.setAlignment(Pos.TOP_CENTER);
        this.getChildren().add(heading);

        Label prüfungsEinstellungen = new Label("Prüfungeneinstellungen");
        prüfungsEinstellungen.setId("h2");
        prüfungsEinstellungen.setPadding(new Insets(0,0,0,15));
        ImageView prüfungIcon = new ImageView(new Image("icons8-quiz-100.png"));
        prüfungIcon.setFitWidth(25);
        prüfungIcon.setPreserveRatio(true);
        HBox prüfungsBox = new HBox(prüfungIcon, prüfungsEinstellungen);
        prüfungsBox.setAlignment(Pos.TOP_CENTER);
        prüfungsBox.setPadding(new Insets(30,0,0,0));
        this.getChildren().add(prüfungsBox);

        this.getChildren().add(new Label("Anzahl an gestellten Fragen"));
        TextField txt_maxAnswerCount = new TextField();
        txt_maxAnswerCount.setId("tf");
        txt_maxAnswerCount.textProperty().bindBidirectional(maxAnswersCount, new StringToInt());
        this.getChildren().add(txt_maxAnswerCount);
        this.getChildren().add(new Label("Mindestanzahl an richtigen Antworten"));
        TextField txt_minCorrectAnswers = new TextField();
        txt_minCorrectAnswers.setId("tf");
        txt_minCorrectAnswers.textProperty().bindBidirectional(minCorrectAnswers, new StringToInt());
        this.getChildren().add(txt_minCorrectAnswers);

        Label verlosungsEinstellungen = new Label("Verlosungseinstellungen");
        verlosungsEinstellungen.setId("h2");
        verlosungsEinstellungen.setPadding(new Insets(0,0,0,15));
        ImageView verlosungsIcon = new ImageView(new Image("icons8-clover-96.png"));
        verlosungsIcon.setFitWidth(25);
        verlosungsIcon.setPreserveRatio(true);
        HBox verlosungsBox = new HBox(verlosungsIcon, verlosungsEinstellungen);
        verlosungsBox.setAlignment(Pos.TOP_CENTER);
        verlosungsBox.setPadding(new Insets(30,0,0,0));
        this.getChildren().add(verlosungsBox);

        this.getChildren().add(new Label("Mindestanzahl gelesener Bücher"));
        TextField txt_minBookCount = new TextField();
        txt_minBookCount.setId("tf");
        txt_minBookCount.textProperty().bindBidirectional(minBookCount, new StringToInt());
        this.getChildren().add(txt_minBookCount);
        this.getChildren().add(new Label("Maximalanzahl gelesener Bücher"));
        TextField txt_maxBookCount = new TextField();
        txt_maxBookCount.setId("tf");
        txt_maxBookCount.textProperty().bindBidirectional(maxBookCount, new StringToInt());
        this.getChildren().add(txt_maxBookCount);
        this.getChildren().add(new Label("Maximale Anzahl an Ziehungen pro Person"));
        TextField txt_maxPicks = new TextField();
        txt_maxPicks.setId("tf");
        txt_maxPicks.textProperty().bindBidirectional(maxPicks, new StringToInt());
        this.getChildren().add(txt_maxPicks);
        this.getChildren().add(new Label("Anzahl vorhandener Preise"));
        TextField txt_prizeCount = new TextField();
        txt_prizeCount.setId("tf");
        txt_prizeCount.textProperty().bindBidirectional(prizeCount, new StringToInt());
        this.getChildren().add(txt_prizeCount);

        Label buchEinstellungen = new Label("Bucheinstellungen");
        buchEinstellungen.setId("h2");
        buchEinstellungen.setPadding(new Insets(0,0,10,15));
        ImageView buchIcon = new ImageView(new Image("icons8-book-100.png"));
        buchIcon.setFitWidth(25);
        buchIcon.setPreserveRatio(true);
        HBox buchBox = new HBox(buchIcon, buchEinstellungen);
        buchBox.setAlignment(Pos.TOP_CENTER);
        buchBox.setPadding(new Insets(30,0,0,0));
        this.getChildren().add(buchBox);

        EditableListView languageList = new EditableListView("Sprachen", "Neue Sprache", languages, "icons8-language-100.png");
        languageList.setMaxWidth(400);
        this.getChildren().add(languageList);

        Label generalEinstellungen = new Label("Generelle Einstellungen");
        generalEinstellungen.setId("h2");
        generalEinstellungen.setPadding(new Insets(0,0,10,15));
        ImageView generalIcon = new ImageView(new Image("icons8-settings-144.png"));
        generalIcon.setFitWidth(25);
        generalIcon.setPreserveRatio(true);
        HBox generalBox = new HBox(generalIcon, generalEinstellungen);
        generalBox.setAlignment(Pos.TOP_CENTER);
        generalBox.setPadding(new Insets(30,0,0,0));
        this.getChildren().add(generalBox);

        EditableListView userList = new EditableListView("Benutzer", "Neuer Benutzer", users, "icons8-user-100.png");
        userList.setMaxWidth(400);
        this.getChildren().add(userList);

        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(10));
        this.setSpacing(5);
    }

    public static int getMaxAnswersCount() {
        return maxAnswersCount.get();
    }

    public static IntegerProperty maxAnswersCountProperty() {
        return maxAnswersCount;
    }

    public static void setMaxAnswersCount(int maxAnswersCount) {
        SettingsTab.maxAnswersCount = new SimpleIntegerProperty(maxAnswersCount);
    }

    public static int getMinCorrectAnswers() {
        return minCorrectAnswers.get();
    }

    public static IntegerProperty minCorrectAnswersProperty() {
        return minCorrectAnswers;
    }

    public static void setMinCorrectAnswers(int minCorrectAnswers) {
        SettingsTab.minCorrectAnswers = new SimpleIntegerProperty(minCorrectAnswers);
    }

    public static int getMinBookCount() {
        return minBookCount.get();
    }

    public static IntegerProperty minBookCountProperty() {
        return minBookCount;
    }

    public static void setMinBookCount(int minBookCount) {
        SettingsTab.minBookCount = new SimpleIntegerProperty(minBookCount);
    }

    public static int getMaxBookCount() {
        return maxBookCount.get();
    }

    public static IntegerProperty maxBookCountProperty() {
        return maxBookCount;
    }

    public static void setMaxBookCount(int maxBookCount) {
        SettingsTab.maxBookCount = new SimpleIntegerProperty(maxBookCount);
    }

    public static int getMaxPicks() {
        return maxPicks.get();
    }

    public static IntegerProperty maxPicksProperty() {
        return maxPicks;
    }

    public static void setMaxPicks(int maxPicks) {
        SettingsTab.maxPicks = new SimpleIntegerProperty(maxPicks);
    }

    public static int getPrizeCount() {
        return prizeCount.get();
    }

    public static IntegerProperty prizeCountProperty() {
        return prizeCount;
    }

    public static void setPrizeCount(int prizeCount) {
        SettingsTab.prizeCount = new SimpleIntegerProperty(prizeCount);
    }

    public static LocalDate getGroupContestStartDate() {
        return groupContestStartDate;
    }

    public static String getGroupContestStartDateAsString() {
        return groupContestStartDate.format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
    }

    public static void setGroupContestStartDate(LocalDate date) {
        SettingsTab.groupContestStartDate = date;
    }

    public static ObservableList<String> getLanguages() {
        return languages;
    }

    public static void setLanguages(ObservableList<String> languages) {
        SettingsTab.languages = FXCollections.observableArrayList();
        SettingsTab.languages = languages;
    }

    public static ObservableList<String> getUsers(){
        return users;
    }

    public static void setUsers(ObservableList<String> users){
        SettingsTab.users = users;
    }

    public static Element getXMLNode(Document doc, Element settings){
        Element prizeCountElement = doc.createElement("PrizeCount");
        Element maxPicksElement = doc.createElement("MaxPicks");
        Element maxAnswersCountElement = doc.createElement("MaxAnswersCount");
        Element minCorrectAnswersElement = doc.createElement("MinCorrectAnswers");
        Element minBookCountElement = doc.createElement("MinBookCount");
        Element maxBookCountElement = doc.createElement("MaxBookCount");
        Element groupContestStartDateElement = doc.createElement("GroupContestStartDate");
        Element languagesElement = doc.createElement("Languages");
        Element usersElement = doc.createElement("Users");

        prizeCountElement.appendChild(doc.createTextNode("" + SettingsTab.getPrizeCount()));
        maxPicksElement.appendChild(doc.createTextNode(SettingsTab.getMaxPicks() + ""));
        maxAnswersCountElement.appendChild(doc.createTextNode(SettingsTab.getMaxAnswersCount() + ""));
        minCorrectAnswersElement.appendChild(doc.createTextNode(SettingsTab.getMinCorrectAnswers() + ""));
        minBookCountElement.appendChild(doc.createTextNode(SettingsTab.getMinBookCount() + ""));
        maxBookCountElement.appendChild(doc.createTextNode(SettingsTab.getMaxBookCount() + ""));
        groupContestStartDateElement.appendChild(doc.createTextNode(SettingsTab.getGroupContestStartDateAsString()));

        ObservableList<String> languages = SettingsTab.getLanguages();
        for (String language: languages) {
            Element languageElement = doc.createElement("Language");
            languageElement.appendChild(doc.createTextNode(language));
            languagesElement.appendChild(languageElement);
        }

        ObservableList<String> users = SettingsTab.getUsers();
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

    public static int getMinMembers() {
        return minMembers.get();
    }

    public static IntegerProperty minMembersProperty() {
        return minMembers;
    }

    public static void setMinMembers(int minMembers) {
        SettingsTab.minMembers.set(minMembers);
    }
}
