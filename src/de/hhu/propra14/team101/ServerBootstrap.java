package de.hhu.propra14.team101;

import java.io.IOException;

/**
 * Start the server in here
 */
public class ServerBootstrap {
    public static void main(String[] args) {
        try {
            NetworkServer server = new NetworkServer();
        } catch (IOException e) {
            System.out.println("Couldn't start server!");
        }
    }
}
