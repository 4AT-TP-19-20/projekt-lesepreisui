package sample;

import javafx.util.StringConverter;

public class StringToInt extends StringConverter<Number> {
    @Override
    public String toString(Number object) {
        return String.valueOf(object);
    }

    @Override
    public Number fromString(String string) {
        if(string.trim().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(string);
    }
}
