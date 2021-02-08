package sample.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;

public class BooleanIntegerBinding {
    private boolean first = true;

    public BooleanIntegerBinding(IntegerProperty integerProperty, BooleanProperty booleanProperty) {
        integerProperty.addListener(e -> {
            if(first) {
                first = false;
                booleanProperty.set(integerProperty.get() == 1);
                first = true;
            }
        });

        booleanProperty.addListener(e -> {
            if(first) {
                first = false;
                integerProperty.set(booleanProperty.get() ? 1 : 0);
                first = true;
            }
        });
    }
}
