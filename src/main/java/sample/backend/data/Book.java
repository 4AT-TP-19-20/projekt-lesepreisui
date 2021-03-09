package sample.backend.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONObject;
import sample.backend.Searchable;
import sample.backend.data.database.DatabaseEntry;

public class Book extends DatabaseEntry implements Comparable<Book>, Searchable {
    private final StringProperty title;
    private final StringProperty authorFirstName;
    private final StringProperty authorLastName;
    private final StringProperty language;
    private final IntegerProperty points;

    public Book() {
        this("Titel", "Vorname", "Nachname", "Sprache", 0);
    }

    public Book(String title, String authorFirstName, String authorLastName, String language, int points) {
        super("books");

        this.title = new SimpleStringProperty(title);
        this.authorFirstName = new SimpleStringProperty(authorFirstName);
        this.authorLastName = new SimpleStringProperty(authorLastName);
        this.language = new SimpleStringProperty(language);
        this.points = new SimpleIntegerProperty(points);

        registerUpdateListeners();
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthorFirstName() {
        return authorFirstName.get();
    }

    public StringProperty authorFirstNameProperty() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName.set(authorFirstName);
    }

    public String getAuthorLastName() {
        return authorLastName.get();
    }

    public StringProperty authorLastNameProperty() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName.set(authorLastName);
    }

    public String getLanguage() {
        return language.get();
    }

    public StringProperty languageProperty() {
        return language;
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public int getPoints() {
        return points.get();
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public void setPoints(int points) {
        this.points.set(points);
    }

    @Override
    public int compareTo(Book other) {
        if(!this.getLanguage().equals(other.getLanguage())) {
            return this.getLanguage().compareTo(other.getLanguage());
        }
        if(!this.getAuthorLastName().equals(other.getAuthorLastName())) {
            return this.getAuthorLastName().compareTo(other.getAuthorLastName());
        }
        if(!this.getAuthorFirstName().equals(other.getAuthorFirstName())) {
            return this.getAuthorFirstName().compareTo(other.getAuthorFirstName());
        }
        return this.getTitle().compareTo(other.getTitle());
    }

    @Override
    public boolean search(String s) {
        return getTitle().toLowerCase().contains(s)
                || getAuthorFirstName().toLowerCase().contains(s)
                || getAuthorLastName().toLowerCase().contains(s)
                || getLanguage().toLowerCase().contains(s);
    }

    @Override
    public void onDelete() {
        for (Exam exam : Data.exams.values()) {
            if(exam.getBook().equals(this)) {
                exam.delete();
            }
        }

        Data.books.remove(getId());
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        object.put("title", getTitle());
        object.put("authorfirstname", getAuthorFirstName());
        object.put("authorlastname", getAuthorLastName());
        object.put("language", getLanguage());
        object.put("points", getPoints());
        return object;
    }

    @Override
    public void fromJson(JSONObject object) {
        this.setTitle(object.getString("title"));
        this.setAuthorFirstName(object.getString("authorfirstname"));
        this.setAuthorLastName(object.getString("authorlastname"));
        this.setLanguage(object.getString("language"));
        this.setPoints(object.getInt("points"));
    }
}
