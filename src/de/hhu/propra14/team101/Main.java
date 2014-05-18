package de.hhu.propra14.team101;

import java.io.FileNotFoundException;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.TimelineBuilder;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
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
        Button option1btn = new Button("Zigzag the flux");
        Button option2btn = new Button("Overclock the proton");
        Button option3btn = new Button("Exterminate the dalek");
        Button returnbtn = new Button("Return");
        TextField textField = new TextField();
        ComboBox selection = new ComboBox();
        CheckBox checkBox = new CheckBox();

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.grid.add(scenetitle, 0, 0, 2, 1);
        this.grid.add(textField, 1, 2);
        this.grid.add(option1btn, 2, 2);
        this.grid.add(selection, 1, 4);
        this.grid.add(option2btn, 2, 4);
        this.grid.add(checkBox, 1, 6);
        this.grid.add(option3btn, 2, 6);
        this.grid.add(returnbtn, 1, 8);

        option1btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //
            }
        });

        option2btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //
            }
        });

        option3btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //
            }
        });

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

        final EventHandler<KeyEvent> keypressHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    // Close the game
                    addMainButtons();
                    // Remove this handler, so we can't "reset" in main menu
                    primaryStage.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, this);
                } else if (keyEvent.getCode() == KeyCode.LEFT) {
                    // Move left
                } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                    // Move right
                } else if (keyEvent.getCode() == KeyCode.I) {
                    // Show the inventory
                } else if (keyEvent.getCode() == KeyCode.N) {
                    game.round += 1;
                    // TODO temporary hack to start a next round
                } else if (keyEvent.getCode() == KeyCode.S) {
                    // Save a game
                    GameSaves saver = new GameSaves();
                    saver.save(game, "GameSave.yml");
                } else if (keyEvent.getCode() == KeyCode.L) {
                    // Load a game
                    GameSaves loader = new GameSaves();
                    try {
                        game = loader.load("GameSave.yml");
                        //game.draw(field.getGraphicsContext2D());
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

        game = new Game();
        game.startLevel(0, gc);
        updateGame(primaryStage);
    }

    public void updateGame (Stage stageName) {
        final Duration oneFrameAmt = Duration.millis(1000 / 60);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                new EventHandler() {
                    public void handle(Event event) {
                        redraw(field.getGraphicsContext2D());
                    }
                }); // oneFrame

        // sets the game world's game loop (Timeline)
        TimelineBuilder.create()
                .cycleCount(Animation.INDEFINITE)
                .keyFrames(oneFrame)
                .build()
                .play();
    }

    private void redraw (GraphicsContext gc) {
        this.game.draw(gc);
        //Random rand = new Random();
        //game.getCurrentTerrain().removeTerrainObject(rand.nextInt(60), rand.nextInt(40));
    }
}

