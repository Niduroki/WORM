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

    public NetworkServer() {
        this.userMap = new HashMap<>();
    }

    private int counter = 0;

    public String interpret(String line) {
        // Every 20 interprets check if everybody's still there
        if (this.counter == 20) {
            this.checkAlive();
            this.counter = 0;
        } else {
            this.counter += 1;
        }

        try {
            if (line.startsWith("hello ")) {
                String name = line.substring(6);
                UUID uuid = UUID.randomUUID();
                this.userMap.put(uuid, new NetworkUser(name));
                return String.valueOf(uuid);
            } else if (line.matches(NetworkServer.uuidRegex + " .+")) {
                UUID uuid = UUID.fromString(line.split(" ")[0]);
                if (userMap.containsKey(uuid)) {
                    String command = line.substring(37);
                    if (command.equals("ping")) {
                        return "pong";
                    } else if (command.equals("logoff")) {
                        userMap.remove(uuid);
                        return "okay";
                    } else if (command.equals("list_rooms")) {
                        // List all rooms here
                    } else if (command.matches("create_room .+")) {
                        // TODO check if the room already exists, if it doesn't create the room
                        return "okay";
                    } else if (command.matches("join_room .+")) {
                        // Join a room
                    } else if (command.equals("leave_room")) {
                        // Leave the current room
                    } else if (command.matches("chat [gr] .+")) {
                        String type = command.split(" ")[1];
                        String message = command.substring(7);
                        if (type.equals("g")) {
                            // Chat globally
                        } else if (type.equals("r")) {
                            // Chat in our room
                        }
                    } else if (command.equals("start_game")) {
                        // Start a game here
                    } else if (command.matches("game .+")) {
                        this.interpretGame(uuid, command.substring(5));
                    }
                } else {
                    return "unknown";
                }
            }
            return "error client";
        } catch (Exception e) {
            e.printStackTrace();
            return "error server";
        }
    }

    private String interpretGame(UUID user, String command) {
        return "bla";
    }

    private void checkAlive() {
        //
    }

}
