package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Savers.SettingSaves;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

/**
 * Class to do networking on the client side
 */

public class NetworkClient {

    private String server;
    private UUID uuid;
    private Scanner input;
    private PrintWriter output;

    /**
     * Constructs a class for networking
     */
    public NetworkClient () {
        int port = 7601;
        SettingSaves loader = new SettingSaves();
        try {
            Map data = loader.load("settings.yml");
            if (data.get("default_server") != null) {
                server = (String) data.get("default_server");
            } else {
                server = "schaepers.it";
            }
        } catch (FileNotFoundException e) {
            //
        }

        try {
            Socket connection = new Socket(server, port);
            this.input = new Scanner(connection.getInputStream());
            this.output = new PrintWriter(connection.getOutputStream());
            new Thread(new HandleIncomingThread(connection, this)).start();
        } catch (IOException e) {
            System.out.println("Can't connect to server");
        }

        this.signIn();
    }

    /**
     * @param data Data to send to the server
     * @return Answer received from the server
     * This method should only be used internally
     */
    private String send (String data) {
        if (this.uuid != null) {
            this.output.println(this.uuid.toString() + " " + data);
            this.output.flush();
            return this.input.nextLine();
        } else {
            // We're not signed in yet
            this.output.println(data);
            this.output.flush();
            return this.input.nextLine();
        }
    }

    /**
     * Callback for socket to handle incoming data
     */
    private void handleIncomingData(String line) {
        System.out.println("Handled a " + line);
        if (line.equals("pong")) {
            this.send("ping");
        } else if (line.matches(NetworkServer.uuidRegex)) {
            this.uuid = UUID.fromString(line);
        }
    }

    private void signIn() {
        SettingSaves loader = new SettingSaves();

        String name;
        try {
            Map data = loader.load("settings.yml");
            name = (String) data.get("multiplayer_name");
        } catch (FileNotFoundException | NullPointerException e) {
            name = "Worms player";
        }

        this.send("hello " + name);
    }

    /**
     * @param name How to name the room
     * @TODO create an own exception, if a room can't be created
     */
    public void createRoom(String name) throws Exception {

        if (!this.send("create_room " + name).equals("okay")) {
            throw (new Exception());
        }
    }

    public void chat(char type, String message) {
        //
    }



    static class HandleIncomingThread implements Runnable {

        Socket socket;
        NetworkClient client;

        public HandleIncomingThread(Socket socket, NetworkClient client) {
            this.socket = socket;
            this.client = client;
        }

        @Override
        public void run() {
            try {

                Scanner input = new Scanner(socket.getInputStream());
                PrintWriter output = new PrintWriter(socket.getOutputStream());

                while (true) {
                    String line = input.nextLine();
                    client.handleIncomingData(line);
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        //
                    }
                }
            } catch (IOException e) {
                System.out.println("Error while communicating with client");
                e.printStackTrace();
            } catch (NoSuchElementException e) {
                System.out.println("Client seemed to quit");
            }
        }
    }
}
