package sample;


import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
        txt_points = new TextField();
        txt_points.textProperty().bindBidirectional(contestant.pointsProperty(), new StringToInt());
        txt_points.setEditable(false);
        topItems.add(txt_points, 1, 3);

        topItems.add(new Label("Gelesene Bücher"),2,3);
        txt_bookCount = new TextField();
        txt_bookCount.textProperty().bindBidirectional(contestant.bookCountProperty(), new StringToInt());
        txt_bookCount.setEditable(false);
        topItems.add(txt_bookCount, 3, 3);

        topItems.setHgap(5);
        topItems.setVgap(5);
        borderPane.setTop(topItems);

        //Center
        TableView<Exam> tbv_exams = new TableView<>();
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
        TableColumn<Exam, String> column_librarian = new TableColumn<>("BibliothekarIn");
        column_librarian.setCellFactory(param -> new AlignedTableCell<>());
        column_librarian.setCellValueFactory(new PropertyValueFactory<>("librarian"));
        TableColumn<Exam, String> column_points = new TableColumn<>("Punkte");
        column_points.setCellFactory(param -> new AlignedTableCell<>());
        column_points.setCellValueFactory(new PropertyValueFactory<>("points"));
        TableColumn<Exam, StackPane> column_passed = new TableColumn<>("Bestanden");
        column_passed.setCellFactory(param -> new AlignedTableCell<>());
        column_passed.setCellValueFactory(param -> new SwitchBox(param.getValue().passedProperty(), "small", false, true));
        TableColumn<Exam, HBox> column_answers = new TableColumn<>("Antworten");
        column_answers.setCellFactory(param -> new AlignedTableCell<>());
        column_answers.setCellValueFactory(param -> new AnswerBoxes(param.getValue().answersProperty(), false, "small"));

        tbv_exams.getColumns().addAll(column_title, column_authorFirstName, column_authorLastName, column_language, column_date, column_librarian, column_points, column_passed, column_answers);
        tbv_exams.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbv_exams.setItems(contestant.getExams());
        tbv_exams.setOnMouseClicked((MouseEvent event) -> {
            if(event.getTarget() instanceof TableColumnHeader) {
                tbv_exams.getSelectionModel().clearSelection();
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                if(tbv_exams.getSelectionModel().getSelectedItem() != null) {
                    ExamDetailWindow.showNewWindow(tbv_exams.getSelectionModel().getSelectedItem());
                }
            }
        });

        borderPane.setCenter(tbv_exams);

        //Bottom
        VBox bottomItems = new VBox();
        Button btn_addExam = new Button("Neue Prüfung");
        btn_addExam.setId("custom-button");
        btn_addExam.setOnAction(e->{
            Stage bookSelectionStage = new Stage();
            bookSelectionStage.setTitle("Buch auswählen");
            BorderPane bookStageRoot = BookTab.generate(true, bookSelectionStage);
            Scene bookSelectionScene = new Scene(bookStageRoot,1280,1024);
            bookSelectionScene.getStylesheets().add("stylesheet.css");
            bookSelectionStage.setScene(bookSelectionScene);
            bookStageRoot.requestFocus();
            bookSelectionStage.showAndWait();
            Book selectedBook = BookTab.getSelectedBook();
            if(selectedBook != null) {
                Exam toAdd = new Exam(selectedBook);
                contestant.addExam(toAdd);
                ExamDetailWindow.showNewWindow(toAdd);
            }
        });
        Button btn_removeExam = new Button("Prüfung löschen");
        btn_removeExam.setId("custom-button");
        btn_removeExam.setOnAction(e->{
            Exam selected = tbv_exams.getSelectionModel().getSelectedItem();
            if(selected != null) {
                contestant.removeExam(selected);
            }
        });
        bottomItems.getChildren().addAll(btn_addExam, btn_removeExam);
        bottomItems.setSpacing(5);
        borderPane.setBottom(bottomItems);

        borderPane.setPadding(new Insets(10));
        BorderPane.setMargin(borderPane.getCenter(), new Insets(10,0,10,0));

        Scene scene = new Scene(borderPane, 1280, 1000);
        scene.getStylesheets().add("stylesheet.css");
        stage.setTitle("Detailansicht Teilnehmer");
        stage.setScene(scene);
        stage.show();
        borderPane.requestFocus();
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
