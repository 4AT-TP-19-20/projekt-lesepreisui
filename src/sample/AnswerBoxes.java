package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class AnswerBoxes extends HBox implements ObservableValue<HBox> {
    private SwitchBox[] switchBoxes;

    AnswerBoxes(String initialValues, boolean enabled, String size) {
        switchBoxes = new SwitchBox[6];

        for(int i = 0; i < 6; i++) {
            int initial = 2;
            if(initialValues.length() > i) {
                if(initialValues.charAt(i) == 'F') {
                    initial = 0;
                }
                else if(initialValues.charAt(i) == 'R') {
                    initial = 1;
                }
            }
            switchBoxes[i] = new SwitchBox(initial, size, enabled, true);
        }

        this.setSpacing(size.equals("small") ? 5 : 10);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(switchBoxes);
    }

    @Override
    public void addListener(ChangeListener<? super HBox> listener) {

    }

    @Override
    public void removeListener(ChangeListener<? super HBox> listener) {

    }

    @Override
    public HBox getValue() {
        return this;
    }

    @Override
    public void addListener(InvalidationListener listener) {

    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }
}
