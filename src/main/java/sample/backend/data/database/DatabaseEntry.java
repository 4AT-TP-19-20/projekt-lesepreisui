package sample.backend.data.database;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.lang.reflect.Field;

public abstract class DatabaseEntry implements JsonSerializable {
    private String id;
    private String rev;
    private Status status;
    private boolean updateAgain;
    private final Object mutex;

    private final String type;

    public DatabaseEntry(String type) {
        this.type = type;
        mutex = new Object();
        status = Status.UNSYNCHRONIZED;
    }

    //To monitor observable changes which have to be reported to the database
    public void registerUpdateListeners() {
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            try {
                declaredField.setAccessible(true);
                Object o = declaredField.get(this);
                if(o instanceof ObservableValue) {
                    ((ObservableValue<?>) o).addListener((observable, oldValue, newValue) -> onUpdate());
                }
                else if(o instanceof ObservableList) {
                    ((ObservableList<?>) o).addListener((ListChangeListener<Object>) c -> onUpdate());
                }
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void synchronize() {
        status = Status.CREATING;
        DatabaseInterface.add(this);

        while (updateAgain) { //If there are already changes
            updateAgain = false;
            status = Status.UPDATING;
            DatabaseInterface.update(this);
        }

        status = Status.SYNCHRONIZED;
    }

    public void synchronize(String id, String rev) {
        this.id = id;
        this.rev = rev;
        status = Status.SYNCHRONIZED;
    }

    public void onUpdate() {
        if(status == Status.DELETING || status == Status.DELETED || status == Status.UNSYNCHRONIZED) {
            return;
        }

        if(status == Status.UPDATING) {
            updateAgain = true;
            return;
        }

        new Thread(() -> {
            synchronized (mutex) {
                do {
                    updateAgain = false;
                    status = Status.UPDATING;
                    DatabaseInterface.update(this);
                } while (updateAgain);

                status = Status.SYNCHRONIZED;
            }
        }).start();
    }

    public void delete() {
        onDelete();

        if(status == Status.UNSYNCHRONIZED) {
            return;
        }

        status = Status.DELETING;

        new Thread(() -> {
            synchronized (mutex) {
                DatabaseInterface.delete(this);
                status = Status.DELETED;
            }
        }).start();
    }

    public abstract void onDelete();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        DELETING,
        DELETED,

        CREATING,

        UPDATING,

        SYNCHRONIZED,
        UNSYNCHRONIZED
    }
}