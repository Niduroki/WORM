package de.hhu.propra14.team101.Networking;

import java.io.PrintWriter;
import java.util.UUID;

public class NetworkUser {

    public String name;
    public PrintWriter networkOutput;
    public boolean gameReady = false;
    public NetworkGame game;
    public long lastPong = System.currentTimeMillis();
    public UUID uuid;

    private NetworkRoom currentRoom;

    public NetworkUser(String name, UUID uuid, PrintWriter networkOutput) {
        this.name = name;
        this.uuid = uuid;
        this.networkOutput = networkOutput;
    }

    public void joinRoom(NetworkRoom room) {
        if (this.currentRoom != null) {
            this.leaveRoom();
        }

        room.addUser(this);
        this.currentRoom = room;
    }

    public void leaveRoom() {
        this.currentRoom.removeUser(this);
        this.currentRoom = null;
    }

    public NetworkRoom getCurrentRoom() {
        return this.currentRoom;
    }

    public void send (String line) {
        networkOutput.println(line);
        networkOutput.flush();
    }
}