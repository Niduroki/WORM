package de.hhu.propra14.team101.GUIElements;

import javafx.scene.control.ListCell;

public class ColoredListCell extends ListCell<String[]> {
    @Override
    public void updateItem(String[] item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item[0]);
            setStyle("-fx-background-color: #"+item[1]);
        }
    }
}