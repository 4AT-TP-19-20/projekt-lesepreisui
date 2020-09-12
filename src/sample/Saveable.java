package sample;

public interface Saveable {
    Saveable getCopy();

    boolean equals(Object other);

    void setValues(Saveable other);
}
