package de.hhu.propra14.team101;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import de.hhu.propra14.team101.Networking.Exceptions.TimeoutException;
import javafx.collections.FXCollections;

import de.hhu.propra14.team101.Networking.NetworkClient;
import de.hhu.propra14.team101.Savers.GameSaves;
import de.hhu.propra14.team101.Savers.SettingSaves;
import de.hhu.propra14.team101.Weapons.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;


/**
 * Main class, that starts the program
 */
public class Main extends Application implements Initializable {

    protected Canvas field;
    protected Game game;
    protected GridPane grid;
    private Stage primaryStage;
    //private int jumping = 0;
    //private Worm jumpingWorm;
    private Timeline timeline;
    private ArrayList<Player> players;
    private ArrayList<String> availableColors;
    private NetworkClient client;

    public static void main (String[] args) {
        launch(args);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO use fxml
    }

    /**
     * @param primaryStage
     * Shows the main GUI
     */
    @Override
    public void start (final Stage primaryStage){
        primaryStage.setTitle("Name");
        this.grid = new GridPane();
        this.grid.setAlignment(Pos.CENTER);
        this.grid.setHgap(10);
        this.grid.setVgap(10);
        this.grid.setPadding(new Insets(25, 25, 25, 25));

        this.addMainButtons();

        //this.grid.setGridLinesVisible(true);

        this.primaryStage = primaryStage;
        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        grid.setStyle("-fx-background-color: #00BFFF");
    }

