package de.hhu.propra14.team101;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class, that starts the program
 */

public class Main extends Application {
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
    public void start(Stage stage) throws Exception {
        stage.show();
    }
}