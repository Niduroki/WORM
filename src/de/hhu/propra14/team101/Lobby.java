package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Networking.Exceptions.TimeoutException;
import de.hhu.propra14.team101.Networking.NetworkClient;
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
import javafx.util.Duration;

/**
 * Class to implement a Lobby for users to chat and find games in
 */
public class Lobby {
    protected Main main;
    private Timeline timeline;
    protected TextArea chatArea;
    protected ListView<String> list;

    public Lobby(Main main) {
        this.main = main;
    }

    public void addMpButtons() {
        this.main.client = new NetworkClient(this.main);
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text("Lobby");
        Button returnbtn = new Button("Back");
        Button create = new Button("Create Game");
        Button join = new Button("Join Game");

        join.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addroombtns();
            }
        });

        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                timeline.stop();
                addcreateGameBtns();
            }
        });


        list = new ListView<String>();
        try {
            String[] users = this.main.client.getUsers();
            list.setItems(FXCollections.observableArrayList(users));
        } catch (TimeoutException exceptionName) {
            System.out.println(exceptionName.getMessage());
        }

        list.setPrefWidth(400);
        list.setPrefHeight(80);

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(false);

        TextField chatfield = new TextField("");
        final EventHandler<KeyEvent> handler = (keyEvent) -> {
            try {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    main.client.chat(chatfield.getText());
                    chatfield.clear();
                }
            } catch (TimeoutException ex) {
                System.out.println(ex.getMessage());
            }
        };

        chatfield.addEventHandler(KeyEvent.KEY_RELEASED, handler);

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.main.grid.add(scenetitle, 0, 0, 3, 1);
        this.main.grid.add(list, 0, 1, 3, 2);
        this.main.grid.add(chatArea, 0, 3, 3, 5);
        this.main.grid.add(chatfield, 0, 7, 3, 9);
        this.main.grid.add(returnbtn, 0, 11);
        this.main.grid.add(create, 1, 11);
        this.main.grid.add(join, 2, 11);


        returnbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                main.gui.addMainButtons();
            }
        });

        //Prepare updating lobby
        final Duration oneFrameAmt = Duration.seconds(1);
        final KeyFrame keyFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        if (main.client.hasMessages()) {
                            chatArea.setText(chatArea.getText() + "\n" + main.client.getLastMessage());
                        }

                        try {
                            String[] users = main.client.getUsers();
                            list.setItems(FXCollections.observableArrayList(users));
                        } catch (TimeoutException exceptionName) {
                            System.out.println(exceptionName.getMessage());
                        }
                    }
                }
        );

        // Construct a timeline with the mainloop
        this.timeline = new Timeline(keyFrame);
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
    }

    public void addroombtns() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text("Platzhalter");
        Button returnbtn = new Button("Leave");
        Button ready = new Button("Ready");
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
                addMpButtons();
            }
        });

        ListView list = new ListView<String>();
        try {
            String[] users = this.main.client.getUsers();
            list.setItems(FXCollections.observableArrayList(users));
        } catch (TimeoutException exceptionName) {
            System.out.println(exceptionName.getMessage());
        }
        list.setPrefWidth(250);
        list.setPrefHeight(150);

        TextArea chatarea = new TextArea();
        chatarea.setEditable(false);
        TextField chatfield = new TextField("");

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.main.grid.add(ready, 1, 12);
        this.main.grid.add(advanced, 2, 2);
        this.main.grid.add(scenetitle, 0, 0, 3, 1);
        this.main.grid.add(color, 2, 1);
        this.main.grid.add(list, 0, 1, 2, 2);
        this.main.grid.add(chatarea, 0, 3, 3, 5);
        this.main.grid.add(chatfield, 0, 7, 3, 9);
        this.main.grid.add(returnbtn, 0, 12);

        advanced.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addadvancebtns();
            }
        });
    }

    private void addcreateGameBtns() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text("Create game");
        Button returnbtn = new Button("Back");
        Button startBtn = new Button("Start");

        Text title1 = new Text("Name");
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
        this.main.grid.add(title3, 0, 2);
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
                //addroombtns();
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
