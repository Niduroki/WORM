package de.hhu.propra14.team101;

import java.io.FileNotFoundException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


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
        Text scenetitle = new Text("Name");
        Button startbtn = new Button("Start");
        Button multibtn = new Button("Multiplayer");
        Button optionsbtn = new Button("Options");
        Button exitbtn = new Button("Exit");

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
                    // TODO temporary hack to start a next round
                } else if (keyEvent.getCode() == KeyCode.S) {
                    // Test saving
                    //MapSaves saver = new MapSaves();
                    //saver.save(game.getLevel(0).getTerrain(), "Map1.yml");
                    //saver.save(game.getLevel(1).getTerrain(), "Map2.yml");
                    //saver.save(game.getLevel(2).getTerrain(), "Map3.yml");
                } else if(keyEvent.getCode() == KeyCode.L) {
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
    }

    public void updateGame (Stage stageName) {
        //
    }
}

