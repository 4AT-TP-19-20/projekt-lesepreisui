package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SettingsTab {
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
    private static LocalDate groupContestStartDate;

    //Book
    private static ObservableList<String> languages = FXCollections.observableArrayList("Deutsch", "Englisch", "Italienisch", "Französisch", "Russisch");

    public static GridPane generate() {
        GridPane gridPane = new GridPane();

        gridPane.add(new Label("Prüfungeneinstellungen"),0,0);
        gridPane.add(new Label("Anzahl an gestellten Fragen"),0,1);
        TextField txt_maxAnswerCount = new TextField();
        txt_maxAnswerCount.textProperty().bindBidirectional(maxAnswersCount, new StringToInt());
        gridPane.add(txt_maxAnswerCount,1,1);
        gridPane.add(new Label("Mindestanzahl an richtigen Antworten"),0,2);
        TextField txt_minCorrectAnswers = new TextField();
        txt_minCorrectAnswers.textProperty().bindBidirectional(minCorrectAnswers, new StringToInt());
        gridPane.add(txt_minCorrectAnswers,1,2);
        gridPane.add(new Canvas(1,20),0,3);

        gridPane.add(new Label("Verlosungseinstellungen"),0,4);
        gridPane.add(new Label("Mindestanzahl gelesener Bücher"),0,5);
        TextField txt_minBookCount = new TextField();
        txt_minBookCount.textProperty().bindBidirectional(minBookCount, new StringToInt());
        gridPane.add(txt_minBookCount,1,5);
        gridPane.add(new Label("Maximalanzahl gelesener Bücher"),0,6);
        TextField txt_maxBookCount = new TextField();
        txt_maxBookCount.textProperty().bindBidirectional(maxBookCount, new StringToInt());
        gridPane.add(txt_maxBookCount,1,6);
        gridPane.add(new Label("Maximale Anzahl an Ziehungen pro Person"),0,7);
        TextField txt_maxPicks = new TextField();
        txt_maxPicks.textProperty().bindBidirectional(maxPicks, new StringToInt());
        gridPane.add(txt_maxPicks,1,7);
        gridPane.add(new Label("Anzahl vorhandener Preise"),0,8);
        TextField txt_prizeCount = new TextField();
        txt_prizeCount.textProperty().bindBidirectional(prizeCount, new StringToInt());
        gridPane.add(txt_prizeCount,1,8);

        gridPane.add(new Canvas(1,20),0,9);

        gridPane.add(new Label("Bucheinstellungen"),0,10);
        gridPane.add(new EditableListView("Sprachen", "Neue Sprache", languages),0,11);

        gridPane.add(new Canvas(1,20),0,12);

        gridPane.add(new Label("Generelle Einstellugen"),0,13);
        gridPane.add(new EditableListView("Benutzer", "Neuer Benutzer", users),0,14);

        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        return gridPane;
    }

    public static int getMaxAnswersCount() {
        return maxAnswersCount.get();
    }

    public static IntegerProperty maxAnswersCountProperty() {
        return maxAnswersCount;
    }

    public static void setMaxAnswersCount(int maxAnswersCount) {
        SettingsTab.maxAnswersCount.set(maxAnswersCount);
    }

    public static int getMinCorrectAnswers() {
        return minCorrectAnswers.get();
    }

    public static IntegerProperty minCorrectAnswersProperty() {
        return minCorrectAnswers;
    }

    public static void setMinCorrectAnswers(int minCorrectAnswers) {
        SettingsTab.minCorrectAnswers.set(minCorrectAnswers);
    }

    public static int getMinBookCount() {
        return minBookCount.get();
    }

    public static IntegerProperty minBookCountProperty() {
        return minBookCount;
    }

    public static void setMinBookCount(int minBookCount) {
        SettingsTab.minBookCount.set(minBookCount);
    }

    public static int getMaxBookCount() {
        return maxBookCount.get();
    }

    public static IntegerProperty maxBookCountProperty() {
        return maxBookCount;
    }

    public static void setMaxBookCount(int maxBookCount) {
        SettingsTab.maxBookCount.set(maxBookCount);
    }

    public static int getMaxPicks() {
        return maxPicks.get();
    }

    public static IntegerProperty maxPicksProperty() {
        return maxPicks;
    }

    public static void setMaxPicks(int maxPicks) {
        SettingsTab.maxPicks.set(maxPicks);
    }

    public static int getPrizeCount() {
        return prizeCount.get();
    }

    public static IntegerProperty prizeCountProperty() {
        return prizeCount;
    }

    public static void setPrizeCount(int prizeCount) {
        SettingsTab.prizeCount.set(prizeCount);
    }

    public static String getGroupContestStartDate() {
        return groupContestStartDate.format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
    }

    public static void setGroupContestStartDate(LocalDate date) {
        SettingsTab.groupContestStartDate = date;
    }

    public static ObservableList<String> getLanguages() {
        return languages;
    }

    public static void setLanguages(ObservableList<String> languages) {
        SettingsTab.languages = languages;
    }

    public static Element getXMLNode(Document doc){
        Element settings = doc.createElement("Settings");
        Element prizeCountElement = doc.createElement("PrizeCount");
        Element maxPicksElement = doc.createElement("MaxPicks");
        Element maxAnswersCountElement = doc.createElement("MaxAnswersCount");
        Element minCorrectAnswersElement = doc.createElement("MinCorrectAnswers");
        Element minBookCountElement = doc.createElement("MinBookCount");
        Element maxBookCountElement = doc.createElement("MaxBookCount");
        Element groupContestStartDateElement = doc.createElement("GroupContestStartDate");
        Element languagesElement = doc.createElement("Languages");
        Element languageElement = doc.createElement("Language");

        prizeCountElement.appendChild(doc.createTextNode("" + SettingsTab.getPrizeCount()));
        maxPicksElement.appendChild(doc.createTextNode(SettingsTab.getMaxPicks() + ""));
        maxAnswersCountElement.appendChild(doc.createTextNode(SettingsTab.getMaxAnswersCount() + ""));
        minCorrectAnswersElement.appendChild(doc.createTextNode(SettingsTab.getMinCorrectAnswers() + ""));
        minBookCountElement.appendChild(doc.createTextNode(SettingsTab.getMinBookCount() + ""));
        maxBookCountElement.appendChild(doc.createTextNode(SettingsTab.getMaxBookCount() + ""));
        groupContestStartDateElement.appendChild(doc.createTextNode(SettingsTab.getGroupContestStartDate()));

        ObservableList<String> languages = SettingsTab.getLanguages();
        for (String language: languages) {
            languageElement.appendChild(doc.createTextNode(language));
            languagesElement.appendChild(languageElement);
        }

        settings.appendChild(prizeCountElement);
        settings.appendChild(maxPicksElement);
        settings.appendChild(maxAnswersCountElement);
        settings.appendChild(minCorrectAnswersElement);
        settings.appendChild(minBookCountElement);
        settings.appendChild(maxBookCountElement);
        settings.appendChild(groupContestStartDateElement);
        settings.appendChild(languagesElement);

        return settings;
    }
}
