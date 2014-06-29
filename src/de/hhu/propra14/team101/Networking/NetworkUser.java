package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Networking.Exceptions.RoomFullException;

import java.io.PrintWriter;
import java.util.UUID;

/**
 * Class to create an online user, for use with the NetworkServer class
 *
 * <pre>
 * {@code
 * NetworkUser user = new NetworkUser("Username", uuid, client.output);
 * user.joinRoom("test");
 * user.leaveRoom();
 * user.send("okay");
 * }
 * </pre>
 */
public class NetworkUser {
    /** Name of the user */
    public String name;
    /** PrintWriter to write to, to send data to the client */
    public PrintWriter networkOutput;
    /** Whether the user is ready to play */
    public boolean gameReady = false;
    /** NetworkGame the user is playing in */
    public NetworkGame game;
    /** Last time we received a pong from the user TODO does this have any use? */
    public long lastPong = System.currentTimeMillis();
    /** UUID of the user */
    public UUID uuid;
    /** Color/Team of the user */
    public String team;
    /** Room the user is currently in */
    public NetworkRoom currentRoom;

    /**
     * Creates a new network user
     * @param name Name of the user
     * @param uuid UUID of the user
     * @param networkOutput Output to write to
     */
    public NetworkUser(String name, UUID uuid, PrintWriter networkOutput) {
        this.name = name;
        this.uuid = uuid;
        this.networkOutput = networkOutput;
    }

    /**
     * User joins a room
     * @param room Room to join
     * @throws RoomFullException If the room is full
     */
    public void joinRoom(NetworkRoom room) throws RoomFullException {
        if (this.currentRoom != null) {
            this.leaveRoom();
        }

        room.addUser(this);
        this.currentRoom = room;
        this.team = "spectator";

        // Propagate
        for (NetworkUser user: this.currentRoom.users) {
            // We don't need this info
            if (user != this) {
                user.send("room_joined " + name);
            }
        }
    }

    /**
     * Makes the user leave his room and clean up after him
     */
    public void leaveRoom() {
        this.currentRoom.removeUser(this);

        // Propagate
        for (NetworkUser user: this.currentRoom.users) {
            // We don't need this info
            if (user != this) {
                user.send("room_left " + name);
            }
        }
        this.currentRoom = null;
    }

    /**
     * Send a line to the client
     * @param line Line to send
     */
    public void send (String line) {
        networkOutput.println(line);
        networkOutput.flush();
    }
}