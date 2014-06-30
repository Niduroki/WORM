package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Networking.Exceptions.RoomFullException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An online room
 *
 * <pre>
 * {@code
 * NetworkRoom room = new NetworkRoom("test", "Plains");
 * try {
 *     room.addUser(user2);
 * } catch (RoomFullException e) {
 *     System.out.println("Room is full!");
 * }
 * room.removeUser(user2);
 * room.setReady(true);
 * room.setWeapon("bazooka", false);
 * room.propagate("chat r Username Hey there!");
 * }
 * </pre>
 */
public class NetworkRoom {
    /** Name of the room */
    public String name;
    /** Array list of NetworkUsers that are in this room */
    public ArrayList<NetworkUser> users = new ArrayList<>();
    /** Owner of this room */
    public NetworkUser owner;
    /** Array list of available colors */
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
     * Creates a room for online games
     * @param name Name of the room
     * @param selectedMap Name of the selected map
     */
    public NetworkRoom(String name, String selectedMap) {
        this.name = name;
        this.selectedMap = selectedMap;
        Collections.addAll(availableColors, "red", "blue", "green", "yellow");
    }

    /**
     * Add an user to this room
     * @param user User to add
     * @throws RoomFullException if the room is full
     */
    public void addUser(NetworkUser user) throws RoomFullException {
        if (this.maxUsers != 0 && this.users.size() == this.maxUsers) {
            throw new RoomFullException();
        }
        this.users.add(user);
        this.updateOwner();
    }

    /**
     * Remove an user from this room
     * @param user User to remove
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
     * Set the room ready
     * @param state State of readiness
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

    /**
     * Updates the owner of the room
     */
    private void updateOwner() {
        this.owner = this.users.get(0);
    }

    /**
     * Updates the empty state of the room
     */
    private void updateEmpty() {
        this.empty = this.users.size() == 0;
    }
}
