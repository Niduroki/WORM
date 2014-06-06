package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Game;
import de.hhu.propra14.team101.Main;
import de.hhu.propra14.team101.Networking.Exceptions.*;
import de.hhu.propra14.team101.Savers.SettingSaves;
import org.yaml.snakeyaml.Yaml;

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
    public ArrayList<String> roomUsers = new ArrayList<>();
    public String ourName;
    public boolean roomReady = false;

    private UUID uuid;
    private PrintWriter output;
    private int sentCounter = 0;
    private int lastReceivedCounter = -1;
    private String lastAnswer = "";
    private ArrayList<NetworkRequest> toSend = new ArrayList<>();
    private Main main;
    private PriorityQueue<String> globalMessages = new PriorityQueue<>();
    private PriorityQueue<String> roomMessages = new PriorityQueue<>();
    // Maximum time in multiples of 50ms to wait for until timeout
    private int maxWait = 40;

    /**
     * Constructs a class for networking
     */
    public NetworkClient(Main main) {
        int port = 7601;
        SettingSaves loader = new SettingSaves();
        String server;
        try {
            Map data = loader.load("settings.gz");
            if (data.get("multiplayer_server") != null) {
                server = (String) data.get("multiplayer_server");
            } else {
                server = "schaepers.it";
            }
        } catch (FileNotFoundException e) {
            server = "schaepers.it";
        }

        try {
            Socket connection = new Socket(server, port);
            Scanner input = new Scanner(connection.getInputStream());
            this.output = new PrintWriter(connection.getOutputStream());
            Thread thread = new Thread(new HandleIncomingThread(connection, this));
            thread.setDaemon(true);
            thread.start();
        } catch (IOException | NullPointerException e) {
            System.out.println("Can't connect to server");
            e.printStackTrace();
        }

        try {
            this.signIn();
        } catch (TimeoutException e) {
            //
        }

        this.main = main;
    }

    private void queueSend(String data, boolean waitForAnswer) {
        this.toSend.add(new NetworkRequest(data, waitForAnswer));
    }

    private void doSending() throws TimeoutException {

        while (!this.toSend.isEmpty()) {
            // Reset last answer
            this.lastAnswer = "";

            NetworkRequest current = this.toSend.get(0);

            int ourCount = this.sentCounter;
            this.sentCounter += 1;
            String line = constructLine(current.data, ourCount);
            this.output.println(line);
            System.out.println("client send::"+line);
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
                    // Wait maxWait*50ms seconds at most (default 2s)
                    if (waitCounter > this.maxWait) {
                        throw (new TimeoutException());
                    }
                }
            }
            this.toSend.remove(0);
        }
    }

    private String constructLine(String data, int count) {
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
            line = line.substring(line.indexOf(" ") + 1);
        }

        if (line.equals("ping")) {
            try {
                this.queueSend("pong", true);
                this.doSending();
            } catch (TimeoutException e) {
                //
            }
        } else if (line.startsWith("chat")) {
            String chatline = line.substring(5);
            String user = chatline.split(" ")[0];
            char type = chatline.split(" ")[1].charAt(0);
            String message = chatline.substring(chatline.indexOf(" ") + 3);
            if (type == 'g') {
                globalMessages.add(user + ">: " + message);
                System.out.println(user + " wrote " + message + " globally");
            } else if (type == 'r') {
                roomMessages.add(user + ">: " + message);
                System.out.println(user + " wrote " + message + " in " + currentRoom);
            }
        } else if (line.matches("room_joined .+")) {
            this.roomUsers.add(line.split(" ")[1]);
        } else if (line.matches("room_left .+")) {
            this.roomUsers.remove(line.split(" ")[1]);
        } else if (line.equals("everyone_ready")) {
            this.roomReady = true;
        } else if (line.equals("everyone_not_ready")) {
            this.roomReady = false;
        } else if (line.startsWith("game")) {
            this.interpretGame(line.substring(5));
        } else if (line.matches(NetworkServer.uuidRegex)) {
            this.uuid = UUID.fromString(line);
        }

        this.lastAnswer = line;
    }

    private void interpretGame(String command) {
        if (command.equals("started")) {
            try {
                this.syncGame();
            } catch (TimeoutException e) {
                System.out.println("Timeout!");
            }
        }/* else if (command.equals("...")) {
            // ...
        }*/
    }

    private void signIn() throws TimeoutException {
        SettingSaves loader = new SettingSaves();

        try {
            Map data = loader.load("settings.gz");
            this.ourName = (String) data.get("multiplayer_name");
        } catch (FileNotFoundException | NullPointerException e) {
            this.ourName = "Worms player";
        }

        this.queueSend("hello " + this.ourName, true);
        this.doSending();
    }

    /**
     * @param name How to name the room
     * @throws de.hhu.propra14.team101.Networking.Exceptions.RoomExistsException
    */
    public void createRoom(String name) throws NetworkException {
        System.out.println("Creating a room called " + name);
        this.queueSend("create_room " + name, true);
        this.doSending();
        if (!this.lastAnswer.equals("okay")) {
            throw (new RoomExistsException());
        }
        this.currentRoom = name;
        // The first user is always ready
        this.switchReady();
    }

    public void joinRoom(String name) throws NetworkException {
        this.queueSend("join_room " + name, true);
        this.doSending();
        if (this.lastAnswer.equals("does_not_exist")) {
            throw (new RoomDoesNotExistException());
        }
        this.currentRoom = name;
    }

    public void leaveRoom() throws NetworkException {
        this.queueSend("leave_room", true);
        this.doSending();
        if (!this.lastAnswer.equals("okay")) {
            throw (new RoomExistsException());
        }
        this.currentRoom = null;
    }

    public String[] getRooms() throws TimeoutException {
        this.queueSend("list_rooms", true);
        this.doSending();
        if (this.lastAnswer.equals("none")) {
            return new String[0];
        } else {
            if(this.lastAnswer.startsWith("rooms")) {
                String rooms = this.lastAnswer.substring(6);
                return rooms.split(",");
            } else {
                return new String[0];
            }
        }
    }

    public boolean hasGlobalMessages() {
        return !(globalMessages.size() == 0);
    }

    public String getLastGlobalMessage() {
        return globalMessages.poll();
    }

    public boolean hasRoomMessages() {
        return !(roomMessages.size() == 0);
    }

    public String getLastRoomMessage() {
        return roomMessages.poll();
    }

    public String[] getUsers() throws TimeoutException {
        this.queueSend("list_users", true);
        this.doSending();
        return this.lastAnswer.split(",");
    }

    public void loadRoomUsers() throws TimeoutException {
        this.queueSend("list_room_users", true);
        this.doSending();
        Collections.addAll(roomUsers, this.lastAnswer.split(","));
    }

    public void chat(char type, String message) throws TimeoutException {
        this.queueSend("chat " + type + " " + message, false);
        this.doSending();
    }

    public void switchReady() throws TimeoutException {
        this.queueSend("ready", true);
        this.doSending();
    }

    public void nextWeapon() throws TimeoutException {
        this.queueSend("next_weapon", true);
        this.doSending();
    }

    public void prevWeapon() throws TimeoutException {
        this.queueSend("prev_weapon", true);
        this.doSending();
    }

    public void move(char direction) throws TimeoutException {
        if (direction == 'l') {
            this.queueSend("move_left", true);
        } else if (direction == 'r') {
            this.queueSend("move_right", true);
        }
        this.doSending();
    }

    public void startGame() throws TimeoutException {
        this.queueSend("start_game", true);
        this.doSending();
    }

    /**
     * Hard resyncs the game by asking the server to send the current game state as a save and substitutes the current game with that
     * @throws TimeoutException
     */
    public void syncGame() throws TimeoutException {
        this.queueSend("game sync", true);
        // This may take a bit, wait up to 10 seconds
        this.maxWait = 200;
        this.doSending();
        // Reset maxWait
        this.maxWait = 40;
        String answer = this.lastAnswer;
        Yaml yaml = new Yaml();
        this.main.game = Game.deserialize((Map<String, Object>) yaml.load(answer.replace(';', '\n')));
        this.main.game.gc = this.main.field.getGraphicsContext2D();
        this.main.game.startLevel();
    }

    public void logoff() {
        try {
            this.queueSend("logoff", false);
            this.doSending();
        } catch (TimeoutException e) {
            //
        }
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
