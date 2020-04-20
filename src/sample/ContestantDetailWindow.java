package sample;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ContestantDetailWindow {
    private static Stage stage;
    private static TextField txt_firstName;
    private static TextField txt_lastName;
    private static TextField txt_grade;
    private static TextField txt_group;
    private static TextField txt_points;
    private static TextField txt_bookCount;

    public static void showNewWindow(Contestant contestant) {
        stage = new Stage();
        BorderPane borderPane = new BorderPane();

        //Top
        GridPane topItems = new GridPane();

        topItems.add(new Label("Vorname"),0,0);
        txt_firstName = new TextField();
        txt_firstName.textProperty().bindBidirectional(contestant.firstNameProperty());
        topItems.add(txt_firstName, 1,0,3,1);

        topItems.add(new Label("Nachname"),0,1);
        txt_lastName = new TextField();
        txt_lastName.textProperty().bindBidirectional(contestant.lastNameProperty());
        topItems.add(txt_lastName, 1,1,3,1);

        topItems.add(new Label("Klasse"),0,2);
        txt_grade = new TextField();
        txt_grade.textProperty().bindBidirectional(contestant.gradeProperty());
        topItems.add(txt_grade,1,2);

        topItems.add(new Label("Gruppe"),2,2);
        txt_group = new TextField("TODO");
        topItems.add(txt_group,3,2);

        topItems.add(new Label("Gesamtpunkte"),0,3);
        txt_points = new TextField(String.valueOf(contestant.getPoints()));
        txt_points.setEditable(false);
        topItems.add(txt_points, 1, 3);

        topItems.add(new Label("Gelesene Bücher"),2,3);
        txt_bookCount = new TextField(String.valueOf(contestant.getBookCount()));
        txt_bookCount.setEditable(false);
        topItems.add(txt_bookCount, 3, 3);

        topItems.setHgap(5);
        topItems.setVgap(5);
        borderPane.setTop(topItems);

        //Center
        TableView<Exam> tbv_exams = new TableView<>();
        TableColumn<Exam, String> column_title = new TableColumn<>("Titel");
        column_title.setCellValueFactory(param -> ((Exam)((TableColumn.CellDataFeatures)param).getValue()).getBook().titleProperty());
        TableColumn<Exam, String> column_authorFirstName = new TableColumn<>("Vorname Author");
        column_authorFirstName.setCellValueFactory(param -> ((Exam)((TableColumn.CellDataFeatures)param).getValue()).getBook().authorFirstNameProperty());
        TableColumn<Exam, String> column_authorLastName = new TableColumn<>("Nachname Author");
        column_authorLastName.setCellValueFactory(param -> ((Exam)((TableColumn.CellDataFeatures)param).getValue()).getBook().authorLastNameProperty());
        TableColumn<Exam, String> column_language = new TableColumn<>("Sprache");
        column_language.setCellValueFactory(param -> ((Exam)((TableColumn.CellDataFeatures)param).getValue()).getBook().languageProperty());
        TableColumn<Exam, String> column_date = new TableColumn<>("Datum");
        column_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Exam, String> column_points = new TableColumn<>("Punkte");
        column_points.setCellValueFactory(new PropertyValueFactory<>("points"));
        TableColumn<Exam, String> column_passed = new TableColumn<>("Bestanden");
        column_passed.setCellValueFactory(new PropertyValueFactory<>("passed"));
        TableColumn<Exam, String> column_librarian = new TableColumn<>("Bibliothekar");
        column_librarian.setCellValueFactory(new PropertyValueFactory<>("librarian"));

        tbv_exams.getColumns().addAll(column_title, column_authorFirstName, column_authorLastName, column_language, column_date, column_points, column_passed);

        tbv_exams.setItems(contestant.getExams());
        borderPane.setCenter(tbv_exams);

        //Bottom
        Button btn_addExam = new Button("Neue Prüfung");
        btn_addExam.setMaxWidth(10000);
        borderPane.setBottom(btn_addExam);

        borderPane.setPadding(new Insets(10));
        BorderPane.setMargin(borderPane.getCenter(), new Insets(10,0,10,0));
        stage.setTitle("Detailansicht Teilnehmer");
        stage.setScene(new Scene(borderPane, 500, 500));
        stage.show();
    }

    private static void setEditable(boolean editable)
    {
        txt_firstName.setEditable(editable);
        txt_lastName.setEditable(editable);
        txt_grade.setEditable(editable);
        txt_group.setEditable(editable);
        txt_points.setEditable(editable);
        txt_bookCount.setEditable(editable);
    }
}
