package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Networking.Exceptions.RoomFullException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An online room
 */
public class NetworkRoom {
    /** Name of the room */
    public String name;
    /** Arraylist of NetworkUsers that are in this room */
    public ArrayList<NetworkUser> users = new ArrayList<>();
    /** Owner of this room */
    public NetworkUser owner;
    /** Arraylist of available colors */
    public ArrayList<String> availableColors = new ArrayList<>();
    /** Whether this room is empty */
    public boolean empty = false;
    /** Whether this room is ready to play */
    public boolean roomReady = false;
    /** Name of the selected map */
    public String selectedMap;
    /** Used password */
    public String password = "";
    /** HashMap of selected weapons */
    public Map<String, Boolean> selectedWeapons = new HashMap<>();
    /** Maximum number of users in this room. 0 means infinite */
    public int maxUsers = 0;

    /**
     *
     * @param name
     * @param selectedMap
     */
    public NetworkRoom(String name, String selectedMap) {
        this.name = name;
        this.selectedMap = selectedMap;
        Collections.addAll(availableColors, "red", "blue", "green", "yellow");
    }

    /**
     *
     * @param user
     * @throws RoomFullException
     */
    public void addUser(NetworkUser user) throws RoomFullException {
        if (this.maxUsers != 0 && this.users.size() == this.maxUsers) {
            throw new RoomFullException();
        }
        this.users.add(user);
        this.updateOwner();
    }

    /**
     *
     * @param user
     */
    public void removeUser(NetworkUser user) {
        this.users.remove(user);
        this.updateEmpty();
        // Don't update the owner if the room is empty now
        if (!this.users.isEmpty()) {
            this.updateOwner();
        }
    }

    /**
     *
     * @param state
     */
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

    /**
     * Sets a weapon enabled/disabled for the game
     * @param weaponName lowercase name of the weapon
     * @param active boolean whether the weapon should be enabled
     */
    public void setWeapon(String weaponName, boolean active) {
        // Make sure weaponName is lowercase TODO this is a bit dumb …
        weaponName = weaponName.toLowerCase();

        this.selectedWeapons.remove(weaponName);
        this.selectedWeapons.put(weaponName, active);
    }

    /**
     * Propagates a line to every user in this room
     * @param line Line to propagate
     */
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
