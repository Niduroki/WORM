package de.hhu.propra14.team101;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Main class, that starts the program
 */

public class Main extends Application {

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
        this.grid.setAlignment(Pos.CENTER_LEFT);
        this.grid.setHgap(10);
        this.grid.setVgap(10);
        this.grid.setPadding(new Insets(25, 25, 25, 25));

        this.addMainButtons();

        this.primaryStage = primaryStage;
        Scene scene = new Scene(grid, 575,400);
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
                Canvas field  = new Canvas(400, 300);
                grid.getChildren().clear();
                grid.add(field, 0, 0);
                startGame(field.getGraphicsContext2D());
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

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.grid.add(scenetitle, 0, 0, 2, 1);
        this.grid.add(option1btn, 1, 2);
        this.grid.add(option2btn, 1, 4);
        this.grid.add(option3btn, 1, 6);
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
     * @param gc
     * Starts the gameplay
     */
    public void startGame (GraphicsContext gc) {
        Physics physics;
        Worm[] wormArray = new Worm[5];
        Terrain terrain;

        physics = new Physics();

        // Create and draw the terrain
        terrain = new Terrain();
        terrain.draw(gc);

        // Create and draw some worms
        wormArray[0] = new Worm(50, 180);
        wormArray[1] = new Worm(110, 160);
        wormArray[2] = new Worm(200, 180);
        wormArray[3] = new Worm(250, 180);
        wormArray[4] = new Worm(300, 180);
        for (int i = 0; i < wormArray.length; i++) {
            wormArray[i].draw(gc);
        }
    }

    public void updateGame (Stage stageName) {
        //
    }
}

