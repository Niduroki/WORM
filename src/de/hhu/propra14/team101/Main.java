package de.hhu.propra14.team101;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
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

public class Main extends Application{
    public static void main (String[] args) {
        launch(args);
        NetworkClient Network;
        GUI GUI;
        SettingSaves SettingSaves;
        Physics Physics;
        Worm[] WormArray = new Worm[5];
        Terrain Terrain;

        Network = new NetworkClient(12345, "123.123.123.123");
        GUI = new GUI();
        SettingSaves = new SettingSaves();
        Physics = new Physics();
        Terrain = new Terrain();

        //Terrain.draw(gc);

        System.out.println("Hello world");
    }
    @Override
    public void start (Stage primaryStage){
        primaryStage.setTitle("SPIELNAME");
        GridPane grid =new GridPane ();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text scenetitle = new Text("SPIELNAME");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);


        Button btn = new Button("      START      ");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.TOP_LEFT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 2);

        Button btn1 = new Button("MULTIPLAYER");
        hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.TOP_LEFT);
        hbBtn.getChildren().add(btn1);
        grid.add(hbBtn, 1, 4);

        Button btn2 = new Button("   OPTIONEN  ");
        hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.TOP_LEFT);
        hbBtn.getChildren().add(btn2);
        grid.add(hbBtn, 1, 6);

        Button btn3 = new Button("  VERLASSEN  ");
        hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.TOP_LEFT);
        hbBtn.getChildren().add(btn3);
        grid.add(hbBtn, 1, 8);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

            }
        });

        Scene scene = new Scene(grid, 575,400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
