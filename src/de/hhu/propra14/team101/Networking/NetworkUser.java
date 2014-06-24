package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Networking.Exceptions.RoomFullException;

import java.io.PrintWriter;
import java.util.UUID;

/**
 * Class to create an online user
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
     *
     * @param name
     * @param uuid
     * @param networkOutput
     */
    public NetworkUser(String name, UUID uuid, PrintWriter networkOutput) {
        this.name = name;
        this.uuid = uuid;
        this.networkOutput = networkOutput;
    }

    /**
     *
     * @param room
     * @throws RoomFullException
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