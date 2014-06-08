package de.hhu.propra14.team101;

import de.hhu.propra14.team101.GUIElements.NumberTextField;
import de.hhu.propra14.team101.Networking.Exceptions.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.input.*;
import javafx.util.Callback;
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

    public void addChatButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        globalChatArea = new TextArea();
        globalChatArea.setEditable(false);
        globalChatArea.setWrapText(false);

        final ListView list2 = new ListView<String>();
        String[] users = new String[0];
        list2.setItems(FXCollections.observableArrayList(users));

        list2.setMaxWidth(150);
        list2.setMaxHeight(200);

        final TextField chatfield = new TextField("");
        final EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                try {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        main.client.chat('g', chatfield.getText());
                        chatfield.clear();
                    }
                } catch (TimeoutException ex) {
                    System.out.println(ex.getMessage());
                }
                }
        };
        chatfield.addEventHandler(KeyEvent.KEY_RELEASED, handler);
        // Add the objects
        this.main.grid.add (list2,3,3,5,5);
        this.main.grid.add(globalChatArea, 0, 3, 3, 5);
        this.main.grid.add(chatfield, 0, 7, 3, 9);
    }

    public void addMpButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text sceneTitle = new Text("Lobby");
        Button Chat = new Button ("Chat");
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
        this.main.grid.add(Chat, 3,11);


        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                main.client.logoff();
                globalTimeline.stop();
                main.gui.addMainButtons();
            }
        });

        Chat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                addChatButtons();
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
                try {
                    main.client.createRoom(text1.getText());
                    // If there's a password: Use it
                    if (!text2.getText().isEmpty()) {
                        //main.client.changePassword(text2.getText());
                    }
                    main.client.changeMap(map.getSelectionModel().getSelectedItem().replace(" ", "")); // Remove spaces
                    // If the owner defined a max player amount: Use it
                    if (!maxPlayers.getText().isEmpty()) {
                        main.client.changeMaxPlayers(Integer.parseInt(maxPlayers.getText()));
                    }
                    addRoomButtons();
                } catch (RoomExistsException e) {
                    text1.setText("");
                } catch (NetworkException e) {
                    //
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
            Map<String, String> data = this.main.client.getRoomProperties();
            text1.setText(data.get("name"));
            text2.setText(data.get("password"));
            map.getSelectionModel().select("Map "+data.get("map").charAt(3));
            maxPlayers.setText(data.get("max_players"));
            // TODO check selected weapons
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
                try {
                    // TODO change which values changed here
                    //main.client.changeRoomName(text1.getText());
                    // If there's a password: Use it
                    if (!text2.getText().isEmpty()) {
                        //main.client.changePassword();
                    }
                    main.client.changeMap(map.getSelectionModel().getSelectedItem().replace(" ", "")); // Remove spaces
                    // If the owner defined a password: Use it
                    if (!maxPlayers.getText().isEmpty()) {
                        main.client.changeMaxPlayers(Integer.parseInt(maxPlayers.getText()));
                    }
                    addRoomButtons();
                } catch (NetworkException e) {
                    //
                }
            }
        });
    }

    static class ColoredListCell extends ListCell<String[]> {
        @Override
        public void updateItem(String[] item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item[0]);
                setStyle("-fx-background-color: #"+item[1]);
            }
        }
    }
}
