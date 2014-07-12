package de.hhu.propra14.team101.GUIElements;

import de.hhu.propra14.team101.Main;
import de.hhu.propra14.team101.Networking.Exceptions.TimeoutException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.Map;

public class IngameChat extends Application {

    private Timeline ingameTimeline;
    private TextArea ingameChatArea;
    private Main main;

    /**
     * Creates ingame Chat.
     */
    public IngameChat(Main main) {
        this.main = main;
    }

    /**
     * Shows the chat
     *
     * @param primaryStage Stage to show window on
     */
    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Chat");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        ingameChatArea = new TextArea();
        ingameChatArea.setEditable(false);
        ingameChatArea.setWrapText(false);

        final ListView<String[]> userList = new ListView<>();
        try {
            main.client.loadRoomUsers();

            ObservableList<String[]> data = FXCollections.observableArrayList();
            for (Map.Entry<String, String> user : main.client.roomUsers.entrySet()) {
                String color;
                if (!user.getValue().equals("spectator")) {
                    if (user.getValue().equals("red")) {
                        color = "FF0000";
                    } else if (user.getValue().equals("blue")) {
                        color = "0044FF";
                    } else if (user.getValue().equals("green")) {
                        color = "00FF00";
                    } else if (user.getValue().equals("yellow")) {
                        color = "FFFF00";
                    } else {
                        color = "000000";
                    }
                } else {
                    color = "FFFFFF";
                }
                data.add(new String[]{user.getKey(), color});
            }
            userList.setItems(data);
            // Produces cells with background color
            userList.setCellFactory(
                    new Callback<ListView<String[]>, ListCell<String[]>>() {
                        @Override
                        public ListCell<String[]> call(ListView<String[]> list) {
                            return new ColoredListCell();
                        }
                    }
            );
        } catch (TimeoutException exceptionName) {
            exceptionName.printStackTrace();
        }

        userList.setMaxWidth(150);
        userList.setMaxHeight(200);

        final TextField chatField = new TextField("");
        final EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                try {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        if (chatField.getText().matches("/kick .+")) {
                            main.client.kickUser(chatField.getText().substring(6));
                        } else {
                            main.client.chat('r', chatField.getText());
                        }
                        chatField.clear();
                    }
                } catch (TimeoutException ex) {
                    ex.printStackTrace();
                }
            }
        };
        chatField.addEventHandler(KeyEvent.KEY_RELEASED, handler);

        // Add the objects
        grid.add(userList, 3, 0, 5, 20);
        grid.add(ingameChatArea, 0, 0, 3, 20);
        grid.add(chatField, 0, 17, 3, 9);

        primaryStage.setScene(new Scene(grid, 450, 300));
        primaryStage.show();
        grid.setStyle("-fx-background-color: #00BFFF");


        //Prepare updating the chat
        final Duration oneFrameAmt = Duration.seconds(1);
        final KeyFrame keyFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        if (main.client.hasRoomMessages()) {
                            ingameChatArea.appendText(main.client.getLastRoomMessage() + "\n");
                        }

                        ObservableList<String[]> data = FXCollections.observableArrayList();
                        for (Map.Entry<String, String> user : main.client.roomUsers.entrySet()) {
                            String color;
                            if (!user.getValue().equals("spectator")) {
                                if (user.getValue().equals("red")) {
                                    color = "FF0000";
                                } else if (user.getValue().equals("blue")) {
                                    color = "0044FF";
                                } else if (user.getValue().equals("green")) {
                                    color = "00FF00";
                                } else if (user.getValue().equals("yellow")) {
                                    color = "FFFF00";
                                } else {
                                    color = "000000";
                                }
                            } else {
                                color = "FFFFFF";
                            }
                            data.add(new String[]{user.getKey(), color});
                        }
                        userList.setItems(data);

                        if (main.client.kicked) {
                            ingameTimeline.stop();
                            primaryStage.close();
                            main.client.kicked = false;
                        }
                    }
                }
        );

        // Construct a timeline and start it
        this.ingameTimeline = new Timeline(keyFrame);
        this.ingameTimeline.setCycleCount(Animation.INDEFINITE);
        this.ingameTimeline.play();
    }

}


