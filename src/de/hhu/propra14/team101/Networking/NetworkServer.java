package de.hhu.propra14.team101.Networking;

import org.yaml.snakeyaml.Yaml;

import java.io.PrintWriter;
import java.util.*;

/**
 * Class to do networking on the server side
 */

public class NetworkServer {

    public static String uuidRegex = ".{8}-.{4}-.{4}-.{4}-.{12}";

    private Map<UUID, NetworkUser> userMap = new HashMap<>();
    private Map<String, NetworkRoom> roomMap = new HashMap<>();

    public NetworkServer() {
    }

    private int counter = 0;

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

        // TODO sanitizing: room names must not have a "," user names must not have a " "
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
                    NetworkUser currentUser = userMap.get(uuid);
                    String command = line.substring(37);
                    if (command.equals("ping")) {
                        answer = "pong";
                    } else if (command.equals("pong")) {
                        currentUser.lastPong = System.currentTimeMillis();
                        answer = "okay";
                    } else if (command.equals("logoff")) {
                        userMap.remove(uuid);
                        answer = "okay";
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
                    } else if (command.equals("list_users")) {
                        answer = "";
                        for (NetworkUser user : this.userMap.values()) {
                            answer += user.name + ",";
                        }
                    } else if (command.equals("list_room_users")) {
                        answer = "";
                        if (currentUser.getCurrentRoom() != null) {
                            for (NetworkUser user : currentUser.getCurrentRoom().users) {
                                answer += user.name + "|"+user.team+",";
                            }
                        } else {
                            answer = "error client no_room";
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
                    } else if (command.matches("create_room .+")) {
                        String roomName = command.substring(command.indexOf(" ")+1);
                        if (this.roomMap.containsKey(roomName)) {
                            answer = "exists";
                        } else {
                            this.roomMap.put(
                                    roomName,
                                    new NetworkRoom(roomName, "Map1") // Map1 is a placeholder - for the case the initial change_map got lost
                            );
                            currentUser.joinRoom(this.roomMap.get(roomName));
                            answer = "okay";
                        }
                    } else if (command.matches("join_room .+")) {
                        String roomName = command.substring(command.indexOf(" ")+1);
                        if (this.roomMap.containsKey(roomName)) {
                            currentUser.joinRoom(this.roomMap.get(roomName));
                            // One user who isn't ready just joined
                            currentUser.getCurrentRoom().setRoomReady(false);
                            answer = "okay";
                        } else {
                            answer = "does_not_exist";
                        }
                    } else if (command.equals("leave_room")) {
                        NetworkRoom room = currentUser.getCurrentRoom();
                        currentUser.leaveRoom();
                        if (room.empty) {
                            this.roomMap.remove(room.name);
                        } else {
                            room.users.get(0).send("youre_owner");
                        }
                        answer = "okay";
                    } else if (command.matches("change_team .+")) {
                        String team = command.substring(12);
                        if (
                                team.equals("spectator") ||
                                currentUser.getCurrentRoom().availableColors.isEmpty() ||
                                !currentUser.getCurrentRoom().availableColors.contains(team)
                        ) {
                            currentUser.team = "spectator";
                        } else {
                            currentUser.team = team;
                        }
                        // Propagate
                        currentUser.getCurrentRoom().propagate("change_team " + currentUser.name + " " + team);
                        answer = "okay";
                    } else if (command.matches("change_map .+")) {
                        if (currentUser == currentUser.getCurrentRoom().owner) {
                            currentUser.getCurrentRoom().selectedMap = command.split(" ")[1];
                        }
                        answer = "okay";
                    } else if (command.matches("kick_user .+")) {
                        if (currentUser == currentUser.getCurrentRoom().owner) {
                            // Search the user
                            NetworkUser toKick = null;
                            for (NetworkUser anUser : currentUser.getCurrentRoom().users) {
                                if (anUser.name.equals(command.substring(10))) {
                                    toKick = anUser;
                                }
                            }
                            if (toKick == null) {
                                answer = "error client no_user";
                            } else {
                                toKick.send("youre_kicked");
                                toKick.leaveRoom();
                                answer = "okay";
                            }
                        } else {
                            answer = "error client not_owner";
                        }
                    } else if (command.equals("get_owner")) {
                        if (currentUser.getCurrentRoom() != null) {
                            answer = currentUser.getCurrentRoom().users.get(0).name;
                        } else {
                            answer = "error client no_room";
                        }
                    } else if (command.matches("chat .+")) {
                        String message = command.substring(5);
                        for (NetworkUser user : userMap.values()) {
                             user.send("chat " + currentUser.name + " " + message);
                        }
                        answer = "okay";
                    } else if (command.equals("ready")) {
                        currentUser.gameReady = !currentUser.gameReady;

                        boolean everyoneReady = true;
                        for (NetworkUser user : currentUser.getCurrentRoom().users) {
                            if (!user.gameReady) {
                                everyoneReady = false;
                            }
                        }

                        // Set the room ready or not ready
                        if (everyoneReady || currentUser.getCurrentRoom().roomReady) {
                            currentUser.getCurrentRoom().setRoomReady(everyoneReady);
                        }
                        answer = "okay";
                    } else if (command.equals("start_game")) {
                        if (currentUser == currentUser.getCurrentRoom().users.get(0)) {
                            if (currentUser.getCurrentRoom().roomReady) {
                                NetworkGame game = new NetworkGame(currentUser.getCurrentRoom());
                                for (NetworkUser user : currentUser.getCurrentRoom().users) {
                                    user.game = game;
                                    user.send("game started");
                                }
                                answer = "okay";
                            } else {
                                answer = "not_ready";
                            }
                        } else {
                            answer = "error client not_owner";
                        }
                    } else if (command.matches("game .+")) {
                        if (currentUser.game != null) {
                            answer = this.interpretGame(currentUser, command.substring(5));
                        } else {
                            answer = "error client no_game";
                        }
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

    private String interpretGame(NetworkUser user, String command) {
        if (command.equals("sync")) {
            Yaml yaml = new Yaml();
            // Send everything over the wire
            // Problem: everything right now is line-based - solution:
            // Replace every newline with an unused char (';') for sending and on receiving undo the replacement
            return "game sync " + yaml.dump(user.game.game.serialize()).replace('\n', ';');
        } else {
            user.game.doAction(user, command);
            return "okay";
        }
    }

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
        }
    }

}
