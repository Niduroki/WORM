package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Game;
import de.hhu.propra14.team101.Main;
import de.hhu.propra14.team101.Networking.Exceptions.*;
import de.hhu.propra14.team101.Savers.SettingSaves;
import de.hhu.propra14.team101.Terrain;
import javafx.scene.canvas.Canvas;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

/**
 * Class to do networking on the client side
 *
 * <pre>
 * {@code
 * NetworkClient client = new NetworkClient(this);
 * System.out.println("My name is: " + client.ourName);
 * String[] existingRooms = client.getRooms();
 * try {
 *     client.createRoom("test");
 * } catch (RoomExistsException e) {
 *     System.out.println("Room exists! Joining instead!");
 *     client.joinRoom("test");
 * }
 * System.out.println("We're in room "+client.currentRoom);
 *
 * Map props = client.getRoomProperties();
 *
 * if (client.weAreOwner) {
 *     client.changeRoomName("test2");
 *     client.changePassword("secret");
 *     client.changeMap("Castle");
 *     client.changeMaxPlayers(5);
 *     client.kickUser("Troll");
 *     client.setWeapon("bazooka", true);
 * } else {
 *     System.out.println("Owner is: " + client.getOwner());
 * }
 *
 * client.changeColor("blue");
 *
 * if (client.hasGlobalMessages()) {
 *     System.out.println("Message: "+client.getLastGlobalMessage());
 * }
 * if (client.hasRoomMessages()) {
 *     System.out.println("Message: "+client.getLastRoomMessage());
 * }
 * client.loadRoomUsers();
 * for (Map.Entry entry : client.roomUsers.entrySet()) {
 *     System.out.println(entry.getValue());
 * }
 *
 * client.chat('r', "Everyone, ready!");
 * client.switchReady();
 * if (client.roomReady) {
 *     System.out.println("Everyone is ready");
 * }
 *
 *
 * client.startGame();
 * // Ingame
 * client.prevWeapon();
 * client.nextWeapon();
 * client.fireWeapon(100, 500);
 * client.move('l');
 * client.useItem(1);
 * client.jump();
 * client.pause();
 * // Unpause
 * client.pause();
 *
 * client.requestSyncGame();
 *
 * if (client.kicked) {
 *     client.leaveRoom();
 *     client.logoff();
 * }
 * }
 * </pre>
 */
public class NetworkClient {

    /** Current room the user is in */
    public String currentRoom;
    /** Map of room users. Map key is the user name, Map value is the users team */
    public Map<String, String> roomUsers = new HashMap<>();
    /** Name of the client */
    public String ourName;
    /** Whether the room we're in is ready to play */
    public boolean roomReady = false;
    /** Whether we're owner of the room we're in */
    public boolean weAreOwner = false;
    /** Whether we've been kicked from the room we're in */
    public boolean kicked = false;
    /** Name of the color we'll use whe playing (or whether we're a spectator) */
    public String color;

    /** Our UUID */
    private UUID uuid;
    /** PrintWriter to write to, for network output */
    private PrintWriter output;
    /** How many messages we sent yet. Needed for waiting for answers on messages */
    private int sentCounter = 0;
    /** Last received message */
    private int lastReceivedCounter = -1;
    /** Last answer */
    private String lastAnswer = "";
    /** Main class */
    private Main main;
    /** Queue to save global chat messages on */
    private PriorityQueue<String> globalMessages = new PriorityQueue<>();
    /** Queue to save room chat messages on */
    private PriorityQueue<String> roomMessages = new PriorityQueue<>();

