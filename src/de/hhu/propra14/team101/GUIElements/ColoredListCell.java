package de.hhu.propra14.team101.GUIElements;

import javafx.scene.control.ListCell;

/**
 * List cell with a background color
 *
 * <pre><code>
 * final ListView&lt;String[]&gt; list = new ListView&lt;&gt;();
 * ObservableList&lt;String[]&gt; data = FXCollections.observableArrayList();
 * list.setItems(data);
 * list.setCellFactory(
 *     new Callback&lt;ListView&lt;String[]&gt;, ListCell&lt;String[]&gt;&gt;() {
 *         \@Override
 *         public ListCell&lt;String[]&gt; call(ListView&lt;String[]&gt; list) {
 *            return new ColoredListCell();
 *         }
 *     }
 * );
 * </code></pre>
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