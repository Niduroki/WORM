package de.hhu.propra14.team101.Networking;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class to do networking on the server side
 */

public class NetworkServer {

    public static String uuidRegex = ".{8}-.{4}-.{4}-.{4}-.{12}";

    private Map<UUID, NetworkUser> userMap;
    private Map<String, NetworkRoom> roomMap;

    public NetworkServer() {
        this.userMap = new HashMap<>();
        this.roomMap = new HashMap<>();
    }

    private int counter = 0;

    public String interpret(String line) {
        String answer;

        // Every 20 interprets check if everybody's still there
        if (this.counter == 20) {
            if (!this.checkAlive()) {
                //client.close();
            }
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
                this.userMap.put(uuid, new NetworkUser(name));
                answer = String.valueOf(uuid);
            } else if (line.matches(NetworkServer.uuidRegex + " .+")) {
                UUID uuid = UUID.fromString(line.split(" ")[0]);
                if (userMap.containsKey(uuid)) {
                    NetworkUser currentUser = userMap.get(uuid);
                    String command = line.substring(37);
                    if (command.equals("ping")) {
                        answer = "pong";
                    } else if (command.equals("logoff")) {
                        userMap.remove(uuid);
                        answer = "okay";
                    } else if (command.equals("list_rooms")) {
                        System.out.println(this.roomMap.size());
                        if (this.roomMap.size() > 0) {
                            answer = "";
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
                            this.roomMap.put(roomName, new NetworkRoom(roomName, currentUser));
                            currentUser.currentRoom = this.roomMap.get(roomName);
                            answer = "okay";
                        }
                    } else if (command.matches("join_room .+")) {
                        // Join a room
                        answer = "okay";
                    } else if (command.equals("leave_room")) {
                        // Leave the current room
                        answer = "okay";
                    } else if (command.matches("chat [gr] .+")) {
                        String type = command.split(" ")[1];
                        String message = command.substring(7);
                        switch (type) {
                            case "g":
                                // Chat globally
                                answer = "okay";
                                break;
                            case "r":
                                // Chat in our room
                                answer = "okay";
                                break;
                            default:
                                answer = "error client syntax";
                                break;
                        }
                    } else if (command.equals("start_game")) {
                        // Start a game here
                        answer = "okay";
                    } else if (command.matches("game .+")) {
                        answer = this.interpretGame(uuid, command.substring(5));
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

    private String interpretGame(UUID user, String command) {
        return "bla";
    }

    private boolean checkAlive() {
        return true;
    }

}
