package de.hhu.propra14.team101.Networking;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class to do networking on the server side
 */

public class NetworkServer {

    public static String uuidRegex = ".{8}-.{4}-.{4}-.{4}-.{12}";

    private Map<UUID, NetworkUser> userMap;
    //private Map<String, NetworkRoom> roomMap;

    public NetworkServer() {
        this.userMap = new HashMap<>();
        //this.roomMap = new HashMap<>();
    }

    private int counter = 0;

    public String interpret(String line, PrintWriter networkOutput) {
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

        // TODO sanitizing: room names must not have a "," user names must not have a " "
        try {
            System.out.println(line);
            if (line.startsWith("hello ")) {
                String name = line.substring(6);
                UUID uuid = UUID.randomUUID();
                this.userMap.put(uuid, new NetworkUser(name, networkOutput));
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
                    /*} else if (command.equals("list_rooms")) {
                        if (this.roomMap.size() > 0) {
                            answer = "";
                            for (NetworkRoom aRoom : this.roomMap.values()) {
                                answer += aRoom.name + ",";
                            }
                        } else {
                            answer = "none";
                        }*/
                    /*} else if (command.matches("create_room .+")) {
                        String roomName = command.substring(command.indexOf(" ")+1);
                        if (this.roomMap.containsKey(roomName)) {
                            answer = "exists";
                        } else {
                            this.roomMap.put(roomName, new NetworkRoom(roomName));
                            currentUser.joinRoom(this.roomMap.get(roomName));
                            answer = "okay";
                        }
                    } else if (command.matches("join_room .+")) {
                        String roomName = command.substring(command.indexOf(" ")+1);
                        if (this.roomMap.containsKey(roomName)) {
                            currentUser.joinRoom(this.roomMap.get(roomName));
                            answer = "okay";
                        } else {
                            answer = "does_not_exist";
                        }
                    } else if (command.equals("leave_room")) {
                        NetworkRoom room = currentUser.getCurrentRoom();
                        currentUser.leaveRoom();
                        if (room.empty) {
                            this.roomMap.remove(room.name);
                        }
                        answer = "okay";*/
                    } else if (command.matches("chat .+")) {
                        String message = command.substring(5);
                        for (NetworkUser user : userMap.values()) {
                             user.send("chat " + currentUser.name + " " + message);
                        }
                        answer = "okay";
                    } else if (command.equals("ready")) {
                        currentUser.gameReady = !currentUser.gameReady;
                        answer = "okay";
                    } else if (command.equals("start_game")) {
                        if (currentUser == currentUser.getCurrentRoom().users.get(0)) {
                            boolean ready = true;
                            for (NetworkUser user : currentUser.getCurrentRoom().users) {
                                if (!user.gameReady) {
                                    ready = false;
                                }
                            }

                            if (ready) {
                                NetworkGame game = new NetworkGame(currentUser.getCurrentRoom());
                                for (NetworkUser user : currentUser.getCurrentRoom().users) {
                                    user.game = game;
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
       user.game.doAction(user, command);
       return "okay";
    }

    private boolean checkAlive() {
        return true;
    }

}
