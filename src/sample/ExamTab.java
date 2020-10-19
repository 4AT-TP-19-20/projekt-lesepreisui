package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

class ExamTab extends BorderPane {
    private CustomTableView<Exam> tbv_exams;

    ExamTab() {
        //Top
        TextField txt_search = new TextField();
        txt_search.setPromptText("Suche nach ...");
        this.setTop(txt_search);

        //Center
        tbv_exams = new CustomTableView<>();

        tbv_exams.addColumn("Vorname", "", param -> param.getValue().getContestant().firstNameProperty());
        tbv_exams.addColumn("Nachname", "", param -> param.getValue().getContestant().lastNameProperty());
        tbv_exams.addColumn("Klasse", "", param -> param.getValue().getContestant().gradeProperty());
        tbv_exams.addColumn("Titel", "", param -> param.getValue().getBook().titleProperty());
        tbv_exams.addColumn("Vorname Autor", "", param -> param.getValue().getBook().authorFirstNameProperty());
        tbv_exams.addColumn("Nachname Autor", "", param -> param.getValue().getBook().authorLastNameProperty());
        tbv_exams.addColumn("Sprache", "", param -> param.getValue().getBook().languageProperty());
        tbv_exams.addColumn("Datum", "", new PropertyValueFactory<>("date"));
        tbv_exams.addColumn("Punkte", 0, new PropertyValueFactory<>("points"));
        tbv_exams.addColumn("Bestanden", new StackPane(), param -> new SwitchBox(param.getValue().passedProperty(), "small", false, true));
        tbv_exams.addColumn("BibliothekarIn", "", new PropertyValueFactory<>("librarian"));
        tbv_exams.addColumn("Antworten", new HBox(), param -> new AnswerBoxes(param.getValue().answersProperty(), false, "small"));

        for(Contestant contestant : Data.contestants) {
            tbv_exams.getItems().addAll(contestant.getExams());
        }
        tbv_exams.getSortOrder().add(tbv_exams.getColumns().get(7));
        tbv_exams.sort();

        this.setCenter(tbv_exams);

        //Bottom
        Button btn_removeExam = new Button("Prüfung löschen");
        btn_removeExam.setOnAction(e -> btn_removeExamAction());
        btn_removeExam.setId("custom-button");
        this.setBottom(btn_removeExam);

        BorderPane.setMargin(this.getCenter(), new Insets(10,0,10,0));
        this.setPadding(new Insets(10));
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
}
