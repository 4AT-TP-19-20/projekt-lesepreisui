package sample.backend.data;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.backend.IntegerPropertyArray;
import sample.backend.Searchable;
import sample.backend.data.database.DatabaseEntry;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Exam extends DatabaseEntry implements Comparable<Exam>, Searchable {
    private Book book;
    private final IntegerPropertyArray answers;
    private final IntegerProperty passed;
    private final IntegerProperty points;
    private final StringProperty librarian;
    private LocalDate date;
    private Contestant contestant;
    private static final Contestant tempContestant;

    static {
        tempContestant = new Contestant();
    }

    public Exam(Book book, int[] initialAnswers, String librarian, LocalDate date) {
        super("exams");

        this.book = book;
        this.answers = new IntegerPropertyArray(Data.settings.getMaxAnswersCount(), initialAnswers);
        this.librarian = new SimpleStringProperty(librarian);
        this.date = date;
        this.contestant = Exam.tempContestant;

        passed = new SimpleIntegerProperty();
        answers.addListener((observable, oldValue, newValue) -> onAnswerChangeListener());
        onAnswerChangeListener();
        points = new SimpleIntegerProperty(passed.get() == 1 ? book.getPoints() : 0);
        points.bind(Bindings.when(passed.isEqualTo(1)).then(book.pointsProperty()).otherwise(0));

        registerUpdateListeners();
    }

    public Exam(Book book) {
        this(book, new int[]{-1}, Data.currentUser, LocalDate.now());
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int[] getAnswers() {
        return answers.get();
    }

    public IntegerPropertyArray answersProperty() {
        return answers;
    }

    public void setAnswers(int[] answers) {
        this.answers.set(answers);
    }

    public int getPassed() {
        return passed.get();
    }

    public IntegerProperty passedProperty() {
        return passed;
    }

    public int getPoints() {
        return points.get();
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public String getLibrarian() {
        return librarian.get();
    }

    public StringProperty librarianProperty() {
        return librarian;
    }

    public void setLibrarian(String librarian) {
        this.librarian.set(librarian);
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateAsString() {
        return date.format(Data.dateFormatter);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setContestant(Contestant contestant) {
        this.contestant = contestant;
    }

    public Contestant getContestant() {
        return contestant;
    }

    private void onAnswerChangeListener() {
        int correct = 0;
        boolean allSet = true;

        for(int value : answers.get()) {
            if (value == 1) {
                correct++;
            }
            else if (value == 2) {
                allSet = false;
            }
        }

        if(correct >= Data.settings.getMinCorrectAnswers()) {
            passed.set(1);
        }
        else if(allSet) {
            passed.set(0);
        }
        else {
            passed.set(2);
        }

        onUpdate();
    }

    @Override
    public int compareTo(Exam other) {
        //For every book only one exam can be taken by every contestant,
        //which means every exam of a contestant has an unique book, so comparing the books is good enough
        return this.getBook().compareTo(other.getBook());
    }

    @Override
    public boolean search(String s) {
        if(getBook().search(s)
        || getLibrarian().toLowerCase().contains(s)) {
            return true;
        }

        try {
            if(s.contains("-")) { //Date range
                String[] dates = s.split("-");

                if(dates.length != 2) {
                    return false;
                }

                LocalDate date1 = LocalDate.parse(dates[0].trim(), Data.dateFormatter);
                LocalDate date2 = LocalDate.parse(dates[1].trim(), Data.dateFormatter);

                return getDate().isEqual(date1)
                        || getDate().isEqual(date2)
                        || (getDate().isAfter(date1) && getDate().isBefore(date2));
            }
            else { //Specific date
                return getDate().isEqual(LocalDate.parse(s.trim(), Data.dateFormatter));
            }
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        object.put("book", getBook().getId());
        object.put("answers", new JSONArray(getAnswers()));
        object.put("librarian", getLibrarian());
        object.put("date", getDateAsString());
        return object;
    }

    @Override
    public void fromJson(JSONObject json) {
        setBook(Data.books.get(json.getString("book")));

        JSONArray answers = json.getJSONArray("answers");
        for (int i = 0; i < Data.settings.getMaxAnswersCount(); i++) {
            answersProperty().getByIndex(i).set((int) answers.get(i));
        }

        setLibrarian(json.getString("librarian"));
        setDate(LocalDate.parse(json.getString("date"), Data.dateFormatter));
    }

    @Override
    public void onDelete() {
        getContestant().removeExam(this);
        Data.exams.remove(getId());
    }
}