    private void addMainButtons() {
        // Clean up
        grid.getChildren().clear();

        // Create buttons and other objects
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20, 0, 20, 20));

        Text scenetitle = new Text("Name");
        Button startbtn = new Button("Start");
        Button multibtn = new Button("Multiplayer");
        Button optionsbtn = new Button("Options");
        Button exitbtn = new Button("Exit");


        startbtn.setMaxWidth(Double.MAX_VALUE);
        multibtn.setMaxWidth(Double.MAX_VALUE);
        optionsbtn.setMaxWidth(Double.MAX_VALUE);
        exitbtn.setMaxWidth(Double.MAX_VALUE);

        VBox vbButtons = new VBox();
        vbButtons.setSpacing(10);
        vbButtons.setPadding(new Insets(0, 20, 10, 20));
        vbButtons.getChildren().addAll(startbtn, multibtn, optionsbtn, exitbtn);


        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.grid.add(scenetitle, 0, 0, 2, 1);
        this.grid.add(startbtn, 1, 2);
        this.grid.add(multibtn, 1, 4);
        this.grid.add(optionsbtn, 1, 6);
        this.grid.add(exitbtn, 1, 8);

        startbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                players = new ArrayList<>();
                availableColors = new ArrayList<>();
                availableColors.add("Red");
                availableColors.add("Green");
                availableColors.add("Blue");
                availableColors.add("Yellow");
                addPlayerButtons();
            }
        });

        optionsbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addOptionsButtons();
            }
        });

        multibtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMpButtons();
            }
        });

        exitbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage.close();
            }
        });
    }

    private void addOptionsButtons() {
        // Clean up
        this.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text("Options");
        Text title1 = new Text("Multiplayer Server");
        Text title2 = new Text("Multiplayer Name");
        Button returnbtn = new Button("Save & Return");


        final CheckBox checkBox = new CheckBox("Exterminate Dalek");
        String initialValue1;
        String initialValue2;
        SettingSaves loader = new SettingSaves();
        try {
            Map<String, Object> data = (Map<String, Object>) loader.load("settings.yml");
            initialValue1 = data.get("multiplayer_server").toString();
            initialValue2 = data.get("multiplayer_name").toString();
            checkBox.setSelected(Boolean.parseBoolean(data.get("dalek").toString()));
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find settings file!");
            initialValue1 = "schaepers.it";
            initialValue2 = "Worms-player";
        } catch (NullPointerException e) {
            System.out.println("Missing setting!");
            initialValue1 = "schaepers.it";
            initialValue2 = "Worms-player";
        }

        final TextField serverField = new TextField(initialValue1);
        final TextField nameField = new TextField(initialValue2);

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.grid.add(scenetitle, 0, 0, 2, 1);
        this.grid.add(title1, 1, 2);
        this.grid.add(serverField, 2, 2);
        this.grid.add(title2, 1, 4);
        this.grid.add(nameField, 2, 4);
        this.grid.add(checkBox, 1, 6);
        this.grid.add(returnbtn, 1, 8);

        returnbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SettingSaves saver = new SettingSaves();
                Map<String, Object> data = new HashMap<>();
                data.put("multiplayer_server", serverField.getText());
                data.put("multiplayer_name", nameField.getText());
                data.put("dalek", checkBox.selectedProperty().getValue());
                saver.save(data, "settings.yml");
                addMainButtons();
            }
        });
    }


    private void addMpButtons() {
        this.client = new NetworkClient(this);
        // Clean up
        this.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text("Multiplayer");
        Button returnbtn = new Button("Back");
        Button Create = new Button("Create");
        Button Join = new Button ("Join");

        Join.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addroombtns();
            }
        });

        Create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addcreatearoom();
            }
        });


        ListView list = new ListView<String>();
        try {
            String[] rooms = client.getRooms();
            list.setItems(FXCollections.observableArrayList(rooms));
        } catch (TimeoutException exceptionName) {
            String[] rooms = {"Cool room name", "Another room", "Room name", "Some room"};
            list.setItems(FXCollections.observableArrayList(rooms));
        }

        list.setPrefWidth(400);
        list.setPrefHeight(80);

        TextArea chatarea = new TextArea();
        chatarea.setEditable(false);
        chatarea.setWrapText(false);
        TextField chatfield = new TextField("");

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.grid.add(scenetitle, 0, 0, 3, 1);
        this.grid.add(list, 0, 1, 3, 2);
        this.grid.add(chatarea, 0, 3, 3, 5);
        this.grid.add(chatfield, 0, 7, 3, 9);
        this.grid.add(returnbtn, 0, 11);
        this.grid.add(Create,1,11);
        this.grid.add(Join,2,11);


        returnbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMainButtons();
            }
        });
    }
    private void addroombtns() {
        // Clean up
        this.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text("Platzhalter");
        Button returnbtn = new Button("Leave");
        Button ready = new Button("Ready");
        Button advanced = new Button ("Advanced");


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
        ObservableList items = FXCollections.observableArrayList("Spieler 1", "Spieler 2", "Spieler 3", "Spieler 4");
        list.setItems(items);
        list.setPrefWidth(250);
        list.setPrefHeight(150);

        TextArea chatarea = new TextArea();
        chatarea.setEditable(false);
        TextField chatfield = new TextField("");

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.grid.add(ready,1,12);
        this.grid.add(advanced, 2, 2);
        this.grid.add(scenetitle, 0, 0, 3, 1);
        this.grid.add(color, 2, 1);
        this.grid.add(list, 0, 1, 2, 2);
        this.grid.add(chatarea, 0, 3, 3, 5);
        this.grid.add(chatfield, 0, 7, 3, 9);
        this.grid.add(returnbtn, 0, 12);

        advanced.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addadvancebtns();
            }
        });

    }
    private void addcreatearoom() {
        // Clean up
        this.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text("Create a room");
        Button returnbtn = new Button("Back");
        Button Create = new Button("Create");

        Text title1 = new Text ("Name");
        Text title2 = new Text ("Password");
        Text title3 = new Text ("Map");
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

        this.grid.add(scenetitle, 0, 0, 3, 1);
        this.grid.add(text1,1,1);
        this.grid.add(text2,1,2);
        this.grid.add(title1,0,1);
        this.grid.add(title2,0,2);
        this.grid.add(title3,0,3);
        this.grid.add(map,1,3);
        this.grid.add(returnbtn, 0, 11);
        this.grid.add(Create,1,11);
        this.grid.add(weaponBox1,0,4);
        this.grid.add(weaponBox2,1,4);
        this.grid.add(weaponBox3,2,4);

        returnbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMpButtons();
            }
        });
        Create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addroombtns();
            }
        });
    }
    private void addadvancebtns() {
        // Clean up
        this.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text("Advanced");
        Button returnbtn = new Button("Back");
        Button Create = new Button("Change Properties");

        Text title1 = new Text ("Name");
        Text title2 = new Text ("Password");
        Text title3 = new Text ("Map");
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

        this.grid.add(scenetitle, 0, 0, 3, 1);
        this.grid.add(text1,1,1);
        this.grid.add(text2,1,2);
        this.grid.add(title1,0,1);
        this.grid.add(title2,0,2);
        this.grid.add(title3,0,3);
        this.grid.add(map,1,3);
        this.grid.add(Create,0,11);
        this.grid.add(weaponBox1,0,4);
        this.grid.add(weaponBox2,1,4);
        this.grid.add(weaponBox3,2,4);

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

    private void addPlayerButtons() {
        // Clean up
        this.grid.getChildren().clear();
        Integer currentPlayer = this.players.size()+1;

        // Create buttons and other objects
        Text scenetitle = new Text( "Player " + currentPlayer.toString());
        Text title1 = new Text("Name");
        Text title2 = new Text("Color");
        Button backbtn = new Button("Back");

        final ComboBox<String> colorSelection = new ComboBox<>();
        colorSelection.getItems().addAll(availableColors);
        colorSelection.setValue(availableColors.get(0));
        final CheckBox weaponBox1 = new CheckBox("Atomic Bomb");
        final CheckBox weaponBox2 = new CheckBox("Grenade");
        final CheckBox weaponBox3 = new CheckBox("Bazooka");
        final TextField nameField = new TextField();
        nameField.setMaxSize(100, 20);

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.grid.add(scenetitle, 0, 0, 2, 1);
        this.grid.add(title1, 1, 2);
        this.grid.add(nameField, 2, 2);
        this.grid.add(title2, 1, 4);
        this.grid.add(colorSelection, 2, 4);
        this.grid.add(weaponBox1, 1, 6);
        this.grid.add(weaponBox2, 2, 6);
        this.grid.add(weaponBox3, 3, 6);
        this.grid.add(backbtn, 1, 8);

        if (players.size() != 0) {
            Button startbtn = new Button("Start");
            this.grid.add(startbtn, 3, 8);

            startbtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    // Don't start without a weapon
                    if (!(
                            weaponBox1.selectedProperty().getValue() ||
                            weaponBox2.selectedProperty().getValue() ||
                            weaponBox3.selectedProperty().getValue()
                    )) {
                        return;
                    }
                    Map<String, Boolean> weaponMap = new HashMap<String, Boolean>();
                    weaponMap.put("AtomicBomb", weaponBox1.selectedProperty().getValue());
                    weaponMap.put("Grenade", weaponBox2.selectedProperty().getValue());
                    weaponMap.put("Bazooka", weaponBox3.selectedProperty().getValue());

                    players.add(parsePlayerSelection(weaponMap, colorSelection.getValue(), nameField.getText()));

                    addMapSelectionButtons();
                }
            });
        }

        backbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMainButtons();
            }
        });

        if (players.size() <= 2) {
            Button addplayerbtn = new Button("Another Player");

            this.grid.add(addplayerbtn, 2, 8);

            addplayerbtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    // Don't start without a weapon
                    if (!(
                            weaponBox1.selectedProperty().getValue() ||
                            weaponBox2.selectedProperty().getValue() ||
                            weaponBox3.selectedProperty().getValue()
                    )) {
                        return;
                    }
                    Map<String, Boolean> weaponMap = new HashMap<String, Boolean>();
                    weaponMap.put("AtomicBomb", weaponBox1.selectedProperty().getValue());
                    weaponMap.put("Grenade", weaponBox2.selectedProperty().getValue());
                    weaponMap.put("Bazooka", weaponBox3.selectedProperty().getValue());

                    players.add(parsePlayerSelection(weaponMap, colorSelection.getValue(), nameField.getText()));
                    addPlayerButtons();
                }
            });
        }
    }

    private void addMapSelectionButtons() {
        // Clean up
        this.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text("Select a map");
        Button backbtn = new Button("Back");
        Button startbtn = new Button("Start");

        final ComboBox<String> mapSelection = new ComboBox<>();
        mapSelection.getItems().addAll("Map 1", "Map 2", "Map 3");
        mapSelection.setValue("Map 1");

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.grid.add(scenetitle, 0, 0, 2, 1);
        this.grid.add(mapSelection, 0, 4);
        this.grid.add(backbtn, 0, 8);
        this.grid.add(startbtn, 1, 8);

        backbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMainButtons();
            }
        });

        startbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                field = new Canvas(600, 400);
                grid.getChildren().clear();
                grid.add(field, 0, 0);
                int levelNumber = Integer.parseInt(mapSelection.getValue().split(" ")[1])-1;
                startGameplay(levelNumber, field.getGraphicsContext2D());
            }
        });
        }

    private Player parsePlayerSelection(Map<String, Boolean> weaponMap, String colorName, String playerName) {
        ArrayList<AbstractWeapon> weapons = new ArrayList<>();
        if (weaponMap.get("AtomicBomb")) {
            weapons.add(new AtomicBomb());
        }
        if (weaponMap.get("Grenade")) {
            weapons.add(new Grenade());
        }
        if (weaponMap.get("Bazooka")) {
            weapons.add(new Bazooka());
        }

        ArrayList<Worm> wormsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            wormsList.add(new Worm(weapons));
        }
        Player tmpPlayer = new Player(wormsList, "Local");
        tmpPlayer.color = Player.deseserializeColor(colorName);
        this.availableColors.remove(this.availableColors.indexOf(colorName));
        tmpPlayer.name = playerName;
        return tmpPlayer;
    }

    private void winScreen(String winner) {
        // Clean up
        grid.getChildren().clear();

        // Create buttons and other objects
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20, 0, 20, 20));

        Text scenetitle = new Text("Player "+winner+" won!");
        Button returnbtn = new Button("Return");

        returnbtn.setMaxWidth(Double.MAX_VALUE);

        VBox vbButtons = new VBox();
        vbButtons.setSpacing(10);
        vbButtons.setPadding(new Insets(0, 20, 10, 20));
        vbButtons.getChildren().add(returnbtn);


        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.grid.add(scenetitle, 0, 0, 2, 1);
        this.grid.add(returnbtn, 1, 2);

        returnbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMainButtons();
            }
        });
    }

    /**
     * Starts the gameplay
     */
    public void startGameplay(int levelNumber, GraphicsContext gc) {

        final EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.isSecondaryButtonDown()) {
                    if (game.turnOfPlayer < game.getPlayers().size()) {
                        Worm currentWorm = game.getPlayers().get(game.turnOfPlayer).wormList.get(game.getPlayers().get(game.turnOfPlayer).currentWorm);
                        // Don't fire without a weapon
                        if (currentWorm.weaponList.size() != 0) {
                            game.fireBullet(currentWorm.fireWeapon(mouseEvent.getX(), mouseEvent.getY()));
                        }
                    }
                }
            }
        };

        final EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                // Scrolled up
                Worm currentWorm = game.getPlayers().get(game.turnOfPlayer).wormList.get(game.getPlayers().get(game.turnOfPlayer).currentWorm);
                if (scrollEvent.getDeltaY() > 0) {
                    currentWorm.nextWeapon();
                } else if (scrollEvent.getDeltaY() < 0) { // Scrolled down
                    currentWorm.prevWeapon();
                }
            }
        };

        final EventHandler<KeyEvent> keypressHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    // Close the game
                    timeline.stop();
                    addMainButtons();
                    // Remove old handlers
                    primaryStage.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, this);
                    primaryStage.getScene().removeEventHandler(ScrollEvent.SCROLL, scrollHandler);
                    primaryStage.getScene().removeEventHandler(MouseEvent.ANY, mouseHandler);
                } else if (keyEvent.getCode() == KeyCode.UP) {
                    // Don't do weird double-jumps
                    //TODO: Add jumping again with physics
                    //if (jumping == 0) {
                    //    int currentWorm = game.getPlayers().get(game.turnOfPlayer).currentWorm;
                    //    jumping = 4;
                    //    jumpingWorm = game.getPlayers().get(game.turnOfPlayer).wormList.get(currentWorm);
                    //}
                } else if (keyEvent.getCode() == KeyCode.LEFT) {
                    int currentWorm = game.getPlayers().get(game.turnOfPlayer).currentWorm;
                    game.getPlayers().get(game.turnOfPlayer).wormList.get(currentWorm).move('l');
                } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                    int currentWorm = game.getPlayers().get(game.turnOfPlayer).currentWorm;
                    game.getPlayers().get(game.turnOfPlayer).wormList.get(currentWorm).move('r');
                } else if (keyEvent.getCode() == KeyCode.I) {
                    // Show the inventory
                } else if (keyEvent.getCode() == KeyCode.S) {
                    // Save a game
                    GameSaves saver = new GameSaves();
                    saver.save(game, "GameSave.yml");
                } else if (keyEvent.getCode() == KeyCode.L) {
                    // Load a game
                    GameSaves loader = new GameSaves();
                    try {
                        game = loader.load("GameSave.yml");
                    } catch (FileNotFoundException e) {
                        //
                    }
                }
            }
        };

        this.primaryStage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, keypressHandler);
        this.primaryStage.getScene().addEventHandler(MouseEvent.ANY, mouseHandler);
        this.primaryStage.getScene().addEventHandler(ScrollEvent.SCROLL, scrollHandler);

        game = new Game(players);
        game.startLevel(levelNumber, gc);

        //Prepare updating game
        final Duration oneFrameAmt = Duration.millis(60);
        final KeyFrame keyFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        if(game.isGameFinished())
                        {
                           stopUpdating();
                           winScreen(game.getPlayers().get(0).name);
                        } else{
                            game.updateGame(field.getGraphicsContext2D());
                            //System.out.println("Ausgabe");
                        }
                       
                    }
                });

        // Construct a timeline with the mainloop
        this.timeline = new Timeline(keyFrame);
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
    }

    private void stopUpdating() {
        this.timeline.stop();
        this.winScreen(game.getPlayers().get(0).name);
    }
}

