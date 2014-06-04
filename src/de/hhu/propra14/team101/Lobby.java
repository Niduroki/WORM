package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Networking.Exceptions.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.input.*;
import javafx.util.Duration;

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
        Text scenetitle = new Text("Lobby");
        Button returnbtn = new Button("Back");
        Button create = new Button("Create Game");
        Button join = new Button("Join Game");

        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                globalTimeline.stop();
                addcreateGameBtns();
            }
        });


        list = new ListView<String>();
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
                        addroombtns();
                    }
                } catch (RoomDoesNotExistException e) {
                    //
                } catch (NetworkException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        globalChatArea = new TextArea();
        globalChatArea.setEditable(false);
        globalChatArea.setWrapText(false);

        final TextField chatfield = new TextField("");
        final EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>(){
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

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.main.grid.add(scenetitle, 0, 0, 3, 1);
        this.main.grid.add(list, 0, 1, 3, 2);
        this.main.grid.add(globalChatArea, 0, 3, 3, 5);
        this.main.grid.add(chatfield, 0, 7, 3, 9);
        this.main.grid.add(returnbtn, 0, 11);
        this.main.grid.add(create, 1, 11);
        this.main.grid.add(join, 2, 11);


        returnbtn.setOnAction(new EventHandler<ActionEvent>() {
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

    public void addroombtns() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text(this.main.client.currentRoom);
        Button returnbtn = new Button("Leave");
        Button advanced = new Button("Advanced");


        final ComboBox<String> color = new ComboBox<>();
        color.getItems().add("Red");
        color.getItems().add("Blue");
        color.getItems().add("Green");
        color.getItems().add("Yellow");
        color.getItems().add("Spectator");
        color.setValue("Red");

        returnbtn.setOnAction(new EventHandler<ActionEvent>() {
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

        final ListView list = new ListView<String>();
        try {
            String[] users = this.main.client.getRoomUsers();
            list.setItems(FXCollections.observableArrayList(users));
        } catch (TimeoutException exceptionName) {
            System.out.println(exceptionName.getMessage());
        }
        list.setPrefWidth(250);
        list.setPrefHeight(150);

        final Button ready;
        if (list.getItems().get(0).toString().equals(main.client.ourName)) {
            ready = new Button("Start");
        } else {
            ready = new Button("Ready");
        }
        ready.setStyle("-fx-background-color: #ff0000");

        this.roomChatArea = new TextArea();
        this.roomChatArea.setEditable(false);
        final TextField chatfield = new TextField("");

        final EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent keyEvent) {
                try {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        main.client.chat('r', chatfield.getText());
                        chatfield.clear();
                    }
                } catch (TimeoutException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        };

        chatfield.addEventHandler(KeyEvent.KEY_RELEASED, handler);

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.main.grid.add(ready, 1, 12);
        this.main.grid.add(advanced, 2, 2);
        this.main.grid.add(scenetitle, 0, 0, 3, 1);
        this.main.grid.add(color, 2, 1);
        this.main.grid.add(list, 0, 1, 2, 2);
        this.main.grid.add(this.roomChatArea, 0, 3, 3, 5);
        this.main.grid.add(chatfield, 0, 7, 3, 9);
        this.main.grid.add(returnbtn, 0, 12);

        advanced.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addadvancebtns();
            }
        });

        ready.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (list.getItems().get(0).toString().equals(main.client.ourName)) {
                    try {
                        if (main.client.roomReady) {
                            main.client.startGame();
                            roomTimeline.stop();
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

                        if (list.getItems().get(0).toString().equals(main.client.ourName)) {
                            ready.setText("Start");
                            if (main.client.roomReady) {
                                ready.setStyle(("-fx-background-color: #00ff00"));
                            } else {
                                ready.setStyle("-fx-background-color: #ff0000");
                            }
                        } else {

                        }

                        try {
                            String[] users = main.client.getRoomUsers();
                            list.setItems(FXCollections.observableArrayList(users));
                        } catch (TimeoutException exceptionName) {
                            System.out.println(exceptionName.getMessage());
                        }
                    }
                }
        );

        // Construct a globalTimeline with the mainloop
        this.roomTimeline = new Timeline(keyFrame);
        this.roomTimeline.setCycleCount(Animation.INDEFINITE);
        this.roomTimeline.play();
    }

    private void addcreateGameBtns() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text("Create game");
        Button returnbtn = new Button("Back");
        Button startBtn = new Button("Start");

        Text title1 = new Text("Name");
        Text title2 = new Text("Password");
        Text title3 = new Text("Map");
        final TextField text1 = new TextField("");
        TextField text2 = new TextField("");

        final ComboBox<String> map = new ComboBox<>();
        map.getItems().add("Map 1");
        map.getItems().add("Map 2");
        map.getItems().add("Map 3");
        map.setValue("Map 1");

        final CheckBox weaponBox1 = new CheckBox("Atomic Bomb");
        final CheckBox weaponBox2 = new CheckBox("Grenade");
        final CheckBox weaponBox3 = new CheckBox("Bazooka");

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects

        this.main.grid.add(scenetitle, 0, 0, 3, 1);
        this.main.grid.add(text1, 1, 1);
        this.main.grid.add(text2, 1, 2);
        this.main.grid.add(title1, 0, 1);
        this.main.grid.add(title2, 0, 2);
        this.main.grid.add(title3, 0, 3);
        this.main.grid.add(map, 1, 3);
        this.main.grid.add(returnbtn, 0, 11);
        this.main.grid.add(startBtn, 1, 11);
        this.main.grid.add(weaponBox1, 0, 4);
        this.main.grid.add(weaponBox2, 1, 4);
        this.main.grid.add(weaponBox3, 2, 4);

        returnbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMpButtons();
            }
        });
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    main.client.createRoom(text1.getText());
                    addroombtns();
                } catch (RoomExistsException e) {
                    //
                } catch (NetworkException e) {
                    //
                }
            }
        });
    }

    private void addadvancebtns() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text("Advanced");
        Button returnbtn = new Button("Back");
        Button Create = new Button("Change Properties");

        Text title1 = new Text("Name");
        Text title2 = new Text("Password");
        Text title3 = new Text("Map");
        TextField text1 = new TextField("");
        TextField text2 = new TextField("");

        final ComboBox<String> map = new ComboBox<>();
        map.getItems().add("Map 1");
        map.getItems().add("Map 2");
        map.getItems().add("Map 3");
        map.setValue("Map 1");

        final CheckBox weaponBox1 = new CheckBox("Atomic Bomb");
        final CheckBox weaponBox2 = new CheckBox("Grenade");
        final CheckBox weaponBox3 = new CheckBox("Bazooka");

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects

        this.main.grid.add(scenetitle, 0, 0, 3, 1);
        this.main.grid.add(text1, 1, 1);
        this.main.grid.add(text2, 1, 2);
        this.main.grid.add(title1, 0, 1);
        this.main.grid.add(title2, 0, 2);
        this.main.grid.add(title3, 0, 3);
        this.main.grid.add(map, 1, 3);
        this.main.grid.add(Create, 0, 11);
        this.main.grid.add(weaponBox1, 0, 4);
        this.main.grid.add(weaponBox2, 1, 4);
        this.main.grid.add(weaponBox3, 2, 4);

        returnbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addroombtns();
            }
        });
        Create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addroombtns();
            }
        });
    }
}
