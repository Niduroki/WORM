package de.hhu.propra14.team101;

import de.hhu.propra14.team101.GUIElements.ColoredListCell;
import de.hhu.propra14.team101.GUIElements.NumberTextField;
import de.hhu.propra14.team101.Networking.Exceptions.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.input.*;
import javafx.util.Callback;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

/**
 * Class to implement a Lobby for users to chat and find games in
 */
public class Lobby {
    protected Main main;
    private Timeline globalTimeline;
    private Timeline roomTimeline;
    protected TextArea globalChatArea;
    protected TextArea roomChatArea;
    protected ListView<String> list;

    public Lobby(Main main) {
        this.main = main;
    }

    public void addMpButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text sceneTitle = new Text("Lobby");
        Button returnButton = new Button("Back");
        Button create = new Button("Create Game");
        Button join = new Button("Join Game");

        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                globalTimeline.stop();
                addCreateGameButtons();
            }
        });


        list = new ListView<>();
        try {
            String[] rooms = this.main.client.getRooms();
            list.setItems(FXCollections.observableArrayList(rooms));
        } catch (TimeoutException exceptionName) {
            System.out.println(exceptionName.getMessage());
        }

        list.setPrefWidth(400);
        list.setPrefHeight(80);

        join.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String selectedRoom = list.getSelectionModel().getSelectedItems().get(0);
                    if (selectedRoom != null) {
                        main.client.joinRoom(selectedRoom);
                        globalTimeline.stop();
                        addRoomButtons();
                    }
                } catch (RoomFullException e) {
                    System.out.println("Room full!");
                } catch (RoomDoesNotExistException e) {
                    System.out.println("Room does not exist!");
                } catch (NetworkException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        globalChatArea = new TextArea();
        globalChatArea.setEditable(false);
        globalChatArea.setWrapText(false);

        final TextField chatField = new TextField("");
        final EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent keyEvent) {
                try {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        main.client.chat('g', chatField.getText());
                        chatField.clear();
                    }
                } catch (TimeoutException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        };

        chatField.addEventHandler(KeyEvent.KEY_RELEASED, handler);

        // Configure each object
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.main.grid.add(sceneTitle, 0, 0, 3, 1);
        this.main.grid.add(list, 0, 1, 3, 2);
        this.main.grid.add(globalChatArea, 0, 3, 3, 5);
        this.main.grid.add(chatField, 0, 7, 3, 9);
        this.main.grid.add(returnButton, 0, 11);
        this.main.grid.add(create, 1, 11);
        this.main.grid.add(join, 2, 11);


        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                main.client.logoff();
                globalTimeline.stop();
                main.gui.addMainButtons();
            }
        });

        //Prepare updating lobby
        final Duration oneFrameAmt = Duration.seconds(1);
        final KeyFrame keyFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        if (main.client.hasGlobalMessages()) {
                            globalChatArea.appendText(main.client.getLastGlobalMessage() + "\n");
                        }

                        try {
                            String[] rooms = main.client.getRooms();
                            list.setItems(FXCollections.observableArrayList(rooms));
                        } catch (TimeoutException exceptionName) {
                            System.out.println(exceptionName.getMessage());
                        }
                    }
                }
        );

        // Construct a globalTimeline with the mainloop
        this.globalTimeline = new Timeline(keyFrame);
        this.globalTimeline.setCycleCount(Animation.INDEFINITE);
        this.globalTimeline.play();
    }

    public void addRoomButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text sceneTitle = new Text(this.main.client.currentRoom);
        Button returnButton = new Button("Leave");
        Button advanced = new Button("Advanced");


        final ComboBox<String> team = new ComboBox<>();
        team.getItems().addAll("Red", "Blue", "Green", "Yellow", "Spectator");
        team.setValue("Spectator");

        team.setOnHiding(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                try {
                    main.client.changeColor(team.getSelectionModel().getSelectedItem().toLowerCase());
                } catch (TimeoutException e) {
                    //
                }
            }
        });

        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    main.client.leaveRoom();
                    roomTimeline.stop();
                    addMpButtons();
                } catch (NetworkException e) {
                    //
                }
            }
        });

        final ListView<String[]> list = new ListView<>();
        try {
            this.main.client.loadRoomUsers();

            ObservableList<String[]> data = FXCollections.observableArrayList();
            for (Map.Entry<String, String> user : this.main.client.roomUsers.entrySet()) {
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
            list.setItems(data);
            list.setCellFactory(
                    new Callback<ListView<String[]>, ListCell<String[]>>() {
                        @Override
                        public ListCell<String[]> call(ListView<String[]> list) {
                            return new ColoredListCell();
                        }
                    }
            );
        } catch (TimeoutException exceptionName) {
            System.out.println(exceptionName.getMessage());
        }

        list.setPrefWidth(250);
        list.setPrefHeight(150);

        final Button ready;
        try {
            this.main.client.weAreOwner = this.main.client.getOwner().equals(this.main.client.ourName);
        } catch (TimeoutException e) {
            this.main.client.weAreOwner = false;
        }

        if (this.main.client.weAreOwner) {
            ready = new Button("Start");
        } else {
            ready = new Button("Ready");
        }
        ready.setStyle("-fx-background-color: #ff0000");

        this.roomChatArea = new TextArea();
        this.roomChatArea.setEditable(false);
        final TextField chatField = new TextField("");

        final EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>(){
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
                    System.out.println(ex.getMessage());
                }
            }
        };

        chatField.addEventHandler(KeyEvent.KEY_RELEASED, handler);

        // Configure each object
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.main.grid.add(ready, 1, 12);
        this.main.grid.add(advanced, 2, 2);
        this.main.grid.add(sceneTitle, 0, 0, 3, 1);
        this.main.grid.add(team, 2, 1);
        this.main.grid.add(list, 0, 1, 2, 2);
        this.main.grid.add(this.roomChatArea, 0, 3, 3, 5);
        this.main.grid.add(chatField, 0, 7, 3, 9);
        this.main.grid.add(returnButton, 0, 12);

        advanced.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addAdvanceButtons();
            }
        });

        ready.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (main.client.weAreOwner) {
                    try {
                        if (main.client.roomReady) {
                            main.client.startGame();
                        }
                    } catch (TimeoutException e) {
                        //
                    }
                } else {
                    try {
                        main.client.switchReady();
                        if (ready.getStyle().equals("-fx-background-color: #00ff00")) {
                            ready.setStyle("-fx-background-color: #ff0000");
                        } else {
                            ready.setStyle("-fx-background-color: #00ff00");
                        }
                    } catch (TimeoutException e) {
                        //
                    }
                }
            }
        });

        //Prepare updating the room
        final Duration oneFrameAmt = Duration.seconds(1);
        final KeyFrame keyFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        if (main.client.hasRoomMessages()) {
                            roomChatArea.appendText(main.client.getLastRoomMessage() + "\n");
                        }

                        if (main.client.weAreOwner) {
                            ready.setText("Start");
                            if (main.client.roomReady) {
                                ready.setStyle(("-fx-background-color: #00ff00"));
                            } else {
                                ready.setStyle("-fx-background-color: #ff0000");
                            }
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
                        list.setItems(data);

                        String selection = team.getSelectionModel().getSelectedItem();
                        team.getItems().clear();
                        team.getItems().addAll("Red", "Blue", "Green", "Yellow", "Spectator");
                        ArrayList<String> usedColors = new ArrayList<>();
                        for (String color: main.client.roomUsers.values()) {
                            if (!color.equals("spectator") || !color.equals(main.client.color)) {
                                // Capitalize
                                usedColors.add(Character.toUpperCase(color.charAt(0)) + color.substring(1));
                            }
                        }
                        team.getItems().removeAll(usedColors);
                        team.getSelectionModel().select(selection);

                        if (Game.startMe) {
                            roomTimeline.stop();
                            Stage stage = new Stage();
                            IngameChat sw = new IngameChat();
                            sw.start(stage);
                            main.grid.getChildren().clear();
                            main.grid.add(main.field, 0, 0);
                            main.initializeHandlers();
                            main.game.gc = main.field.getGraphicsContext2D();
                            // Everyone needs the same fps, otherwise bullets fly at different speed
                            main.game.fps = 16;
                            main.game.startGameplay();
                        }

                        if (main.client.kicked) {
                            roomTimeline.stop();
                            addMpButtons();
                            main.client.kicked = false;
                        }
                    }
                }
        );

        // Construct a globalTimeline with the mainloop
        this.roomTimeline = new Timeline(keyFrame);
        this.roomTimeline.setCycleCount(Animation.INDEFINITE);
        this.roomTimeline.play();
    }

    private void addCreateGameButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text sceneTitle = new Text("Create game");
        Button returnButton = new Button("Back");
        Button startButton = new Button("Start");

        Text title1 = new Text("Name");
        Text title2 = new Text("Password");
        Text title3 = new Text("Map");
        Text title4 = new Text("Maximum players");
        final TextField text1 = new TextField("");
        final TextField text2 = new TextField("");

        final ComboBox<String> map = new ComboBox<>();
        map.getItems().addAll("Map 1", "Map 2", "Map 3");
        map.setValue("Map 1");

        final NumberTextField maxPlayers = new NumberTextField();

        final CheckBox weaponBox1 = new CheckBox("Atomic Bomb");
        final CheckBox weaponBox2 = new CheckBox("Grenade");
        final CheckBox weaponBox3 = new CheckBox("Bazooka");

        // Configure each object
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects

        this.main.grid.add(sceneTitle, 0, 0, 3, 1);
        this.main.grid.add(text1, 1, 1);
        this.main.grid.add(text2, 1, 2);
        this.main.grid.add(title1, 0, 1);
        this.main.grid.add(title2, 0, 2);
        this.main.grid.add(title3, 0, 3);
        this.main.grid.add(title4, 0, 4);
        this.main.grid.add(map, 1, 3);
        this.main.grid.add(maxPlayers, 1, 4);
        this.main.grid.add(returnButton, 0, 11);
        this.main.grid.add(startButton, 1, 11);
        this.main.grid.add(weaponBox1, 0, 5);
        this.main.grid.add(weaponBox2, 1, 5);
        this.main.grid.add(weaponBox3, 2, 5);

        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMpButtons();
            }
        });
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Don't do anything if no name for the room is provided and no weapon selected
                if (!text1.getText().isEmpty() && (
                        weaponBox1.selectedProperty().get() ||
                        weaponBox2.selectedProperty().get() ||
                        weaponBox3.selectedProperty().get()
                )) {
                    try {
                        main.client.createRoom(text1.getText());
                        // If there's a password: Use it
                        if (!text2.getText().isEmpty()) {
                            main.client.changePassword(text2.getText());
                        }
                        main.client.changeMap(map.getSelectionModel().getSelectedItem().replace(" ", "")); // Remove spaces
                        // If the owner defined a max player amount: Use it
                        if (!maxPlayers.getText().isEmpty()) {
                            main.client.changeMaxPlayers(Integer.parseInt(maxPlayers.getText()));
                        }

                        //Submit selected weapons
                        main.client.setWeapon("bazooka", weaponBox1.selectedProperty().get());
                        main.client.setWeapon("grenade", weaponBox2.selectedProperty().get());
                        main.client.setWeapon("atomicbomb", weaponBox3.selectedProperty().get());
                        addRoomButtons();
                    } catch (RoomExistsException e) {
                        text1.setText("");
                    } catch (NetworkException e) {
                        //
                    }
                }
            }
        });
    }

    private void addAdvanceButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text sceneTitle = new Text("Advanced");
        Button returnButton = new Button("Back");
        Button submitButton = new Button("Change Properties");

        Text title1 = new Text("Name");
        Text title2 = new Text("Password");
        Text title3 = new Text("Map");
        Text title4 = new Text("Maximum Players");
        final TextField text1 = new TextField("");
        final TextField text2 = new TextField("");

        final ComboBox<String> map = new ComboBox<>();
        map.getItems().addAll("Map 1", "Map 2", "Map 3");
        map.setValue("Map 1");

        final NumberTextField maxPlayers = new NumberTextField();

        final CheckBox weaponBox1 = new CheckBox("Atomic Bomb");
        final CheckBox weaponBox2 = new CheckBox("Grenade");
        final CheckBox weaponBox3 = new CheckBox("Bazooka");

        // Configure each object
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects

        this.main.grid.add(sceneTitle, 0, 0, 3, 1);
        this.main.grid.add(text1, 1, 1);
        this.main.grid.add(text2, 1, 2);
        this.main.grid.add(title1, 0, 1);
        this.main.grid.add(title2, 0, 2);
        this.main.grid.add(title3, 0, 3);
        this.main.grid.add(title4, 0, 4);
        this.main.grid.add(map, 1, 3);
        this.main.grid.add(maxPlayers, 1, 4);
        this.main.grid.add(returnButton, 0, 11);
        this.main.grid.add(submitButton, 1, 11);
        this.main.grid.add(weaponBox1, 0, 5);
        this.main.grid.add(weaponBox2, 1, 5);
        this.main.grid.add(weaponBox3, 2, 5);

        try {
            Map<String, Object> data = this.main.client.getRoomProperties();
            text1.setText((String) data.get("name"));
            text2.setText((String) data.get("password"));
            map.getSelectionModel().select("Map "+ ((String) data.get("map")).charAt(3));
            // Don't prefill maxPlayers with a 0
            if (!String.valueOf(data.get("max_players")).equals("0")) {
                maxPlayers.setText(String.valueOf(data.get("max_players")));
            }
            // Tick selected weapons
            for (Map.Entry<String, Boolean> entry : ((Map<String, Boolean>) data.get("weapons")).entrySet()) {
                if (entry.getKey().equals("bazooka")) {
                    weaponBox1.selectedProperty().set(entry.getValue());
                } else if (entry.getKey().equals("grenade")) {
                    weaponBox2.selectedProperty().set(entry.getValue());
                } else if (entry.getKey().equals("atomicbomb")) {
                    weaponBox3.selectedProperty().set(entry.getValue());
                } else {
                    System.out.println("Unknown weapon: " + entry.getKey());
                }
            }
        } catch (TimeoutException e) {
            System.out.println("Timeout while loading room properties!");
        }

        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addRoomButtons();
            }
        });
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Don't do anything if no name for the room is provided and no weapon selected
                if (!text1.getText().isEmpty() && (
                        weaponBox1.selectedProperty().get() ||
                        weaponBox2.selectedProperty().get() ||
                        weaponBox3.selectedProperty().get()
                )) {
                    try {
                        // TODO submit only values which changed here
                        main.client.changeRoomName(text1.getText());
                        // If there's a password: Use it
                        if (!text2.getText().isEmpty()) {
                            main.client.changePassword(text2.getText());
                        }
                        main.client.changeMap(map.getSelectionModel().getSelectedItem().replace(" ", "")); // Remove spaces
                        // If the owner defined a max player amount: Use it
                        if (!maxPlayers.getText().isEmpty()) {
                            main.client.changeMaxPlayers(Integer.parseInt(maxPlayers.getText()));
                        }

                        // Submit selected weapons
                        main.client.setWeapon("bazooka", weaponBox1.selectedProperty().get());
                        main.client.setWeapon("grenade", weaponBox2.selectedProperty().get());
                        main.client.setWeapon("atomicbomb", weaponBox3.selectedProperty().get());
                        addRoomButtons();
                    } catch (NetworkException e) {
                        //
                    }
                }
            }
        });
    }

    public class IngameChat extends Application {

        private Timeline ingameTimeline;
        private TextArea ingameChatArea;

        @Override
        public void start(Stage primaryStage) {
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
                userList.setCellFactory(
                        new Callback<ListView<String[]>, ListCell<String[]>>() {
                            @Override
                            public ListCell<String[]> call(ListView<String[]> list) {
                                return new ColoredListCell();
                            }
                        }
                );
            } catch (TimeoutException exceptionName) {
                System.out.println(exceptionName.getMessage());
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
                        System.out.println(ex.getMessage());
                    }
                }
            };
            chatField.addEventHandler(KeyEvent.KEY_RELEASED, handler);

            // Add the objects
            grid.add (userList,3,0,5,20);
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
                                addMpButtons();
                                main.client.kicked = false;
                            }
                        }
                    }
            );

            // Construct a timeline with the loop
            this.ingameTimeline = new Timeline(keyFrame);
            this.ingameTimeline.setCycleCount(Animation.INDEFINITE);
            this.ingameTimeline.play();
        }

    }
}
