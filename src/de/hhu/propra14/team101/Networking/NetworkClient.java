package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Networking.Exceptions.NetworkException;
import de.hhu.propra14.team101.Networking.Exceptions.RoomExistsException;
import de.hhu.propra14.team101.Networking.Exceptions.TimeoutException;
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

    public String currentRoom;

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
        } catch (TimeoutException e) {
            //
        }
    }

    private void queueSend (String data, boolean waitForAnswer) throws TimeoutException {
        if (this.toSend.size() == 0) {
            this.toSend.add(new NetworkRequest(data, waitForAnswer));
            this.doSending();
        } else {
            this.toSend.add(new NetworkRequest(data, waitForAnswer));
        }
    }

    private void doSending() throws TimeoutException {
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
                        System.out.println("InterruptedException while waiting!");
                        e.printStackTrace();
                    }
                    waitCounter += 1;
                    // Wait 2 seconds at most
                    if (waitCounter > 40) {
                        throw (new TimeoutException());
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

        if (line.equals("ping")) {
            try {
                this.queueSend("pong", true);
            } catch (TimeoutException e) {
                //
            }
        } else if (line.startsWith("chat")) {
            String chatline = line.substring(7);
            if (line.charAt(5) == 'g') {
                String user = chatline.split(" ")[0];
                String message = chatline.substring(chatline.indexOf(" ")+1);
                // TODO display message in the GUI global chat
                System.out.println(user + " wrote " + message + " globally");
            } else if (line.charAt(5) == 'r') {
                String user = line.substring(7, line.substring(7).indexOf(" "));
                String message = line.substring(line.substring(7).indexOf(" "));
                // TODO display message in the GUI room chat
                System.out.println(user + " wrote " + message + " in " + currentRoom);
            }
        } else if (line.startsWith("game")) {
            // TODO interpret whatever we got now
        } else if (line.matches(NetworkServer.uuidRegex)) {
            this.uuid = UUID.fromString(line);
        }

        this.lastAnswer = line;
    }

    private void signIn() throws TimeoutException {
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
     * @throws de.hhu.propra14.team101.Networking.Exceptions.RoomExistsException
     */
    public void createRoom(String name) throws NetworkException {
        System.out.println("Creating a room called " + name);
        this.queueSend("create_room " + name, true);
        this.waitForAnswer();
        if (!this.lastAnswer.equals("okay")) {
            throw (new RoomExistsException());
        }
        this.currentRoom = name;
    }

    public void joinRoom(String name) throws NetworkException {
        this.queueSend("join_room " + name, true);
        this.waitForAnswer();
        if (!this.lastAnswer.equals("okay")) {
            throw (new RoomExistsException());
        }
        this.currentRoom = name;
    }

    public void leaveRoom() throws NetworkException {
        this.queueSend("leave_room", true);
        this.waitForAnswer();
        if (!this.lastAnswer.equals("okay")) {
            throw (new RoomExistsException());
        }
        this.currentRoom = null;
    }

    public String[] getRooms() throws TimeoutException {
        this.queueSend("list_rooms", true);
        this.waitForAnswer();
        return this.lastAnswer.split(",");
    }

    private void waitForAnswer() throws TimeoutException {
        int counter = 0;
        while (counter <= 20) {
            if (!this.lastAnswer.equals("")) {
                return;
            }
        }
        throw (new TimeoutException());
    }

    public void chat(char type, String message) throws TimeoutException {
        this.queueSend("chat " + type + " " + message, true);
        // TODO is this needed?
        this.waitForAnswer();
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
