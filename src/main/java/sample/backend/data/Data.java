package sample.backend.data;

import javafx.collections.*;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.backend.*;
import sample.backend.data.database.DatabaseEntry;
import sample.backend.data.database.DatabaseInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Data {
    //Backed by database
    public static ObservableMap<String, Contestant> contestants;
    public static ObservableMap<String, Book> books;
    public static ObservableMap<String, Exam> exams;

    public static ObservableList<Group> groups;
    public static String currentUser;
    public static Settings settings;
    public static DateTimeFormatter dateFormatter;
    private static final String path = ".\\storage\\";

    public static void init() {
        contestants = FXCollections.observableHashMap();
        books = FXCollections.observableHashMap();
        exams = FXCollections.observableHashMap();
        groups = FXCollections.observableArrayList();
        settings = new Settings();
        dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                synchronizeLocals(DatabaseInterface.read("books"), books, json -> {
                    Book b = new Book();
                    b.fromJson(json);
                    b.synchronize(json.getString("_id"), json.getString("_rev"));
                    return b;
                });

                synchronizeLocals(DatabaseInterface.read("exams"), exams, json -> {
                    Exam e = new Exam(books.get(json.getString("book")));
                    e.fromJson(json);
                    e.synchronize(json.getString("_id"), json.getString("_rev"));
                    return e;
                });

                synchronizeLocals(DatabaseInterface.read("contestants"), contestants, json -> {
                    Contestant c = new Contestant();
                    c.fromJson(json);
                    c.synchronize(json.getString("_id"), json.getString("_rev"));
                    return c;
                });
            }
        }, 0, 5000);
    }

    public static Group getGroupByGrade(String grade) {
        for(Group group : groups) {
            if(group.getGrade().equals(grade)) {
                return group;
            }
        }

        Group newGroup = new Group(grade);
        groups.add(newGroup);
        return newGroup;
    }

    public static <T extends DatabaseEntry> void synchronizeLocals(JSONArray remotes, Map<String, T> locals, Callback<JSONObject, T> factory) {
        ArrayList<String> ids = new ArrayList<>();

        //Create elements which only exist remotely and update entries whose properties have changed
        remotes.forEach(item -> {
            JSONObject jsonItem = (JSONObject) item;
            String id = jsonItem.getString("_id");
            String rev = jsonItem.getString("_rev");

            ids.add(id);

            T entry = locals.get(id);
            if(entry == null) {
                locals.put(id, factory.call(jsonItem));
            }
            else {
                if(entry.getStatus() == DatabaseEntry.Status.SYNCHRONIZED
                && entry.getRev().compareTo(rev) < 0) { //If the remote rev is newer (greater) than the local rev
                    entry.fromJson((JSONObject) item);
                }
            }
        });

        /*
          Delete elements which only exist locally. Exceptions:
          - It is being created (Status is CREATING).
          - It is already being deleted (Status is DELETING).
        */
        Map<String, T> missing  = new HashMap<>(locals);

        ids.forEach(missing::remove);

        missing.forEach((id, entry) -> {
            if(entry.getStatus() == DatabaseEntry.Status.CREATING
            || entry.getStatus() == DatabaseEntry.Status.DELETING) {
                return;
            }

            entry.delete();
        });
    }
}
