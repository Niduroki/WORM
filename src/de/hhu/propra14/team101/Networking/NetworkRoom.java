package de.hhu.propra14.team101.Networking;

import java.util.ArrayList;

public class NetworkRoom {
    public String name;
    public ArrayList<NetworkUser> users = new ArrayList<>();

    public NetworkRoom(String name, NetworkUser creater) {
        this.name = name;
        this.users.add(creater);
    }

    public void addUser(NetworkUser user) {
        this.users.add(user);
    }
}
