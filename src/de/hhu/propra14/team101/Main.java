package de.hhu.propra14.team101;

/**
 * Main class, that starts the program
 */

public class Main {
    public static void main (String[] args) {
        NetworkClient Network;
        GUI GUI;
        SettingSaves SettingSaves;
        Physics Physics;
        Worms[] WormArray = new Worms[5];

        Network = new NetworkClient(12345, "123.123.123.123");
        GUI = new GUI();
        SettingSaves = new SettingSaves();
        Physics = new Physics();

        System.out.println("Hello world");
    }
}