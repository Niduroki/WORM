package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Networking.Exceptions.RoomFullException;
import org.yaml.snakeyaml.Yaml;

import java.io.PrintWriter;
import java.util.*;

/**
 * Class to do networking on the server side
 *
 * <pre>
 * {@code
 * NetworkServer server = new NetworkServer();
 * server.interpret("hello username", client.output);
 * server.cleanUp(client.uuid);
 * }
 * </pre>
 */
public class NetworkServer {
    /** Regex for matching an UUID */
    public static String uuidRegex = ".{8}-.{4}-.{4}-.{4}-.{12}";

    /** Map saving the users and UUIDs */
    private Map<UUID, NetworkUser> userMap = new HashMap<>();
    /** Map saving roomnames and rooms */
    private Map<String, NetworkRoom> roomMap = new HashMap<>();
    /** Used to check every 30 interprets, whether everyone is still alive */
    private int counter = 0;
    
    /**
     * Cleans up, when an user hard-quits with by closing the application
     * @param uuid UUID of the user (took from the last line received from the user)
     */
    public void cleanUp(UUID uuid) {
        NetworkUser user = userMap.get(uuid);
        try {
            NetworkRoom oldRoom = user.currentRoom;

            user.leaveRoom();
            // If the room is empty delete it
            if (oldRoom.empty) {
                roomMap.remove(oldRoom.name);
            }

            // If the user was in a game, and the room he was in is empty: stop the game, too
            if (user.game != null && oldRoom.empty) {
                user.game.game.gameUpdateThread.interrupt();
            }
        } catch (NullPointerException e) {
            System.out.println("User was in no room");
        }

        // Remove the user himself
        userMap.remove(uuid);
    }
    /**
     * Interprets a line
     * @param line Line to interpret
     * @param networkOutput Where to write output to (used when creating a new user)
     * @return Resulting answer
     */
    public String interpret(String line, PrintWriter networkOutput) {
        String answer;

        // Every 30 interprets check if everybody's still there
        if (this.counter == 30) {
            this.checkAlive();
            this.counter = 0;
        } else {
            this.counter += 1;
        }

        int requestCount = -1;
        if (line.matches("[0-9]+ .+")) {
            requestCount = Integer.parseInt(line.split(" ")[0]);
            line = line.substring(line.indexOf(" ")+1);
            System.out.println(requestCount);
        }

        try {
            System.out.println(line);
            if (line.startsWith("hello ")) {
                String name = line.substring(6);
                UUID uuid = UUID.randomUUID();
                this.userMap.put(uuid, new NetworkUser(name, uuid, networkOutput));
                answer = String.valueOf(uuid);
            } else if (line.matches(NetworkServer.uuidRegex + " .+")) {
                UUID uuid = UUID.fromString(line.split(" ")[0]);
                if (userMap.containsKey(uuid)) {
                    answer = "okay"; // default answer if nothing went wrong
                    NetworkUser currentUser = userMap.get(uuid);
                    String command = line.substring(37);
                    if (command.matches("change_map .+") && currentUser == currentUser.currentRoom.owner) {
                        currentUser.currentRoom.selectedMap = command.split(" ")[1];
                    } else if (command.matches("change_max_players .+") && currentUser == currentUser.currentRoom.owner) {
                        currentUser.currentRoom.maxUsers = Integer.parseInt(command.split(" ")[1]);
                    } else if (command.matches("change_room_name .+") && currentUser == currentUser.currentRoom.owner) {
                        String newName = command.substring(17);
                        String oldName = currentUser.currentRoom.name;
                        this.roomMap.remove(oldName);
                        this.roomMap.put(newName, currentUser.currentRoom);
                        currentUser.currentRoom.name = newName;
                    } else if (command.matches("change_team .+")) {
                        String team = command.substring(12);
                        if (
                                team.equals("spectator") ||
                                        currentUser.currentRoom.availableColors.isEmpty() ||
                                        !currentUser.currentRoom.availableColors.contains(team)
                                ) {
                            currentUser.team = "spectator";
                        } else {
                            currentUser.team = team;
                        }
                        // Propagate
                        currentUser.currentRoom.propagate("change_team " + currentUser.name + " " + team);
                    } else if (command.matches("change_weapon .+ .+") && currentUser == currentUser.currentRoom.owner) {
                        currentUser.currentRoom.setWeapon(command.split(" ")[1], Boolean.parseBoolean(command.split(" ")[2]));
                    } else if (command.matches("create_room .+")) {
                        String roomName = command.substring(command.indexOf(" ")+1);
                        if (this.roomMap.containsKey(roomName)) {
                            answer = "exists";
                        } else {
                            this.roomMap.put(roomName,
                                new NetworkRoom(roomName, "Map1") // Map1 is a placeholder
                            );
                            currentUser.joinRoom(this.roomMap.get(roomName));
                        }
                    } else if (command.matches("game .+")) {
                        if (currentUser.game != null) {
                            answer = this.interpretGame(currentUser, command.substring(5));
                        } else {
                            answer = "error client no_game";
                        }
                    } else if (command.equals("get_owner")) {
                        if (currentUser.currentRoom != null) {
                            answer = currentUser.currentRoom.users.get(0).name;
                        } else {
                            answer = "error client no_room";
                        }
                    } else if (command.equals("get_room_properties")) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("map", currentUser.currentRoom.selectedMap);
                        data.put("name", currentUser.currentRoom.name);
                        data.put("password", currentUser.currentRoom.password);
                        data.put("max_players", currentUser.currentRoom.maxUsers);
                        data.put("weapons", currentUser.currentRoom.selectedWeapons);
                        Yaml yaml = new Yaml();
                        answer = yaml.dump(data).replace('\n', ';');
                    } else if (command.matches("chat .+")) {
                        String message = command.substring(5);
                        for (NetworkUser user : userMap.values()) {
                            user.send("chat " + currentUser.name + " " + message);
                        }
                    } else if (command.matches("join_room .+")) {
                        String roomName = command.substring(command.indexOf(" ") + 1);
                        if (this.roomMap.containsKey(roomName)) {
                            try {
                                currentUser.joinRoom(this.roomMap.get(roomName));
                                // One user who isn't ready just joined
                                currentUser.currentRoom.setRoomReady(false);
                            } catch (RoomFullException e) {
                                answer = "room_full";
                            }
                        } else {
                            answer = "does_not_exist";
                        }
                    } else if (command.matches("kick_user .+")) {
                        if (currentUser == currentUser.currentRoom.owner) {
                            // Search the user
                            NetworkUser toKick = null;
                            for (NetworkUser anUser : currentUser.currentRoom.users) {
                                if (anUser.name.equals(command.substring(10))) {
                                    toKick = anUser;
                                }
                            }
                            if (toKick == null) {
                                answer = "error client no_user";
                            } else {
                                toKick.send("youre_kicked");
                                toKick.leaveRoom();
                            }
                        } else {
                            answer = "error client not_owner";
                        }
                    } else if (command.equals("leave_room")) {
                        NetworkRoom room = currentUser.currentRoom;
                        currentUser.leaveRoom();
                        if (room.empty) {
                            this.roomMap.remove(room.name);
                        } else {
                            room.users.get(0).send("youre_owner");
                        }
                    } else if (command.equals("list_rooms")) {
                        if (this.roomMap.size() > 0) {
                            answer = "rooms ";
                            for (NetworkRoom aRoom : this.roomMap.values()) {
                                answer += aRoom.name + ",";
                            }
                        } else {
                            answer = "none";
                        }
                    } else if (command.equals("list_room_users")) {
                        answer = "";
                        if (currentUser.currentRoom != null) {
                            for (NetworkUser user : currentUser.currentRoom.users) {
                                answer += user.name + "|"+user.team+",";
                            }
                        } else {
                            answer = "error client no_room";
                        }
                    } else if (command.equals("list_users")) {
                        answer = "";
                        for (NetworkUser user : this.userMap.values()) {
                            answer += user.name + ",";
                        }
                    } else if (command.equals("logoff")) {
                        userMap.remove(uuid);
                    } else if (command.equals("ping")) {
                        answer = "pong";
                    } else if (command.equals("pong")) {
                        currentUser.lastPong = System.currentTimeMillis();
                    } else if (command.equals("ready")) {
                        currentUser.gameReady = !currentUser.gameReady;

                        boolean everyoneReady = true;
                        for (NetworkUser user : currentUser.currentRoom.users) {
                            if (!user.gameReady) {
                                everyoneReady = false;
                            }
                        }

                        // Set the room ready or not ready
                        if (everyoneReady || currentUser.currentRoom.roomReady) {
                            currentUser.currentRoom.setRoomReady(everyoneReady);
                        }
                    } else if (command.equals("start_game")) {
                        if (currentUser == currentUser.currentRoom.users.get(0)) {
                            if (currentUser.currentRoom.roomReady) {
                                NetworkGame game = new NetworkGame(currentUser.currentRoom);
                                for (NetworkUser user : currentUser.currentRoom.users) {
                                    user.game = game;
                                    user.send("game started");
                                }
                            } else {
                                answer = "not_ready";
                            }
                        } else {
                            answer = "error client not_owner";
                        }
                    } else if (command.equals("status")) {
                        answer = "name:" + currentUser.name + ",";
                        /*if (currentUser.getCurrentRoom() != null) {
                            answer += "room:" + currentUser.getCurrentRoom().name + ",";

                            answer += "current_room_users:";
                            for (NetworkUser user : currentUser.getCurrentRoom().users) {
                                answer += user.name + ",";
                            }
                        } else {
                            answer += "room:none,";
                        }*/
                    } else {
                        answer = "error client command";
                    }
                } else {
                    answer = "unknown";
                }
            } else {
                answer = "error client syntax";
            }
        } catch (Exception e) {
            e.printStackTrace();
            answer = "error server";
        }

