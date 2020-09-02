package sample;

public interface Saveable<T> {
    T getCopy();

    boolean isEqualTo(T t);

    void setValues(T t);
}
