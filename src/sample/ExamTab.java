package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ExamTab {
    public static BorderPane generate() {
        BorderPane borderPane = new BorderPane();

        //Top
        TextField txt_search = new TextField();
        txt_search.setPromptText("Suche nach ...");
        borderPane.setTop(txt_search);

        //Center
        TableView<Exam> tbv_exams = new TableView<>();
        TableColumn<Exam, String> column_firstName = new TableColumn<>("Vorname");
        column_firstName.setCellFactory(param -> new AlignedTableCell<>());
        column_firstName.setCellValueFactory(param -> param.getValue().getContestant().firstNameProperty());
        TableColumn<Exam, String> column_lastName = new TableColumn<>("Nachname");
        column_lastName.setCellFactory(param -> new AlignedTableCell<>());
        column_lastName.setCellValueFactory(param -> param.getValue().getContestant().lastNameProperty());
        TableColumn<Exam, String> column_grade = new TableColumn<>("Klasse");
        column_grade.setCellFactory(param -> new AlignedTableCell<>());
        column_grade.setCellValueFactory(param -> param.getValue().getContestant().gradeProperty());
        TableColumn<Exam, String> column_title = new TableColumn<>("Titel");
        column_title.setCellFactory(param -> new AlignedTableCell<>());
        column_title.setCellValueFactory(param -> param.getValue().getBook().titleProperty());
        TableColumn<Exam, String> column_authorFirstName = new TableColumn<>("Vorname Autor");
        column_authorFirstName.setCellFactory(param -> new AlignedTableCell<>());
        column_authorFirstName.setCellValueFactory(param -> param.getValue().getBook().authorFirstNameProperty());
        TableColumn<Exam, String> column_authorLastName = new TableColumn<>("Nachname Autor");
        column_authorLastName.setCellFactory(param -> new AlignedTableCell<>());
        column_authorLastName.setCellValueFactory(param -> param.getValue().getBook().authorLastNameProperty());
        TableColumn<Exam, String> column_language = new TableColumn<>("Sprache");
        column_language.setCellFactory(param -> new AlignedTableCell<>());
        column_language.setCellValueFactory(param -> param.getValue().getBook().languageProperty());
        TableColumn<Exam, String> column_date = new TableColumn<>("Datum");
        column_date.setCellFactory(param -> new AlignedTableCell<>());
        column_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Exam, String> column_points = new TableColumn<>("Punkte");
        column_points.setCellFactory(param -> new AlignedTableCell<>());
        column_points.setCellValueFactory(new PropertyValueFactory<>("points"));
        TableColumn<Exam, StackPane> column_passed = new TableColumn<>("Bestanden");
        column_passed.setCellFactory(param -> new AlignedTableCell<>());
        column_passed.setCellValueFactory(param -> new SwitchBox(param.getValue().passedProperty(), "small", false, true));
        TableColumn<Exam, String> column_librarian = new TableColumn<>("BibliothekarIn");
        column_librarian.setCellFactory(param -> new AlignedTableCell<>());
        column_librarian.setCellValueFactory(new PropertyValueFactory<>("librarian"));
        TableColumn<Exam, HBox> column_answers = new TableColumn<>("Antworten");
        column_answers.setCellFactory(param -> new AlignedTableCell<>());
        column_answers.setCellValueFactory(param -> new AnswerBoxes(param.getValue().answersProperty(), false, "small"));
        borderPane.setCenter(tbv_exams);

        //Bottom
        Button btn_removeExam = new Button("Prüfung löschen");
        btn_removeExam.setId("custom-button");
        borderPane.setBottom(btn_removeExam);

        tbv_exams.getColumns().addAll(column_firstName, column_lastName, column_grade, column_title, column_authorFirstName, column_authorLastName, column_language, column_date, column_points, column_passed, column_answers);
        tbv_exams.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for(Contestant contestant : Data.contestants) {
            tbv_exams.getItems().addAll(contestant.getExams());
        }
        tbv_exams.getSortOrder().add(column_date);
        tbv_exams.sort();
        BorderPane.setMargin(borderPane.getCenter(), new Insets(10,0,10,0));
        borderPane.setPadding(new Insets(10));

        return borderPane;
    }
}
