package de.hhu.propra14.team101;

public class Main {
    public static void main (String[] args) {
        NetworkClient Network;
        GUI GUI;
        SettingSaves SettingSaves;
        Physics Physics;

        Network = new NetworkClient(12345, "123.123.123.123");
        GUI = new GUI();
        SettingSaves = new SettingSaves();
        Physics = new Physics();

        System.out.println("Hello world");
    }
}