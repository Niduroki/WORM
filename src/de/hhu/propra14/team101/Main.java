package de.hhu.propra14.team101;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import de.hhu.propra14.team101.Networking.Exceptions.TimeoutException;
import de.hhu.propra14.team101.Networking.NetworkClient;
import de.hhu.propra14.team101.Savers.GameSaves;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


/**
 * Main class, that starts the program
 */
public class Main extends Application implements Initializable {

    public Canvas field;
    public Game game;
    public GridPane grid;
    protected Stage primaryStage;
    //private int jumping = 0;
    //private Worm jumpingWorm;
    protected ArrayList<Player> players;
    protected ArrayList<String> availableColors;
    protected NetworkClient client;
    protected GUI gui;
    protected Lobby lobby;

    public static boolean headless = false;

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

    public void initializeHandlers() {
        final EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (game.paused) {
                    // Don't do anything
                    return;
                }
                if (mouseEvent.isSecondaryButtonDown()) {
                    if (game.turnOfPlayer < game.getPlayers().size()) {
                        if (game.online) {
                            try {
                                client.fireWeapon((int) mouseEvent.getX(), (int) mouseEvent.getY());
                            } catch (TimeoutException e) {
                                //
                            }
                        } else {
                            game.doAction("fire "+String.valueOf((int) mouseEvent.getX())+" "+String.valueOf((int) mouseEvent.getY()));
                        }
                    }
                }
            }
        };

        final EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if (game.paused) {
                    // Don't do anything
                    return;
                }
                // Scrolled up
                Worm currentWorm = game.getPlayers().get(game.turnOfPlayer).wormList.get(game.getPlayers().get(game.turnOfPlayer).currentWorm);
                if (scrollEvent.getDeltaY() > 0) {
                    if (game.online) {
                        try {
                            client.nextWeapon();
                        } catch (TimeoutException e) {
                            //
                        }
                    } else {
                        currentWorm.nextWeapon();
                    }
                } else if (scrollEvent.getDeltaY() < 0) { // Scrolled down
                    if (game.online) {
                        try {
                            client.prevWeapon();
                        } catch (TimeoutException e) {
                            //
                        }
                    } else {
                        currentWorm.prevWeapon();
                    }
                }
            }
        };

        final EventHandler<KeyEvent> keypressHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (game.paused && keyEvent.getCode() != KeyCode.P && keyEvent.getCode() != KeyCode.T) {
                    // Don't do anything
                    return;
                }
                if (keyEvent.getCode() == KeyCode.ESCAPE && !game.online) {
                    // Close the game
                    game.timeline.stop();
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
                    if (game.online) {
                        try {
                            client.move('l');
                        } catch (TimeoutException e) {
                            //
                        }
                    } else {
                        game.doAction("move_left");
                    }
                } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                    if (game.online) {
                        try {
                            client.move('r');
                        } catch (TimeoutException e) {
                            //
                        }
                    } else {
                        game.doAction("move_right");
                    }
                } else if (keyEvent.getCode() == KeyCode.I) {
                    // Show the inventory
                } else if (keyEvent.getCode() == KeyCode.P) {
                    // (Un-)Pause the game
                    if (game.online) {
                        try {
                            client.pause();
                        } catch (TimeoutException e) {
                            //
                        }
                    } else {
                        game.doAction("pause");
                    }
                } else if (keyEvent.getCode() == KeyCode.T && game.online) {
                    // Show the chat ingame here
                } else if (keyEvent.getCode() == KeyCode.X && game.online) {
                    try {
                        client.requestSyncGame();
                    } catch (TimeoutException e) {
                        //
                    }
                } else if (keyEvent.getCode() == KeyCode.S && !game.online) {
                    // Save a game
                    GameSaves saver = new GameSaves();
                    saver.save(game, "GameSave.gz");
                } else if (keyEvent.getCode() == KeyCode.L && !game.online) {
                    // Load a game
                    GameSaves loader = new GameSaves();
                    try {
                        game = loader.load("GameSave.gz", false);
                    } catch (FileNotFoundException e) {
                        //
                    }
                }
            }
        };

        this.primaryStage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, keypressHandler);
        this.primaryStage.getScene().addEventHandler(MouseEvent.ANY, mouseHandler);
        this.primaryStage.getScene().addEventHandler(ScrollEvent.SCROLL, scrollHandler);
    }
}

