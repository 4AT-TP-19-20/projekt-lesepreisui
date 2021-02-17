package sample.frontend.tabs;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
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
import sample.frontend.controls.SwitchBox;

public class ExamTab extends BorderPane {
    private final CustomTableView<Exam> tbv_exams;

    public ExamTab() {
        //Top
        TextField txt_search = new TextField();
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

        for(Contestant contestant : Data.contestants) {
            tbv_exams.getItems().addAll(contestant.getExams());
        }
        tbv_exams.getSortOrder().add(tbv_exams.getColumns().get(7));
        tbv_exams.sort();

        this.setCenter(tbv_exams);

        Data.contestants.addListener(this::contestantsListener);

        //Bottom
        Button btn_removeExam = new Button("Prüfung löschen");
        btn_removeExam.setOnAction(e -> btn_removeExamAction());
        btn_removeExam.setId("red-button");
        this.setBottom(btn_removeExam);

        BorderPane.setMargin(this.getCenter(), new Insets(10,0,10,0));
        this.setPadding(new Insets(10));
    }

    private void contestantsListener(ListChangeListener.Change<? extends Contestant> c) {
        while(c.next()) {
            for(Contestant added : c.getAddedSubList()) {
                tbv_exams.getItems().addAll(added.getExams());
                added.getExams().addListener(this::examsListener);
            }
            for (Contestant removed : c.getRemoved()) {
                tbv_exams.getItems().removeAll(removed.getExams());
                removed.getExams().removeListener(this::examsListener);
            }
        }
    }

    private void examsListener(ListChangeListener.Change<? extends Exam> c) {
        while(c.next()) {
            tbv_exams.getItems().addAll(c.getAddedSubList());
            tbv_exams.getItems().removeAll(c.getRemoved());
        }
    }

    private void btn_removeExamAction() {
        if(!tbv_exams.getSelectionModel().isEmpty()) {
            for(Contestant contestant : Data.contestants) {
                for(Exam exam : contestant.getExams()) {
                    if(exam == tbv_exams.getSelectionModel().getSelectedItem()) {
                        contestant.removeExam(exam);
                        tbv_exams.getItems().remove(exam);
                        return;
                    }
                }
            }
        }
    }

    private void textChangeListener(String newValue) {
        newValue = newValue.trim().toLowerCase();
        tbv_exams.getItems().clear();

        if(newValue.isEmpty()) {
            for(Contestant contestant : Data.contestants) {
                tbv_exams.getItems().addAll(contestant.getExams());
            }
            tbv_exams.sort();
            return;
        }

        for(Contestant contestant : Data.contestants) {
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
}
