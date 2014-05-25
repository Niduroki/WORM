package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Savers.SettingSaves;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

/**
 * Class to do networking on the client side
 */

public class NetworkClient {

    private int port;
    private String server;
    private int user_id = 0;
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
        this.user_id = this.signIn();
    }

    /**
     * @param data Data to send to the server
     * @return Answer received from the server
     * This method should only be used internally
     */
    private String send (String data) {
        /*if (this.user_id != 0) {
            // We're signed in, send our user id, too
            return "123";
        } else {
            // We're not signed in yet
            return "123";
        }*/
        this.output.println(data);
        this.output.flush();
        return this.input.nextLine();
    }

    /**
     * Callback for socket to handle incoming data
     */
    private void handleIncomingData() {
        //
    }

    /**
     * @return Returns an unique user id
     */
    private int signIn() {
        String id;

        id = this.send("sign me in");
        return Integer.parseInt(id);
    }

    /**
     * @param name How to name the room
     * @return Returns the ID of the room we've just created
     */
    public int createRoom(String name) {
        String answer;

        answer = this.send("create room " + name);
        return Integer.parseInt(answer);
    }
}
