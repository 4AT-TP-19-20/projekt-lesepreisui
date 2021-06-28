package sample.frontend.tabs;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import sample.backend.Searchables;
import sample.backend.data.Contestant;
import sample.backend.data.Data;
import sample.backend.data.Exam;
import sample.frontend.controls.AnswerBoxes;
import sample.frontend.controls.CustomTableView;
import sample.frontend.controls.OpenNotified;
import sample.frontend.controls.SwitchBox;

public class ExamTab extends BorderPane implements OpenNotified {
    private final CustomTableView<Exam> tbv_exams;
    private final TextField txt_search;

    public ExamTab() {
        //Top
        txt_search = new TextField();
        txt_search.setPromptText("Suche nach Datum, Teilnehmer, Bücher, ...");
        txt_search.textProperty().addListener((observable, oldValue, newValue) -> textChangeListener(newValue));
        this.setTop(txt_search);

        //Center
        tbv_exams = new CustomTableView<>();

        tbv_exams.<String>addColumn("Vorname", param -> param.getValue().getContestant().firstNameProperty());
        tbv_exams.<String>addColumn("Nachname", param -> param.getValue().getContestant().lastNameProperty());
        tbv_exams.<String>addColumn("Klasse", param -> param.getValue().getContestant().gradeProperty());
        tbv_exams.<String>addColumn("Titel", param -> param.getValue().getBook().titleProperty());
        tbv_exams.<String>addColumn("Vorname Autor", param -> param.getValue().getBook().authorFirstNameProperty());
        tbv_exams.<String>addColumn("Nachname Autor",  param -> param.getValue().getBook().authorLastNameProperty());
        tbv_exams.<String>addColumn("Sprache", param -> param.getValue().getBook().languageProperty());
        tbv_exams.<String>addColumn("Datum", param -> new SimpleStringProperty(param.getValue().getDateAsString()));
        tbv_exams.<Integer>addColumn("Punkte", new PropertyValueFactory<>("points"));
        tbv_exams.<StackPane>addColumn("Bestanden", param -> new SwitchBox(param.getValue().passedProperty(), "small", false, true));
        tbv_exams.<String>addColumn("BibliothekarIn", new PropertyValueFactory<>("librarian"));
        tbv_exams.<HBox>addColumn("Antworten", param -> new AnswerBoxes(param.getValue().answersProperty(), false, "small"));

        tbv_exams.getItems().addAll(Data.exams.values());
        tbv_exams.getSortOrder().add(tbv_exams.getColumns().get(7));
        tbv_exams.sort();

        Data.exams.addListener((MapChangeListener<String, Exam>) change -> textChangeListener(txt_search.getText()));

        this.setCenter(tbv_exams);

        //Bottom
        Button btn_removeExam = new Button("Prüfung löschen");
        btn_removeExam.setOnAction(e -> btn_removeExamAction());
        btn_removeExam.setId("red-button");
        this.setBottom(btn_removeExam);

        BorderPane.setMargin(this.getCenter(), new Insets(10,0,10,0));
        this.setPadding(new Insets(10));
    }

    private void btn_removeExamAction() {
        if(!tbv_exams.getSelectionModel().isEmpty()) {
            tbv_exams.getSelectionModel().getSelectedItem().delete();
        }
    }

    private void textChangeListener(String newValue) {
        newValue = newValue.trim().toLowerCase();
        tbv_exams.getItems().clear();

        if(newValue.isEmpty()) {
            tbv_exams.getItems().addAll(Data.exams.values());
            tbv_exams.sort();
            return;
        }

        //If the match is a contestant, all corresponding exams should be listed
        for(Contestant contestant : Data.contestants.values()) {
            if(Searchables.contain(newValue, contestant)) {
                tbv_exams.getItems().addAll(contestant.getExams());
            }
            else {
                for(Exam exam : contestant.getExams()) {
                    if(Searchables.contain(newValue, contestant, exam)) {
                        tbv_exams.getItems().add(exam);
                    }
                }
            }
        }
    }

    @Override
    public void onOpen() {
        textChangeListener(txt_search.getText());
    }
}
