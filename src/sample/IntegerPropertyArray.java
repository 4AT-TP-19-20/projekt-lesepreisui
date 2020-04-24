package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;

public class IntegerPropertyArray {
    private int size;
    private IntegerProperty[] properties;

    IntegerPropertyArray(int size) {
        this(size, new int[size]);
    }

    IntegerPropertyArray(int size, int[] initialValues) {
        this.size = size;
        properties = new IntegerProperty[size];

        for(int i = 0; i < size; i++) {
            properties[i] = new SimpleIntegerProperty(initialValues[i]);
        }
    }

    public IntegerProperty getByIndex(int index) {
        return properties[index];
    }

    public int[] get() {
        int[] values = new int[size];

        for(int i = 0; i < size; i++) {
            values[i] = properties[i].get();
        }

        return values;
    }

    public void set(int[] array) {
        for (int i = 0; i < size; i++) {
            properties[i].set(array[i]);
        }
    }

    public void addListener(ChangeListener<? super Number> changeListener) {
        for(IntegerProperty property : properties) {
            property.addListener(changeListener);
        }
    }
}
