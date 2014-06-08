package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Networking.Exceptions.RoomFullException;

import java.util.ArrayList;
import java.util.Collections;

public class NetworkRoom {
    public String name;
    public ArrayList<NetworkUser> users = new ArrayList<>();
    public NetworkUser owner;
    public ArrayList<String> availableColors = new ArrayList<>();
    public boolean empty = false;
    public boolean roomReady = false;
    public String selectedMap;
    /** 0 means infinite */
    public int maxUsers = 0;

    public NetworkRoom(String name, String selectedMap) {
        this.name = name;
        this.selectedMap = selectedMap;
        Collections.addAll(availableColors, "red", "blue", "green", "yellow");
    }

    public void addUser(NetworkUser user) throws RoomFullException {
        if (this.maxUsers != 0 && this.users.size() == this.maxUsers) {
            throw new RoomFullException();
        }
        this.users.add(user);
        this.updateOwner();
    }

    public void removeUser(NetworkUser user) {
        this.users.remove(user);
        this.updateEmpty();
        this.updateOwner();
    }

    public void setRoomReady(boolean state) {
        if (state != this.roomReady) {
            if (state) {
                this.users.get(0).send("everyone_ready");
            } else {
                this.users.get(0).send("everyone_not_ready");
            }
            this.roomReady = state;
        }
        // Do nothing otherwise
    }

    public void propagate(String line) {
        for (NetworkUser user : this.users) {
            user.send(line);
        }
    }

    private void updateOwner() {
        this.owner = this.users.get(0);
    }

    private void updateEmpty() {
        this.empty = this.users.size() == 0;
    }
}