    /**
     * Constructs a class for networking
     * @param main Main class the app is running with
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
            //Scanner input = new Scanner(connection.getInputStream());
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

    /**
     * Send data to the server
     * @param data String of data
     * @param waitForAnswer Whether to wait for an answer
     * @throws TimeoutException If there has been no answer after 2 seconds
     */
    private void send(String data, boolean waitForAnswer) throws TimeoutException {

        // Reset last answer
        this.lastAnswer = "";

        int ourCount = this.sentCounter;
        this.sentCounter += 1;
        String line = constructLine(data, ourCount);
        this.output.println(line);
        System.out.println("client send::" + line);
        this.output.flush();
        if (waitForAnswer) {
            int waitCounter = 0;
            while (this.lastReceivedCounter < ourCount) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException while waiting!");
                    e.printStackTrace();
                }
                waitCounter += 1;
                // Wait 2s at most
                if (waitCounter > 40) {
                    System.out.println("TIMEOUT");
                    throw (new TimeoutException());
                }
            }
        }
    }

    /**
     * Construct a line with uuid, if there is one, and number of the current message
     * @param data String of data
     * @param count Number of the message
     * @return Constructed line
     */
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
     * @param line Line to handle
     */
    private void handleIncomingData(String line) {
        System.out.println("Handled a " + line);
        if (line.matches("[0-9]+ .+")) {
            lastReceivedCounter = Integer.parseInt(line.split(" ")[0]);
            line = line.substring(line.indexOf(" ") + 1);
        }

        if (line.equals("ping")) {
            try {
                this.send("pong", false);
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
            } else if (type == 'r') {
                roomMessages.add(user + ">: " + message);
            }
        } else if (line.matches("change_team .+ .+")) {
            this.roomUsers.remove(line.split(" ")[1]);
            this.roomUsers.put(line.split(" ")[1], line.split(" ")[2]);
        } else if (line.matches("room_joined .+")) {
            this.roomUsers.put(line.split(" ")[1], "spectator");
        } else if (line.matches("room_left .+")) {
            this.roomUsers.remove(line.split(" ")[1]);
        } else if (line.equals("youre_owner")) {
            this.weAreOwner = true;
        } else if (line.equals("youre_kicked")) {
            this.kicked = true;
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

    /**
     * Sub-callback, to deal with game-related incoming data
     * @param command Incoming game command
     */
    @SuppressWarnings("unchecked")
    private void interpretGame(String command) {
        if (command.equals("started")) {
            try {
                main.field = new Canvas(Terrain.getWidthInPixel(), Terrain.getHeightInPixel());
                this.requestSyncGame();
            } catch (TimeoutException e) {
                System.out.println("Timeout!");
            }
        } else if (command.matches("sync .+")) {
            Yaml yaml = new Yaml();
            this.main.game = Game.deserialize((Map<String, Object>) yaml.load(command.substring(5).replace(';', '\n')));
            this.main.game.online = true;
            Game.startMe = true;
        } else {
            this.main.game.doAction(command);
        }
    }

    /**
     * Sign in to the server and request an UUID
     * @throws TimeoutException On timeout
     */
    private void signIn() throws TimeoutException {
        SettingSaves loader = new SettingSaves();

        try {
            Map data = loader.load("settings.gz");
            this.ourName = (String) data.get("multiplayer_name");
        } catch (FileNotFoundException | NullPointerException e) {
            this.ourName = "Worms player";
        }

        this.send("hello " + this.ourName, true);
    }

    /**
     * Creates a room
     * @param name Name of the room
     * @throws NetworkException RoomExistsException if a room with this name exists, or TimeoutException on timeout
     */
    public void createRoom(String name) throws NetworkException {
        this.send("create_room " + name, true);
        if (!this.lastAnswer.equals("okay")) {
            throw (new RoomExistsException());
        }
        this.currentRoom = name;
        // The first user is always ready
        this.switchReady();
    }

    /**
     * Join a room
     * @param name Room to join
     * @throws NetworkException RoomDoesNotExistException if there's no room, RoomFullException if the room is full
     * TimeoutException on timeout
     */
    public void joinRoom(String name) throws NetworkException {
        this.send("join_room " + name, true);
        if (this.lastAnswer.equals("does_not_exist")) {
            throw new RoomDoesNotExistException();
        } else if (this.lastAnswer.equals("room_full")) {
            throw new RoomFullException();
        }
        this.currentRoom = name;
    }

    /**
     * Leaves the current room
     * @throws NetworkException TODO RoomExistsException???
     */
    public void leaveRoom() throws NetworkException {
        this.send("leave_room", true);
        if (!this.lastAnswer.equals("okay")) {
            throw (new RoomExistsException());
        }
        this.currentRoom = null;
    }

    /**
     * Gets existing rooms
     * @return String array of existing rooms
     * @throws TimeoutException On timeout
     */
    public String[] getRooms() throws TimeoutException {
        this.send("list_rooms", true);
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

    /**
     * Changes the room name
     * @param name Name to change to
     * @throws TimeoutException On timeout
     */
    public void changeRoomName(String name) throws TimeoutException {
        // TODO
    }

    /**
     * Changes the password
     * @param password Password to change to
     * @throws TimeoutException On timeout
     */
    public void changePassword(String password) throws TimeoutException {
        // TODO
    }

    /**
     * Gets the name of the current owner
     * @return Name of the owner
     * @throws TimeoutException On timeout
     */
    public String getOwner() throws TimeoutException {
        this.send("get_owner", true);
        return this.lastAnswer;
    }

    /**
     * Checks whether there are global messages
     * @return Whether there are global messages
     */
    public boolean hasGlobalMessages() {
        return !(globalMessages.size() == 0);
    }

    /**
     * Gets the last global message
     * @return Last global message
     */
    public String getLastGlobalMessage() {
        return globalMessages.poll();
    }

    /**
     * Checks whether there are room messages
     * @return Whether there are room messages
     */
    public boolean hasRoomMessages() {
        return !(roomMessages.size() == 0);
    }

    /**
     * Gets the last room message
     * @return Last room message
     */
    public String getLastRoomMessage() {
        return roomMessages.poll();
    }

    /**
     * Loads current users in the room
     * @throws TimeoutException On timeout
     */
    public void loadRoomUsers() throws TimeoutException {
        this.send("list_room_users", true);
        for (String user : this.lastAnswer.split(",")) {
            roomUsers.put(user.split("\\|")[0], user.split("\\|")[1]);
        }
    }

    /**
     * Chat
     * @param type Globally 'g' or in the room 'r'
     * @param message Text to send
     * @throws TimeoutException On timeout
     */
    public void chat(char type, String message) throws TimeoutException {
        this.send("chat " + type + " " + message, false);
    }

    /**
     * Negate our ready state
     * @throws TimeoutException On timeout
     */
    public void switchReady() throws TimeoutException {
        this.send("ready", true);
    }

    /**
     * Change the current map
     * @param name Name of the new map
     * @throws TimeoutException On timeout
     */
    public void changeMap(String name) throws TimeoutException {
        this.send("change_map "+name, false);
    }

    /**
     * Change to the next weapon ingame
     * @throws TimeoutException On timeout
     */
    public void nextWeapon() throws TimeoutException {
        this.send("game next_weapon", true);
    }

    /**
     * Change to the previous weapon ingame
     * @throws TimeoutException On timeout
     */
    public void prevWeapon() throws TimeoutException {
        this.send("game prev_weapon", true);
    }

    /**
     * Fire the current weapon
     * @param x X coordinate to fire to
     * @param y Y coordinate to fire to
     * @throws TimeoutException On timeout
     */
    public void fireWeapon(int x, int y) throws TimeoutException {
        // The network game needs the Main.sizeMultiplier==1 coordinates
        this.send("game fire "+String.valueOf(x/Main.sizeMultiplier)+" "+String.valueOf(y/Main.sizeMultiplier), true);
    }

    /**
     * Move the worm
     * @param direction Direction to move in, either 'l', for left
     * @throws TimeoutException On timeout
     */
    public void move(char direction) throws TimeoutException {
        if (direction == 'l') {
            this.send("game move_left", true);
        } else if (direction == 'r') {
            this.send("game move_right", true);
        }
    }

    /**
     * Use an item
     * @param number Item number to use
     * @throws TimeoutException On timeout
     */
    public void useItem(int number) throws TimeoutException {
        this.send("game use_item " + number, true);
    }

    /**
     * Make the worm jump
     * @throws TimeoutException On timeout
     */
    public void jump() throws  TimeoutException {
        this.send("game jump", true);
    }

    /**
     * (Un-)Pause the game
     * @throws TimeoutException On timeout
     */
    public void pause() throws TimeoutException {
        this.send("game pause", true);
    }

    /**
     * Start the game
     * @throws TimeoutException On timeout
     */
    public void startGame() throws TimeoutException {
        this.send("start_game", true);
    }

    /**
     * Change our color/team
     * @param team Color name (or "spectator")
     * @throws TimeoutException On timeout
     */
    public void changeColor(String team) throws TimeoutException {
        this.send("change_team " + team, false);
        this.color = team;
    }

    /**
     * Change maximum amounts of players in this room
     * @param amount How many players are allowed
     * @throws TimeoutException On timeout
     */
    public void changeMaxPlayers (int amount) throws TimeoutException {
        this.send("change_max_players "+String.valueOf(amount), true);
    }

    /**
     * Kicks an user
     * @param name Name of the user to kick
     * @throws TimeoutException On timeout
     */
    public void kickUser (String name) throws TimeoutException {
        this.send("kick_user " + name, false);
    }

    /**
     * Gets all room properties
     * @return Map referencing every property with a string
     * @throws TimeoutException On timeout
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getRoomProperties() throws TimeoutException {
        this.send("get_room_properties", true);
        Yaml yaml = new Yaml();
        return (Map<String, Object>) yaml.load(this.lastAnswer.replace(';', '\n'));
    }

    /**
     * Sets a weapon enabled/disabled for the game
     * @param weaponName lowercase name of the weapon
     * @param active boolean whether the weapon should be enabled
     * @throws TimeoutException On timeout
     */
    public void setWeapon(String weaponName, boolean active) throws TimeoutException {
        // Make sure weaponName is lowercase
        weaponName = weaponName.toLowerCase();

        this.send("change_weapon " + weaponName + " " + String.valueOf(active), true);
    }

    /**
     * Hard resyncs the game by asking the server to send the current game state as a save and substitutes the current game with that
     * @throws TimeoutException On timeout
     */
    public void requestSyncGame() throws TimeoutException {
        this.send("game sync", false);
    }

    /**
     * Logs the client out properly
     */
    public void logoff() {
        try {
            this.send("logoff", false);
        } catch (TimeoutException e) {
            //
        }
    }

    /**
     * Thread to handle incoming data asynchronous
     */
    static class HandleIncomingThread implements Runnable {

        Socket socket;
        NetworkClient client;

        /**
         * Creates a thread to handle incoming data
         * @param socket Socket to send data with
         * @param client Parent Client
         */
        public HandleIncomingThread(Socket socket, NetworkClient client) {
            this.socket = socket;
            this.client = client;
        }

        /**
         * Runs the thread
         */
        @Override
        public void run() {
            try {

                Scanner input = new Scanner(socket.getInputStream());
                //PrintWriter output = new PrintWriter(socket.getOutputStream());

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
