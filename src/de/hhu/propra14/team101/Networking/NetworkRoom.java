package de.hhu.propra14.team101.Networking;

import java.util.ArrayList;

public class NetworkRoom {
    public String name;
    public ArrayList<NetworkUser> users = new ArrayList<>();
    public boolean empty = false;
    public boolean roomReady = false;

    public NetworkRoom(String name) {
        this.name = name;
    }

    public void addUser(NetworkUser user) {
        this.users.add(user);
    }

    public void removeUser(NetworkUser user) {
        this.users.remove(user);
        this.updateEmpty();
    }

    private void updateEmpty() {
        this.empty = this.users.size() == 0;
    }
}
