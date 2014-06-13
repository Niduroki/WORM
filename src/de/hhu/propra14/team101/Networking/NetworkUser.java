package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Networking.Exceptions.RoomFullException;

import java.io.PrintWriter;
import java.util.UUID;

public class NetworkUser {

    public String name;
    public PrintWriter networkOutput;
    public boolean gameReady = false;
    public NetworkGame game;
    public long lastPong = System.currentTimeMillis();
    public UUID uuid;
    public String team;
    public NetworkRoom currentRoom;

    public NetworkUser(String name, UUID uuid, PrintWriter networkOutput) {
        this.name = name;
        this.uuid = uuid;
        this.networkOutput = networkOutput;
    }

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

    public void send (String line) {
        networkOutput.println(line);
        networkOutput.flush();
    }
}