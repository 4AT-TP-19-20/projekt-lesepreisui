package sample.frontend.detailviews;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import sample.*;
import sample.backend.*;
import sample.backend.data.Contestant;
import sample.backend.data.Data;
import sample.backend.data.Exam;
import sample.backend.data.Group;
import sample.frontend.controls.AnswerBoxes;
import sample.frontend.controls.CustomTableView;
import sample.frontend.controls.SwitchBox;
import sample.frontend.detailwindows.ExamDetailWindow;
import sample.frontend.tabs.BookTab;
import sample.utils.StringToInt;

public class ContestantDetailView extends BorderPane {
    private final Contestant contestant;
    private CustomTableView<Exam> tbv_exams;

    public ContestantDetailView(Contestant contestant) {
        this.contestant = contestant;
        contestant.getExams().addListener((ListChangeListener<Exam>) c -> {
            tbv_exams.getItems().clear();
            tbv_exams.getItems().addAll(contestant.getExams());
        });

        //Top
        GridPane topItems = new GridPane();

        topItems.add(new Label("Vorname"),0,0);
        TextField txt_firstName = new TextField();
        txt_firstName.textProperty().bindBidirectional(contestant.firstNameProperty());
        txt_firstName.setPrefWidth(200);
        topItems.add(txt_firstName, 1,0);

        topItems.add(new Canvas(50, 1), 2, 0);

        topItems.add(new Label("Nachname"),0,1);
        TextField txt_lastName = new TextField();
        txt_lastName.textProperty().bindBidirectional(contestant.lastNameProperty());
        txt_lastName.setPrefWidth(200);
        topItems.add(txt_lastName, 1,1);

        topItems.add(new Canvas(50, 1), 2, 1);

        topItems.add(new Label("Klasse"),3,0);
        TextField txt_grade = new TextField();
        txt_grade.textProperty().bindBidirectional(contestant.gradeProperty());
        txt_grade.setPrefWidth(150);
        topItems.add(txt_grade,4,0, 2, 1);

        topItems.add(new Canvas(50, 1), 6, 0);

        topItems.add(new Label("Gruppe"),3,1);
        SwitchBox sbx_groupMember = new SwitchBox(contestant.groupMemberProperty(), "small", true);
        sbx_groupMember.setDisable(!contestant.getGrade().contains("1"));
        txt_grade.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.contains("1")) {
                sbx_groupMember.setDisable(false);
            } else {
                sbx_groupMember.setDisable(true);

                contestant.setGroupMember(false);
                for(Group group : Data.groups) {
                    if(group.getMembers().contains(contestant)) {
                        group.removeMember(contestant);
                        if(group.getMemberCount() == 0) {
                            Data.groups.remove(group);
                            break;
                        }
                    }
                }
            }
        });
        topItems.add(sbx_groupMember,4,1);

        HBox hbx_qualified = new HBox(5, new Label("Qualifiziert"), new SwitchBox(contestant.qualifiedProperty(), "small", false));
        hbx_qualified.setAlignment(Pos.CENTER_RIGHT);
        topItems.add(hbx_qualified, 5, 1);

        topItems.add(new Canvas(50, 1), 6, 1);

        topItems.add(new Label("Gesamtpunkte"),7,0);
        TextField txt_points = new TextField();
        txt_points.textProperty().bindBidirectional(contestant.pointsProperty(), StringToInt.getInstance());
        txt_points.setEditable(false);
        topItems.add(txt_points, 8, 0);

        topItems.add(new Label("Gelesene Bücher"),7,1);
        TextField txt_bookCount = new TextField();
        txt_bookCount.textProperty().bindBidirectional(contestant.bookCountProperty(), StringToInt.getInstance());
        txt_bookCount.setEditable(false);
        topItems.add(txt_bookCount, 8, 1);

        topItems.setHgap(5);
        topItems.setVgap(5);

        this.setTop(topItems);

        //Center
        VBox centerItems = new VBox();
        TextField txt_search = new TextField();
        txt_search.setPromptText("Suche nach Datum, Buch, Autor, ...");
        txt_search.textProperty().addListener((observable, oldValue, newValue) -> textChangeListener(newValue));

        tbv_exams = new CustomTableView<>();

        tbv_exams.<String>addColumn("Titel", param -> param.getValue().getBook().titleProperty());
        tbv_exams.<String>addColumn("Vorname Autor", param -> param.getValue().getBook().authorFirstNameProperty());
        tbv_exams.<String>addColumn("Nachname Autor", param -> param.getValue().getBook().authorLastNameProperty());
        tbv_exams.<String>addColumn("Sprache", param -> param.getValue().getBook().languageProperty());
        tbv_exams.<String>addColumn("Datum", param -> new SimpleStringProperty(param.getValue().getDateAsString()));
        tbv_exams.<String>addColumn("BibliothekarIn", new PropertyValueFactory<>("librarian"));
        tbv_exams.<String>addColumn("Punkte", new PropertyValueFactory<>("points"));
        tbv_exams.<StackPane>addColumn("Bestanden", param -> new SwitchBox(param.getValue().passedProperty(), "small", false, true));
        tbv_exams.<HBox>addColumn("Antworten", param -> new AnswerBoxes(param.getValue().answersProperty(), false, "small"));

        tbv_exams.getItems().addAll(contestant.getExams());
        tbv_exams.setOnMouseClicked((MouseEvent event) -> {
            if(event.getTarget() instanceof TableColumnHeader) {
                tbv_exams.getSelectionModel().clearSelection();
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                if(tbv_exams.getSelectionModel().getSelectedItem() != null) {
                    ExamDetailWindow examDetailWindow = new ExamDetailWindow(tbv_exams.getSelectionModel().getSelectedItem());
                    examDetailWindow.show();
                }
            }
        });

        centerItems.getChildren().addAll(txt_search, tbv_exams);
        centerItems.setSpacing(5);
        VBox.setVgrow(tbv_exams, Priority.ALWAYS);
        this.setCenter(centerItems);

        //Bottom
        VBox bottomItems = new VBox();
        Button btn_addExam = new Button("Neue Prüfung");
        btn_addExam.setId("custom-button");
        btn_addExam.setOnAction(e -> {
            BookTab bookTab = new BookTab() {
                protected void onItemSelected() {
                    for(Exam exam : contestant.getExams()) {
                        if(exam.getBook().equals(getSelectedBook())) {
                            Alert duplicationAlert = new Alert(Alert.AlertType.ERROR, "Es wurde bereits eine Prüfung für das gewählte Buch durchgeführt!", ButtonType.OK);
                            duplicationAlert.setHeaderText("");
                            duplicationAlert.setTitle("Doppelte Prüfung");
                            duplicationAlert.initOwner(this.getScene().getWindow());
                            duplicationAlert.show();
                            return;
                        }
                    }

                    Main.getCurrentContentStack().pop();

                    Exam toAdd = new Exam(getSelectedBook());
                    ExamDetailWindow examDetailWindow = new ExamDetailWindow(toAdd);
                    examDetailWindow.show();

                    toAdd.synchronize();
                    Data.exams.put(toAdd.getId(), toAdd);
                    contestant.addExam(toAdd);
                }
            };
            Main.getCurrentContentStack().push(bookTab);
        });
        Button btn_removeExam = new Button("Prüfung löschen");
        btn_removeExam.setId("red-button");
        btn_removeExam.setOnAction(e->{
            Exam selected = tbv_exams.getSelectionModel().getSelectedItem();
            if(selected != null) {
                selected.delete();
            }
        });
        bottomItems.getChildren().addAll(btn_addExam, btn_removeExam);
        bottomItems.setSpacing(5);
        this.setBottom(bottomItems);

        BorderPane.setMargin(this.getCenter(), new Insets(10,0,10,0));
        this.setPadding(new Insets(10));
    }

    private void textChangeListener(String newValue) {
        newValue = newValue.trim().toLowerCase();
        tbv_exams.getItems().clear();

        if(newValue.isEmpty()) {
            tbv_exams.getItems().addAll(contestant.getExams());
            return;
        }

        for(Exam exam : contestant.getExams()) {
            if(Searchables.contain(newValue, exam)) {
                tbv_exams.getItems().add(exam);
            }
        }
    }
}
