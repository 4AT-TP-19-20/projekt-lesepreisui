package sample;


import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class ContestantTab {
    private static TableView<Contestant> tbv_contestants = new TableView<>();

    public static BorderPane generate() {
        BorderPane borderPane = new BorderPane();

        //Top
        TextField txt_search = new TextField();
        txt_search.setPromptText("Suche nach Name, Klasse, ...");
        txt_search.setMaxWidth(10000);
        txt_search.textProperty().addListener((observable, oldValue, newValue) -> textChangeListener(newValue));
        borderPane.setTop(txt_search);

        //Center
        TableColumn<Contestant, String> column_firstName = new TableColumn<>("Vorname");
        column_firstName.setCellFactory(param -> new AlignedTableCell<>());
        column_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<Contestant, String> column_lastName = new TableColumn<>("Nachname");
        column_lastName.setCellFactory(param -> new AlignedTableCell<>());
        column_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        TableColumn<Contestant, String> column_grade = new TableColumn<>("Klasse");
        column_grade.setCellFactory(param -> new AlignedTableCell<>());
        column_grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        TableColumn<Contestant, Integer> column_bookCount = new TableColumn<>("Gelesene Bücher");
        column_bookCount.setCellFactory(param -> new AlignedTableCell<>());
        column_bookCount.setCellValueFactory(new PropertyValueFactory<>("bookCount"));
        TableColumn<Contestant, Integer> column_points = new TableColumn<>("Punkte");
        column_points.setCellFactory(param -> new AlignedTableCell<>());
        column_points.setCellValueFactory(new PropertyValueFactory<>("points"));
        tbv_contestants.getColumns().addAll(column_firstName, column_lastName, column_grade, column_bookCount, column_points);
        tbv_contestants.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbv_contestants.getItems().addAll(Data.contestants);
        tbv_contestants.setOnMouseClicked((MouseEvent event) -> {
            if(event.getTarget() instanceof TableColumnHeader) {
                tbv_contestants.getSelectionModel().clearSelection();
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                if(tbv_contestants.getSelectionModel().getSelectedItem() != null) {
                    ContestantDetailWindow.showNewWindow(tbv_contestants.getSelectionModel().getSelectedItem());
                }
            }
        });
        borderPane.setCenter(tbv_contestants);

        //Bottom
        Button btn_addContestant = new Button("Neuer Teilnehmer");
        btn_addContestant.setId("custom-button");
        btn_addContestant.setMaxWidth(10000);
        borderPane.setBottom(btn_addContestant);

        BorderPane.setMargin(borderPane.getCenter(), new Insets(10, 0, 10, 0));
        borderPane.setPadding(new Insets(10));
        return borderPane;
    }

    private static void textChangeListener(String newValue) {
        newValue = newValue.trim().toLowerCase();
        tbv_contestants.getItems().clear();

        if(newValue.trim().isEmpty()) {
            tbv_contestants.getItems().addAll(Data.contestants);
            return;
        }

        for(Contestant contestant : Data.contestants) {
            if(contestant.getFirstName().toLowerCase().contains(newValue)
                    || contestant.getLastName().toLowerCase().contains(newValue)
                    || contestant.getGrade().toLowerCase().contains(newValue)) {
                tbv_contestants.getItems().add(contestant);
            }
        }
    }
}
