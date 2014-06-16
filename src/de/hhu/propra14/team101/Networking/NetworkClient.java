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
 */

public class NetworkClient {

    public String currentRoom;
    /** Map of room users. Map key is the user name, Map value is the users team */
    public Map<String, String> roomUsers = new HashMap<>();
    public String ourName;
    public boolean roomReady = false;
    public boolean weAreOwner = false;
    public boolean kicked = false;
    public String color;

    private UUID uuid;
    private PrintWriter output;
    private int sentCounter = 0;
    private int lastReceivedCounter = -1;
    private String lastAnswer = "";
    private Main main;
    private PriorityQueue<String> globalMessages = new PriorityQueue<>();
    private PriorityQueue<String> roomMessages = new PriorityQueue<>();

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
     * @param name How to name the room
     * @throws de.hhu.propra14.team101.Networking.Exceptions.RoomExistsException
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

    public void joinRoom(String name) throws NetworkException {
        this.send("join_room " + name, true);
        if (this.lastAnswer.equals("does_not_exist")) {
            throw new RoomDoesNotExistException();
        } else if (this.lastAnswer.equals("room_full")) {
            throw new RoomFullException();
        }
        this.currentRoom = name;
    }

    public void leaveRoom() throws NetworkException {
        this.send("leave_room", true);
        if (!this.lastAnswer.equals("okay")) {
            throw (new RoomExistsException());
        }
        this.currentRoom = null;
    }

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

    public void changeRoomName(String name) throws TimeoutException {
        // TODO
    }

    public void changePassword(String password) throws TimeoutException {
        // TODO
    }

    public String getOwner() throws TimeoutException {
        this.send("get_owner", true);
        return this.lastAnswer;
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

    /*public String[] getUsers() throws TimeoutException {
        this.send("list_users", true);
        return this.lastAnswer.split(",");
    }*/

    public void loadRoomUsers() throws TimeoutException {
        this.send("list_room_users", true);
        for (String user : this.lastAnswer.split(",")) {
            roomUsers.put(user.split("\\|")[0], user.split("\\|")[1]);
        }
    }

    public void chat(char type, String message) throws TimeoutException {
        this.send("chat " + type + " " + message, false);
    }

    public void switchReady() throws TimeoutException {
        this.send("ready", true);
    }

    public void changeMap(String name) throws TimeoutException {
        this.send("change_map "+name, false);
    }

    public void nextWeapon() throws TimeoutException {
        this.send("game next_weapon", true);
    }

    public void prevWeapon() throws TimeoutException {
        this.send("game prev_weapon", true);
    }

    public void fireWeapon(int x, int y) throws TimeoutException {
        this.send("game fire "+String.valueOf(x)+" "+String.valueOf(y), true);
    }

    public void move(char direction) throws TimeoutException {
        if (direction == 'l') {
            this.send("game move_left", true);
        } else if (direction == 'r') {
            this.send("game move_right", true);
        }
    }

    public void jump() throws  TimeoutException {
        this.send("game jump", true);
    }

    public void pause() throws TimeoutException {
        this.send("game pause", true);
    }

    public void startGame() throws TimeoutException {
        this.send("start_game", true);
    }

    public void changeColor(String team) throws TimeoutException {
        this.send("change_team " + team, false);
        this.color = team;
    }

    public void changeMaxPlayers (int amount) throws TimeoutException {
        this.send("change_max_players "+String.valueOf(amount), true);
    }

    public void kickUser (String name) throws TimeoutException {
        this.send("kick_user " + name, false);
    }

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
     */
    public void setWeapon(String weaponName, boolean active) throws TimeoutException {
        // Make sure weaponName is lowercase
        weaponName = weaponName.toLowerCase();

        this.send("change_weapon " + weaponName + " " + String.valueOf(active), true);
    }

    /**
     * Hard resyncs the game by asking the server to send the current game state as a save and substitutes the current game with that
     * @throws TimeoutException
     */
    public void requestSyncGame() throws TimeoutException {
        this.send("game sync", false);
    }

    public void logoff() {
        try {
            this.send("logoff", false);
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
