package de.hhu.propra14.team101;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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
    private Canvas field;
    private Terrain terrain;

    public static void main (String[] args) {
        launch(args);
    }

    /**
     * @param primaryStage
     * Shows the main GUI
     */

    @Override
    public void start (Stage primaryStage){
        primaryStage.setTitle("SPIELNAME");
        GridPane grid =new GridPane ();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text scenetitle = new Text("SPIELNAME");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Button btn = new Button("      START      ");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 2);

        Button btn1 = new Button("MULTIPLAYER");
        hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(btn1);
        grid.add(hbBtn, 1, 4);

        Button btn2 = new Button("   OPTIONEN  ");
        hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(btn2);
        grid.add(hbBtn, 1, 6);

        Button btn3 = new Button("  VERLASSEN  ");
        hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(btn3);
        grid.add(hbBtn, 1, 8);

        field  = new Canvas(400, 300);
        grid.add(field, 2, 0, 1, 9);
        terrain = new Terrain();

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                terrain.draw(field.getGraphicsContext2D());
            }
        });

        Scene scene = new Scene(grid, 575,400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    /**
     * @param stageName
     * Starts the game
     */
    public void startGame (Stage stageName) {
        Physics Physics;
        Worm[] WormArray = new Worm[5];
        Terrain Terrain;

        Physics = new Physics();

        // Create and draw the terrain
        Terrain = new Terrain();
        //Terrain.draw(gc);

        // Create and draw some worms
        WormArray[0] = new Worm(100, 50);
        WormArray[1] = new Worm(120, 40);
        WormArray[2] = new Worm(140, 40);
        WormArray[3] = new Worm(170, 60);
        WormArray[4] = new Worm(180, 50);
        for (int i = 0; i < WormArray.length; i++) {
            //WormArray[i].draw(gc);
        }
    }

    public void updateGame (Stage stageName) {
        //
    }
}

