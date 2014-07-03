package de.hhu.propra14.team101.GUIElements;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Shows a popup window
 */

public class Popup extends Application {
    /** Message to show */
    private String message;
    /** Title of the opening popup */
    private String title;

    /**
     * Opens a popup
     * @param message Message to display
     * @param title Title of the opening popup
     */
    public static void popup (String message, String title) {
        Stage stage = new Stage();
        Popup popup = new Popup(message, title);
        popup.start(stage);
    }

    /**
     * Creates a popup
     * @param message Message to display
     * @param title Title of the displayed popup
     */
    public Popup (String message, String title) {
        this.message = message;
        this.title = title;
    }

    /**
     * Shows the popup
     * @param primaryStage Stage to show window on
     */
    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle(title);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text messageText = new Text(message);
        Button closeButton = new Button("OK");

        // Add the objects
        grid.add(messageText, 2, 0);
        grid.add(closeButton, 2, 1);

        primaryStage.setScene(new Scene(grid, 200, 100));
        primaryStage.show();
        grid.setStyle("-fx-background-color: #00BFFF");

        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage.close();
            }
        });
    }
}