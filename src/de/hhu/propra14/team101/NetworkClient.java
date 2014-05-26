package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Savers.SettingSaves;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

/**
 * Class to do networking on the client side
 */

public class NetworkClient {

    private int port;
    private String server;
    private UUID uuid;
    private Socket connection;
    private Scanner input;
    private PrintWriter output;

    /**
     * Constructs a class for networking
     */
    public NetworkClient () {
        this.port = 7601;
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
            this.connection = new Socket(server, port);
            this.input= new Scanner(this.connection.getInputStream());
            this.output = new PrintWriter(this.connection.getOutputStream());
        } catch (IOException e) {
            System.out.println("Can't connect to server");
        }

        this.uuid = this.signIn();
    }

    /**
     * @param data Data to send to the server
     * @return Answer received from the server
     * This method should only be used internally
     */
    private String send (String data) {
        if (this.uuid != null) {
            this.output.println(this.uuid.toString() + data);
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
    private void handleIncomingData() {
        //
    }

    /**
     * @return Returns an UUID
     */
    private UUID signIn() {
        String id;
        SettingSaves loader = new SettingSaves();

        String name;
        try {
            Map data = loader.load("settings.yml");
            name = (String) data.get("multiplayer_name");
        } catch (FileNotFoundException | NullPointerException e) {
            name = "Worms player";
        }

        id = this.send("hello " + name);
        return UUID.fromString(id);
    }

    /**
     * @param name How to name the room
     */
    public void createRoom(String name) {
        String answer;

        answer = this.send("create_room " + name);
        //return Integer.parseInt(answer);
    }

    public void chat(char type, String message) {
        //
    }
}
