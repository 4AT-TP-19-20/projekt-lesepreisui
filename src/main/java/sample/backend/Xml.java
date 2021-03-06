package sample.backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sample.backend.data.Book;
import sample.backend.data.Contestant;
import sample.backend.data.Data;
import sample.backend.data.Exam;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Xml {
    public static void set(String path, String type){
        try {
            DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            File xmlFile = new File(path);

            Element mainRootElement = doc.createElement(type);
            doc.appendChild(mainRootElement);

            switch (type) {
                case "Books":
                    for (Book book : Data.books) {
                        mainRootElement.appendChild(book.getXMLNode(doc));
                    }
                    break;
                case "Contestants":
                    for (Contestant contestant : Data.contestants) {
                        mainRootElement.appendChild(contestant.getXMLNode(doc));
                    }
                    break;
                case "Settings":
                    Data.settings.getXMLNode(doc, mainRootElement);
                    break;
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult target = new StreamResult(xmlFile);
            transformer.transform(source, target);
        }catch (Exception e){
            System.out.println("[System] Error while writing to " + type.toLowerCase() + "-file!");
        }
    }

    public static void getBooks(String path){
        try{
            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            NodeList bookNodeList = doc.getElementsByTagName("Book");

            for (int i = 0; i < bookNodeList.getLength(); i++) {
                Node currentBook = bookNodeList.item(i);
                if (currentBook.getNodeType() == Node.ELEMENT_NODE) {
                    Element currentBookElement = (Element) currentBook;
                    String title = currentBookElement.getElementsByTagName("Title").item(0).getTextContent();
                    String authorFirstName = currentBookElement.getElementsByTagName("AuthorFirstName").item(0).getTextContent();
                    String authorLastName = currentBookElement.getElementsByTagName("AuthorLastName").item(0).getTextContent();
                    String language = currentBookElement.getElementsByTagName("Language").item(0).getTextContent();
                    int points = Integer.parseInt(currentBookElement.getElementsByTagName("Points").item(0).getTextContent());

                    Data.books.add(new Book(title, authorFirstName, authorLastName, language, points));
                }
            }
        }catch (Exception e){
            System.out.println("[System] Error while reading the books-file!");
        }
    }

    public static void getContestants(String path){
        try{
            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            NodeList contestantsNodeList = doc.getElementsByTagName("Contestant");

            for (int i = 0; i < contestantsNodeList.getLength(); i++) {
                Node currentContestant = contestantsNodeList.item(i);
                if (currentContestant.getNodeType() == Node.ELEMENT_NODE) {
                    Element currentContestantElement = (Element) currentContestant;
                    String firstName = currentContestantElement.getElementsByTagName("FirstName").item(0).getTextContent();
                    String lastName = currentContestantElement.getElementsByTagName("LastName").item(0).getTextContent();
                    String grade = currentContestantElement.getElementsByTagName("Grade").item(0).getTextContent();
                    String isGroupMember = currentContestantElement.getElementsByTagName("IsGroupMember").item(0).getTextContent();
                    Contestant contestant = new Contestant(firstName, lastName, grade);
                    contestant.setGroupMember(Boolean.parseBoolean(isGroupMember));

                    NodeList examsNodeList = currentContestantElement.getElementsByTagName("Exam");
                    for (int j = 0; j < examsNodeList.getLength(); j++) {
                        Node currentExam = examsNodeList.item(j);
                        if (currentExam.getNodeType() == Node.ELEMENT_NODE) {
                            Book book = Data.books.get(0);
                            Element currentExamElement = (Element) currentExam;
                            String bookTitle = currentExamElement.getElementsByTagName("BookTitle").item(0).getTextContent();
                            for (Book b:Data.books) {
                                if (b.getTitle().equals(bookTitle)){
                                    book = b;
                                }
                            }
                            String answers = currentExamElement.getElementsByTagName("Answers").item(0).getTextContent();
                            char[] answerCharArray = answers.toCharArray();
                            int[] answerIntArray = new int[answerCharArray.length];
                            for (int y = 0; y < answerCharArray.length; y++){
                                answerIntArray[y] = answerCharArray[y]-'0';
                            }
                            String librarian = currentExamElement.getElementsByTagName("Librarian").item(0).getTextContent();
                            String date = currentExamElement.getElementsByTagName("Date").item(0).getTextContent();

                            contestant.addExam(new Exam(book, answerIntArray, librarian, LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
                        }
                    }
                    Data.contestants.add(contestant);
                }
            }
        }catch (Exception e){
            System.out.println("[System] Error while reading the contestants-file!");
        }
    }

    public static void getSettings(String path){
        try{
            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            NodeList settingsNodeList = doc.getElementsByTagName("Settings");

            Element currentSettingElement = (Element) settingsNodeList.item(0);
            Data.settings.setPrizeCount(Integer.parseInt(currentSettingElement.getElementsByTagName("PrizeCount").item(0).getTextContent()));
            Data.settings.setMaxPicks(Integer.parseInt(currentSettingElement.getElementsByTagName("MaxPicks").item(0).getTextContent()));
            Data.settings.setMinCorrectAnswers(Integer.parseInt(currentSettingElement.getElementsByTagName("MinCorrectAnswers").item(0).getTextContent()));
            Data.settings.setMinBookCount(Integer.parseInt(currentSettingElement.getElementsByTagName("MinBookCount").item(0).getTextContent()));
            Data.settings.setMaxBookCount(Integer.parseInt(currentSettingElement.getElementsByTagName("MaxBookCount").item(0).getTextContent()));
            Data.settings.setGroupContestStartDate(LocalDate.parse(currentSettingElement.getElementsByTagName("GroupContestStartDate").item(0).getTextContent(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            Data.settings.setMinMembers(Integer.parseInt(currentSettingElement.getElementsByTagName("MinMembers").item(0).getTextContent()));
            Data.settings.setUseAndInSearch(Boolean.parseBoolean(currentSettingElement.getElementsByTagName("UseAndInSearch").item(0).getTextContent()));

            NodeList languageNodeList = doc.getElementsByTagName("Language");
            ObservableList<String> languages = FXCollections.observableArrayList();
            for (int i = 0; i < languageNodeList.getLength(); i++) {
                languages.add(languageNodeList.item(i).getTextContent());
            }
            Data.settings.setLanguages(languages);

            NodeList userNodeList = doc.getElementsByTagName("User");
            ObservableList<String> users = FXCollections.observableArrayList();
            for (int i = 0; i < userNodeList.getLength(); i++) {
                users.add(userNodeList.item(i).getTextContent());
            }
            Data.settings.setUsers(users);
        }catch (Exception e){
            System.out.println("[System] Error while reading the settings-file!");
        }
    }
}
