package de.hhu.propra14.team101.GUIElements;

import javafx.scene.control.ListCell;

/**
 * List cell with a background color
 * @code
 * <p>
 * final ListView<String[]> list = new ListView<>();
 * ObservableList<String[]> data = FXCollections.observableArrayList();
 * list.setItems(data);
 * list.setCellFactory(
 *     new Callback<ListView<String[]>, ListCell<String[]>>() {
 *         @Override
 *         public ListCell<String[]> call(ListView<String[]> list) {
 *            return new ColoredListCell();
 *         }
 *     }
 * );
 * </p>
 */
public class ColoredListCell extends ListCell<String[]> {
    @Override
    /**
     * Display the text and set a background color
     */
    public void updateItem(String[] item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item[0]);
            setStyle("-fx-background-color: #"+item[1]);
        }
    }
}