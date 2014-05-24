package de.hhu.propra14.team101;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import de.hhu.propra14.team101.Savers.GameSaves;
import de.hhu.propra14.team101.Savers.SettingSaves;
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
                field = new Canvas(600, 400);
                grid.getChildren().clear();
                grid.add(field, 0, 0);
                startGameplay(field.getGraphicsContext2D());
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

        startbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addPlayerButtons();
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
        Text title1 = new Text("ZigZag");
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
            initialValue = (String) data.get("zigzag");
            selection.setValue(data.get("proton").toString());
            checkBox.setSelected(Boolean.parseBoolean(data.get("dalek").toString()));
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find settings file!");
            initialValue = "";
        } catch (NullPointerException e) {
            System.out.println("Missing setting!");
            initialValue = "";
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
                data.put("zigzag", textField.getText());
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

        // Create buttons and other objects
        Text scenetitle = new Text("Player 1");
        Text title1 = new Text("Name");
        Text title2 = new Text("Color");
        Button startbtn = new Button("Start");
        Button backbtn = new Button("Back");
        Button addplayerbtn = new Button("Another Player");

        final ComboBox<String> selection = new ComboBox<String>();
        selection.getItems().addAll("Red", "Green", "Blue", "Yellow");
        selection.setValue("Blue");
        final CheckBox checkBox = new CheckBox("Atomic Bomb");
        final CheckBox checkBox2 = new CheckBox("Grenade");
        final CheckBox checkBox3 = new CheckBox("Bazooka");
        String initialValue;
        initialValue = "";
        final TextField textField = new TextField(initialValue);
        textField.setMaxSize(100,20);

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.grid.add(scenetitle, 0, 0, 2, 1);
        this.grid.add(title1, 1, 2);
        this.grid.add(textField, 2, 2);
        this.grid.add(title2, 1, 4);
        this.grid.add(selection, 2, 4);
        this.grid.add(checkBox, 1, 6);
        this.grid.add(checkBox2, 2, 6);
        this.grid.add(checkBox3, 3, 6);
        this.grid.add(startbtn, 3, 8);
        this.grid.add(backbtn, 1, 8);
        this.grid.add(addplayerbtn, 2, 8);

        startbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                field = new Canvas(600, 400);
                grid.getChildren().clear();
                grid.add(field, 0, 0);
                startGameplay(field.getGraphicsContext2D());
            }
        });

        backbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SettingSaves saver = new SettingSaves();
                Map<String, Object> data = new HashMap<String, Object>();
                addMainButtons();
            }
        });
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
                            game.addBullet(currentWorm.fireWeapon(new int[]{(int) mouseEvent.getX(), (int) mouseEvent.getY()}));
                            game.bulletFired = true;
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

        game = new Game();
        game.startLevel(0, gc);

        //Prepare updating game
        final Duration oneFrameAmt = Duration.millis(1000 / 60);
        final KeyFrame keyFrame = new KeyFrame(oneFrameAmt,
                new EventHandler() {
                    public void handle(Event event) {
                        if(game.isGameFinished())
                        {
                           stopUpdating();
                           winScreen(game.getPlayers().get(0).name);
                        } else{
                            game.updateGame(field.getGraphicsContext2D());
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

