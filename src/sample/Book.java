package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Book {
    private StringProperty title;
    private StringProperty authorFirstName;
    private StringProperty authorLastName;
    private StringProperty language;
    private IntegerProperty points;
    
    public Book(String title, String authorFirstName, String authorLastName, String language, int points) {
        this.title = new SimpleStringProperty(title);
        this.authorFirstName = new SimpleStringProperty(authorFirstName);
        this.authorLastName = new SimpleStringProperty(authorLastName);
        this.language = new SimpleStringProperty(language);
        this.points = new SimpleIntegerProperty(points);
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

    public Element getXMLNode(Document doc){
        Element book = doc.createElement("Book");
        Element titleElement = doc.createElement("Title");
        Element authorFirstNameElement = doc.createElement("AuthorFirstName");
        Element authorLastNameElement = doc.createElement("AuthorLastName");
        Element languageElement = doc.createElement("Language");
        Element pointsElement = doc.createElement("Points");

        titleElement.appendChild(doc.createTextNode(this.getTitle()));
        authorFirstNameElement.appendChild(doc.createTextNode(this.getAuthorFirstName()));
        authorLastNameElement.appendChild(doc.createTextNode(this.getAuthorLastName()));
        languageElement.appendChild(doc.createTextNode(this.getLanguage()));
        pointsElement.appendChild(doc.createTextNode(""+this.getPoints()));

        book.appendChild(titleElement);
        book.appendChild(authorFirstNameElement);
        book.appendChild(authorLastNameElement);
        book.appendChild(languageElement);
        book.appendChild(pointsElement);

        return book;
    }
}
