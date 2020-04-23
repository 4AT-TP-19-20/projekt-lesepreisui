package sample;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

public class AlignedTableCell<X, Y> extends TableCell<X, Y>{
    protected void updateItem(Y item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            if(item instanceof Node) {
                setGraphic((Node)item);
            }
            else {
                this.setText(item.toString());
            }
            this.setAlignment(Pos.CENTER);
        }
    }
}
