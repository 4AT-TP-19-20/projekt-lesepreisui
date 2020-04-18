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
        //TODO Change TextFields to use TextProperty?
        stage = new Stage();
        BorderPane borderPane = new BorderPane();

        //Top
        GridPane topItems = new GridPane();

        topItems.add(new Label("Vorname"),0,0);
        txt_firstName = new TextField(contestant.getFirstName());
        topItems.add(txt_firstName, 1,0,3,1);

        topItems.add(new Label("Nachname"),0,1);
        txt_lastName = new TextField(contestant.getLastName());
        topItems.add(txt_lastName, 1,1,3,1);

        topItems.add(new Label("Klasse"),0,2);
        txt_grade = new TextField(contestant.getGrade());
        topItems.add(txt_grade,1,2);

        topItems.add(new Label("Gruppe"),2,2);
        txt_group = new TextField("TODO");
        topItems.add(txt_group,3,2);

        topItems.add(new Label("Gesamtpunkte"),0,3);
        txt_points = new TextField(String.valueOf(contestant.getPoints()));
        topItems.add(txt_points, 1, 3);

        topItems.add(new Label("Gelesene Bücher"),2,3);
        txt_bookCount = new TextField(String.valueOf(contestant.getBookCount()));
        topItems.add(txt_bookCount, 3, 3);

        setEditable(false);

        topItems.setHgap(5);
        topItems.setVgap(5);
        borderPane.setTop(topItems);

        //Center
        TableView<Exam> tbv_exams = new TableView<>();
        TableColumn<Exam, String> column_title = new TableColumn<>("Atworten");
        column_title.setCellValueFactory(new PropertyValueFactory<>("answers"));
        TableColumn<Exam, String> column_authorFirstName = new TableColumn<>("Bibliothekar");
        column_authorFirstName.setCellValueFactory(new PropertyValueFactory<>("librarian"));
        tbv_exams.getColumns().addAll(column_title, column_authorFirstName);
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
