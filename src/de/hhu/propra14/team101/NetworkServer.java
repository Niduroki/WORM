package de.hhu.propra14.team101;

import java.util.ArrayList;

/**
 * Class to do networking on the server side
 */

public class NetworkServer {

    private int nextUserId = 0;
    private ArrayList<NetworkUser> userList;

    public NetworkServer() {
        this.userList = new ArrayList<>();
    }

    public String interpret(String line) {
        if (line.equals("ping")) {
            return "pong";
        } else if (line.startsWith("hello ")) {
            String name = line.substring(6);
            int uid = nextUserId;
            this.userList.add(new NetworkUser(uid, name));
            this.nextUserId += 1;
            return String.valueOf(uid);
        }
        return "error";
    }


    private class NetworkUser {

        public int uid;
        public String name;

        public NetworkUser(int uid, String name) {
            this.uid = uid;
            this.name = name;
        }
    }
}
