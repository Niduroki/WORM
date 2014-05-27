package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Savers.SettingSaves;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

/**
 * Class to do networking on the client side
 */

public class NetworkClient {

    private Socket connection;
    private String server;
    private UUID uuid;
    private Scanner input;
    private PrintWriter output;
    private int sentCounter = 0;
    private int lastReceivedCounter = -1;
    private String lastAnswer;
    private ArrayList<NetworkRequest> toSend = new ArrayList<>();

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
            this.connection = new Socket(server, port);
            this.input = new Scanner(connection.getInputStream());
            this.output = new PrintWriter(connection.getOutputStream());
            Thread thread = new Thread(new HandleIncomingThread(connection, this));
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            System.out.println("Can't connect to server");
        }

        try {
            this.signIn();
        } catch (Exception e) {
            //
        }
    }

    /**
     * @param data Data to send to the server
     * @param waitForAnswer whether we should wait for an answer
     * This method should only be used internally
     */
    private void queueSend (String data, boolean waitForAnswer) throws Exception {
        if (this.toSend.size() == 0) {
            this.toSend.add(new NetworkRequest(data, waitForAnswer));
            this.doSending();
        } else {
            this.toSend.add(new NetworkRequest(data, waitForAnswer));
        }
    }

    private void doSending() throws Exception {
        // Reset last answer
        this.lastAnswer = "";

        while (this.toSend.size() > 0) {
            NetworkRequest current = this.toSend.get(0);

            int ourCount = this.sentCounter;
            this.sentCounter += 1;
            String line = constructLine(current.data, ourCount);
            this.output.println(line);
            this.output.flush();
            if (current.waitForAnswer) {
                int waitCounter = 0;
                while (this.lastReceivedCounter < ourCount) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        //
                    }
                    waitCounter += 1;
                    // Wait 2 seconds at most
                    if (waitCounter > 40) {
                        // TODO define our own exception here
                        throw (new Exception());
                    }
                }
            }
            this.toSend.remove(0);
        }
    }

    private String constructLine (String data, int count) {
        String line = String.valueOf(count);
        line += " ";
        if (this.uuid != null) {
            line += this.uuid.toString() + " ";
        }
        line += data;
        return line;
    }

    /**
     * Callback for socket to handle incoming data
     */
    private void handleIncomingData(String line) {
        System.out.println("Handled a " + line);
        if (line.matches("[0-9]+ .+")) {
            lastReceivedCounter = Integer.parseInt(line.split(" ")[0]);
            line = line.substring(line.indexOf(" ")+1);
        }

        if (line.equals("pong")) {
            try {
                this.queueSend("ping", true);
            } catch (Exception e) {
                //
            }
        } else if (line.matches(NetworkServer.uuidRegex)) {
            this.uuid = UUID.fromString(line);
        }

        this.lastAnswer = line;
    }

    private void signIn() throws Exception {
        SettingSaves loader = new SettingSaves();

        String name;
        try {
            Map data = loader.load("settings.yml");
            name = (String) data.get("multiplayer_name");
        } catch (FileNotFoundException | NullPointerException e) {
            name = "Worms player";
        }

        this.queueSend("hello " + name, true);
    }

    /**
     * @param name How to name the room
     * @TODO create an own exception, if a room can't be created
     */
    public void createRoom(String name) throws Exception {
        System.out.println("Creating a room called " + name);
        this.queueSend("create_room " + name, true);
        this.waitForAnswer();
        if (!this.lastAnswer.equals("okay")) {
            throw (new Exception());
        }
    }

    public String[] getRooms() throws Exception {
        this.queueSend("list_rooms", true);
        this.waitForAnswer();
        return this.lastAnswer.split(",");
    }

    private void waitForAnswer() throws Exception {
        int counter = 0;
        while (counter <= 20) {
            if (!this.lastAnswer.equals("")) {
                return;
            }
        }
        throw (new Exception());
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
                }
            } catch (IOException e) {
                System.out.println("Error while communicating with server");
                e.printStackTrace();
            } catch (NoSuchElementException e) {
                System.out.println("Server seemed to quit");
            }
        }
    }
}
