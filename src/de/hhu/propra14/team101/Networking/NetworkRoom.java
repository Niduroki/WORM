package de.hhu.propra14.team101.Networking;

import java.util.ArrayList;

public class NetworkRoom {
    public String name;
    public ArrayList<NetworkUser> users = new ArrayList<>();
    public boolean empty = false;
    public boolean roomReady = false;
    public int selectedMap;

    public NetworkRoom(String name, int selectedMap) {
        this.name = name;
        this.selectedMap = selectedMap;
    }

    public void addUser(NetworkUser user) {
        this.users.add(user);
    }

    public void removeUser(NetworkUser user) {
        this.users.remove(user);
        this.updateEmpty();
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

    private void updateEmpty() {
        this.empty = this.users.size() == 0;
    }
}
