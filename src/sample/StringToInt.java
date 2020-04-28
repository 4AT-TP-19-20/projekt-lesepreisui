package sample;

import javafx.util.StringConverter;

public class StringToInt extends StringConverter<Number> {
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
