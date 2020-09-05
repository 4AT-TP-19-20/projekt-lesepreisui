package sample;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.Optional;
import java.util.concurrent.Callable;

public class ContestantDetailView extends BorderPane implements MultiContent {
    private Contestant contestant;
    private Contestant copy;
    private BorderPane content;
    private Callable<Boolean> canLeave;

    ContestantDetailView(Contestant contestant, ContestantTab parent) {
        content = new BorderPane();

        //Top
        GridPane topItems = new GridPane();

        topItems.add(new Label("Vorname"),0,0);
        TextField txt_firstName = new TextField();
        txt_firstName.textProperty().bindBidirectional(contestant.firstNameProperty());
        topItems.add(txt_firstName, 1,0,3,1);

        topItems.add(new Label("Nachname"),0,1);
        TextField txt_lastName = new TextField();
        txt_lastName.textProperty().bindBidirectional(contestant.lastNameProperty());
        topItems.add(txt_lastName, 1,1,3,1);

        topItems.add(new Label("Klasse"),0,2);
        TextField txt_grade = new TextField();
        txt_grade.textProperty().bindBidirectional(contestant.gradeProperty());
        topItems.add(txt_grade,1,2);

        topItems.add(new Label("Gruppe"),2,2);
        SwitchBox sbx_groupMember = new SwitchBox(contestant.groupMemberProperty(), "small", true);
        topItems.add(sbx_groupMember,3,2);

        topItems.add(new Label("Gesamtpunkte"),0,3);
        TextField txt_points = new TextField();
        txt_points.textProperty().bindBidirectional(contestant.pointsProperty(), new StringToInt());
        txt_points.setEditable(false);
        topItems.add(txt_points, 1, 3);

        topItems.add(new Label("Gelesene Bücher"),2,3);
        TextField txt_bookCount = new TextField();
        txt_bookCount.textProperty().bindBidirectional(contestant.bookCountProperty(), new StringToInt());
        txt_bookCount.setEditable(false);
        topItems.add(txt_bookCount, 3, 3);

        topItems.setHgap(5);
        topItems.setVgap(5);

        content.setTop(topItems);

        //Center
        CustomTableView<Exam> tbv_exams = new CustomTableView<>();

        tbv_exams.addColumn("Titel", "", param -> param.getValue().getBook().titleProperty());
        tbv_exams.addColumn("Vorname Autor","", param -> param.getValue().getBook().authorFirstNameProperty());
        tbv_exams.addColumn("Nachname Autor", "", param -> param.getValue().getBook().authorLastNameProperty());
        tbv_exams.addColumn("Sprache", "", param -> param.getValue().getBook().languageProperty());
        tbv_exams.addColumn("Datum", "", new PropertyValueFactory<>("date"));
        tbv_exams.addColumn("BibliothekarIn", "", new PropertyValueFactory<>("librarian"));
        tbv_exams.addColumn("Punkte", "", new PropertyValueFactory<>("points"));
        tbv_exams.addColumn("Bestanden", new StackPane(), param -> new SwitchBox(param.getValue().passedProperty(), "small", false, true));
        tbv_exams.addColumn("Antworten", new HBox(), param -> new AnswerBoxes(param.getValue().answersProperty(), false, "small"));

        tbv_exams.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbv_exams.setItems(contestant.getExams());
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

        content.setCenter(tbv_exams);

        //Bottom
        VBox bottomItems = new VBox();
        Button btn_addExam = new Button("Neue Prüfung");
        btn_addExam.setId("custom-button");
        ContestantDetailView outerThis = this; //TODO find intended way to do this
        btn_addExam.setOnAction(e -> {
            Main.disableButtons();
            Main.enableBack(ee -> {
                showContent();
                enableSaveDiscardSystem();
            });
            BookTab bookTab = new BookTab() {
                protected void onSelection() {
                    outerThis.showContent();
                    enableSaveDiscardSystem();

                    Exam toAdd = new Exam(getSelectedBook());
                    contestant.addExam(toAdd);
                    ExamDetailWindow examDetailWindow = new ExamDetailWindow(toAdd);
                    examDetailWindow.show();
                }
            };
            this.setCenter(bookTab);
            bookTab.requestFocus();
        });
        Button btn_removeExam = new Button("Prüfung löschen");
        btn_removeExam.setId("red-button");
        btn_removeExam.setOnAction(e->{
            Exam selected = tbv_exams.getSelectionModel().getSelectedItem();
            if(selected != null) {
                contestant.removeExam(selected);
            }
        });
        bottomItems.getChildren().addAll(btn_addExam, btn_removeExam);
        bottomItems.setSpacing(5);
        content.setBottom(bottomItems);

        canLeave = () -> {
            if(!contestant.isEqualTo(copy)) {
                SaveAlert saveAlert = new SaveAlert();
                Optional<ButtonType> picked = saveAlert.showAndWait();

                if(picked.isPresent()) {
                    switch (picked.get().getButtonData()) {
                        case YES:
                            copy.setValues(contestant);
                            break;
                        case NO:
                            contestant.setValues(copy);
                            break;
                        case CANCEL_CLOSE:
                            return false;
                    }
                }
            }
            parent.showContent();
            Main.disableButtons();
            return true;
        };

        this.contestant = contestant;
        copy = contestant.getCopy();
        enableSaveDiscardSystem();

        BorderPane.setMargin(content.getCenter(), new Insets(10,0,10,0));
        content.setPadding(new Insets(10));
        this.showContent();
    }

    public void showContent() {
        this.setCenter(content);
    }

    private void enableSaveDiscardSystem() {
        Main.enableSaveDiscardSystem(save -> copy.setValues(contestant),
                discard -> contestant.setValues(copy),
                canLeave,
                true);
    }
}
