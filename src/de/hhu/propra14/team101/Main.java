package de.hhu.propra14.team101;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import de.hhu.propra14.team101.Networking.Exceptions.TimeoutException;
import de.hhu.propra14.team101.Networking.NetworkClient;
import de.hhu.propra14.team101.Savers.GameSaves;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.util.Duration;


/**
 * Main class, that starts the program
 */
public class Main extends Application implements Initializable {

    protected Canvas field;
    protected Game game;
    protected GridPane grid;
    protected Stage primaryStage;
    //private int jumping = 0;
    //private Worm jumpingWorm;
    private Timeline timeline;
    protected ArrayList<Player> players;
    protected ArrayList<String> availableColors;
    protected NetworkClient client;
    protected GUI gui;
    protected Lobby lobby;
    public boolean isOnlineGame = false;

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
        this.gui = new GUI(this);
        this.lobby = new Lobby(this);

        primaryStage.setTitle("Name");
        this.grid = new GridPane();
        this.grid.setAlignment(Pos.CENTER);
        this.grid.setHgap(10);
        this.grid.setVgap(10);
        this.grid.setPadding(new Insets(25, 25, 25, 25));

        this.gui.addMainButtons();

        //this.grid.setGridLinesVisible(true);

        this.primaryStage = primaryStage;
        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        grid.setStyle("-fx-background-color: #00BFFF");
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
                        if (isOnlineGame) {
                            //
                        }
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
                    if (isOnlineGame) {
                        try {
                            client.nextWeapon();
                        } catch (TimeoutException e) {
                            //
                        }
                    }
                    currentWorm.nextWeapon();
                } else if (scrollEvent.getDeltaY() < 0) { // Scrolled down
                    if (isOnlineGame) {
                        try {
                            client.prevWeapon();
                        } catch (TimeoutException e) {
                            //
                        }
                    }
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
                    gui.addMainButtons();
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
                    if (isOnlineGame) {
                        try {
                            client.move('l');
                        } catch (TimeoutException e) {
                            //
                        }
                    }
                    int currentWorm = game.getPlayers().get(game.turnOfPlayer).currentWorm;
                    game.getPlayers().get(game.turnOfPlayer).wormList.get(currentWorm).move('l');
                } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                    int currentWorm = game.getPlayers().get(game.turnOfPlayer).currentWorm;
                    game.getPlayers().get(game.turnOfPlayer).wormList.get(currentWorm).move('r');
                    if (isOnlineGame) {
                        try {
                            client.move('r');
                        } catch (TimeoutException e) {
                            //
                        }
                    }
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
                           gui.winScreen(game.getPlayers().get(0).name);
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
        this.gui.winScreen(game.getPlayers().get(0).name);
    }
}

