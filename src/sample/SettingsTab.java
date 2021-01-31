package sample;

import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

class SettingsTab extends VBox implements ChildSaveable {
    SettingsTab() {
        this.getChildren().add(new SettingsHeader("Einstellungen",
                "h1", "icons8-settings-144.png", 40, false));

        this.getChildren().add(new SettingsHeader("Prüfungseinstellungen",
                "h2", "icons8-quiz-100.png", 25, true));

        this.getChildren().add(new Label("Mindestanzahl an richtigen Antworten"));
        this.getChildren().add(new IntField(Data.settings.minCorrectAnswersProperty()));

        this.getChildren().add(new SettingsHeader("Gruppeneinstellungen",
                "h2", "icons8-user-100.png", 25, true));

        this.getChildren().add(new Label("Mindestanzahl an Teilnehmern einer Gruppe"));
        this.getChildren().add(new IntField(Data.settings.minMembersProperty()));

        this.getChildren().add(new SettingsHeader("Verlosungseinstellungen",
                "h2", "icons8-clover-96.png", 25, true));

        this.getChildren().add(new Label("Mindestanzahl gelesener Bücher"));
        this.getChildren().add(new IntField(Data.settings.minBookCountProperty()));

        this.getChildren().add(new Label("Maximalanzahl gelesener Bücher"));
        this.getChildren().add(new IntField(Data.settings.maxBookCountProperty()));

        this.getChildren().add(new Label("Maximale Anzahl an Ziehungen pro Person"));
        this.getChildren().add(new IntField(Data.settings.maxPicksProperty()));

        this.getChildren().add(new Label("Anzahl vorhandener Preise"));
        this.getChildren().add(new IntField(Data.settings.prizeCountProperty()));

        this.getChildren().add(new SettingsHeader("Bucheinstellungen",
                "h2", "icons8-book-100.png", 25, true));

        EditableListView languageList = new EditableListView("Sprachen",
                "Neue Sprache", Data.settings.getLanguages(), "icons8-language-100.png");
        languageList.setMaxWidth(400);
        this.getChildren().add(languageList);

        this.getChildren().add(new SettingsHeader("Generelle Einstellungen",
                "h2", "icons8-settings-144.png", 25, true));

        EditableListView userList = new EditableListView("Benutzer",
                "Neuer Benutzer", Data.settings.getUsers(), "icons8-user-100.png");
        userList.setMaxWidth(400);
        this.getChildren().add(userList);

        CheckBox cbx_useAnd = new CheckBox("Schlüsselwörter in der Suche mit UND verknüpfen, sodass alle Wörter vorkommen müssen");
        cbx_useAnd.selectedProperty().bindBidirectional(Data.settings.useAndInSearchProperty());
        this.getChildren().add(cbx_useAnd);

        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(10));
        this.setSpacing(5);
    }

    @Override
    public Saveable getSaveable() {
        return Data.settings;
    }

    static class SettingsHeader extends HBox {
        SettingsHeader(String text,
                       String textId,
                       String iconUrl,
                       double iconWidth,
                       boolean hasHeaderPadding) {

            Label label = new Label(text);
            label.setId(textId);
            label.setPadding(new Insets(0,0,0,15));

            ImageView imageView = new ImageView(new Image(iconUrl));
            imageView.setFitWidth(iconWidth);
            imageView.setPreserveRatio(true);

            this.getChildren().addAll(label, imageView);
            this.setAlignment(Pos.TOP_CENTER);
            if(hasHeaderPadding) {
                this.setPadding(new Insets(30,0,0,0));
            }
        }
    }

    static class IntField extends TextField {
        IntField(Property<Number> property) {
            this.setId("tf");
            this.setText(String.valueOf(property.getValue()));
            this.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    property.setValue(Integer.parseInt(newValue));
                    this.setStyle(null);
                }
                catch (NumberFormatException ex) {
                    this.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
                }
            });
            this.textProperty().bindBidirectional(property, StringToInt.getInstance());
        }
    }
}
