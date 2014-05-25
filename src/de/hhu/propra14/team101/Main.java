package de.hhu.propra14.team101;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hhu.propra14.team101.Savers.GameSaves;
import de.hhu.propra14.team101.Savers.SettingSaves;
import de.hhu.propra14.team101.Weapons.AbstractWeapon;
import de.hhu.propra14.team101.Weapons.AtomicBomb;
import de.hhu.propra14.team101.Weapons.Bazooka;
import de.hhu.propra14.team101.Weapons.Grenade;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.scene.paint.Color;


/**
 * Main class, that starts the program
 */
public class Main extends Application {

    protected Canvas field;
    protected Game game;
    protected GridPane grid;
    private Stage primaryStage;
    //private int jumping = 0;
    //private Worm jumpingWorm;
    private Timeline timeline;
    private ArrayList<Player> players;
    private ArrayList<String> availableColors;


    public static void main (String[] args) {
        launch(args);
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

        multibtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //
            }
        });

        optionsbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addOptionsButtons();
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
        Text title2 = new Text("Proton");
        Button returnbtn = new Button("Save & Return");

        final ComboBox<String> selection = new ComboBox<String>();
        selection.getItems().addAll("Overclock", "Normal", "Underclock");
        selection.setValue("Normal");
        final CheckBox checkBox = new CheckBox("Exterminate Dalek");
        String initialValue;
        SettingSaves loader = new SettingSaves();
        try {
            Map<String, Object> data = (Map<String, Object>) loader.load("settings.yml");
            initialValue = data.get("multiplayer_server").toString();
            selection.setValue(data.get("proton").toString());
            checkBox.setSelected(Boolean.parseBoolean(data.get("dalek").toString()));
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find settings file!");
            initialValue = "schaepers.it";
        } catch (NullPointerException e) {
            System.out.println("Missing setting!");
            initialValue = "schaepers.it";
        }

        final TextField textField = new TextField(initialValue);

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.grid.add(scenetitle, 0, 0, 2, 1);
        this.grid.add(title1, 1, 2);
        this.grid.add(textField, 2, 2);
        this.grid.add(title2, 1, 4);
        this.grid.add(selection, 2, 4);
        this.grid.add(checkBox, 1, 6);
        this.grid.add(returnbtn, 1, 8);

        returnbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SettingSaves saver = new SettingSaves();
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("multiplayer_server", textField.getText());
                data.put("proton", selection.getValue());
                data.put("dalek", checkBox.selectedProperty().getValue());
                saver.save(data, "settings.yml");
                addMainButtons();
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

        final ComboBox<String> colorSelection = new ComboBox<String>();
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
                    Map<String, Boolean> weaponMap = new HashMap<String, Boolean>();
                    weaponMap.put("AtomicBomb", weaponBox1.selectedProperty().getValue());
                    weaponMap.put("Grenade", weaponBox2.selectedProperty().getValue());
                    weaponMap.put("Bazooka", weaponBox3.selectedProperty().getValue());

                    players.add(parsePlayerSelection(weaponMap, colorSelection.getValue(), nameField.getText()));

                    field = new Canvas(600, 400);
                    grid.getChildren().clear();
                    grid.add(field, 0, 0);
                    startGameplay(field.getGraphicsContext2D());
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
        Color color;
        if (colorName.equals("Red")) {
            color = Color.RED;
        } else if (colorName.equals("Blue")) {
            color = Color.BLUE;
        } else if (colorName.equals("Green")) {
            color = Color.GREEN;
        } else if (colorName.equals("Yellow")) {
            color = Color.YELLOW;
        } else {
            color = Color.GREY;
        }
        tmpPlayer.color = color;
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
    public void startGameplay(GraphicsContext gc) {

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
                } else if (keyEvent.getCode() == KeyCode.N) {
                    game.nextRound();
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
                } else if(keyEvent.getCode() == KeyCode.M) {
                    //switch to next level
                    if (game.getSelectedLevelNumber() + 1 < game.getCountLevel()) {
                        game.startLevel(game.getSelectedLevelNumber() + 1, field.getGraphicsContext2D());
                    } else {
                        game.startLevel(0, field.getGraphicsContext2D());
                    }
                }
            }
        };

        this.primaryStage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, keypressHandler);
        this.primaryStage.getScene().addEventHandler(MouseEvent.ANY, mouseHandler);
        this.primaryStage.getScene().addEventHandler(ScrollEvent.SCROLL, scrollHandler);

        game = new Game(players);
        game.startLevel(0, gc);

        //Prepare updating game
        final Duration oneFrameAmt = Duration.millis(60);
        final KeyFrame keyFrame = new KeyFrame(oneFrameAmt,
                new EventHandler() {
                    public void handle(Event event) {
                        if(game.isGameFinished())
                        {
                           stopUpdating();
                           winScreen(game.getPlayers().get(0).name);
                        } else{
                            game.updateGame(field.getGraphicsContext2D());
                            System.out.println("Ausgabe");
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

