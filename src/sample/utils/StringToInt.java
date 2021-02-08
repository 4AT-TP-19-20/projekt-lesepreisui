package sample.utils;

import javafx.util.StringConverter;

public class StringToInt extends StringConverter<Number> {
    private static StringToInt instance;

    private StringToInt() {}

    public static StringToInt getInstance() {
        if(instance == null) {
            instance = new StringToInt();
        }

        return instance;
    }

    @Override
    public String toString(Number object) {
        return String.valueOf(object);
    }

    @Override
    public Number fromString(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
