package sample;

import javafx.util.StringConverter;

public class StringToInt extends StringConverter<Number> {
    @Override
    public String toString(Number object) {
        return String.valueOf(object);
    }

    @Override
    public Number fromString(String string) {
        return Integer.parseInt(string);
    }
}