        if (requestCount != -1) {
            return requestCount + " " + answer;
        } else {
            return answer;
        }
    }

    /**
     * Sends every user a ping and removes these players that didn't answer them
     */
    private void checkAlive() {
        ArrayList<UUID> toRemove = new ArrayList<>();
        for (NetworkUser user: userMap.values()) {
            // If the last pong is more than 90s ago remove the user
            if (System.currentTimeMillis() - user.lastPong > 90000) {
                toRemove.add(user.uuid);
            } else {
                user.send("ping");
            }
        }

        // This is needed to avoid ConcurrentModificationException
        for (UUID uuid: toRemove) {
            userMap.remove(uuid);
            this.cleanUp(uuid);
        }
    }



    /**
     * Subfunction of interpret to deal with game interpretation
     * @param user User that send the command
     * @param command Command that has been send
     * @return Resulting answer
     */
    private String interpretGame(NetworkUser user, String command) {
        if (command.equals("sync")) {
            Yaml yaml = new Yaml();
            // Send everything over the wire
            // Problem: yaml depends on newlines, our protocol is line-based - solution:
            // Replace every newline with an unused char (';') for sending and on receiving reverse the replacement
            return "game sync " + yaml.dump(user.game.game.serialize()).replace('\n', ';');
        } else {
            user.game.doAction(user, command);
            return "okay";
        }
    }


}
