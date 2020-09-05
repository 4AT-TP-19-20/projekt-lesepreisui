package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class AnswerBoxes extends HBox implements ObservableValue<HBox> {
    private SwitchBox[] switchBoxes;
    private IntegerPropertyArray values;

    AnswerBoxes(IntegerPropertyArray valueProperties, boolean enabled, String size) {
        switchBoxes = new SwitchBox[Data.settings.getMaxAnswersCount()];
        values = valueProperties;

        for(int i = 0; i < Data.settings.getMaxAnswersCount(); i++) {
            switchBoxes[i] = new SwitchBox(values.getByIndex(i), size, enabled, true);
        }

        this.setSpacing(size.equals("small") ? 5 : 10);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(switchBoxes);
    }

    public SwitchBox[] getSwitchBoxes() {
        return switchBoxes;
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
