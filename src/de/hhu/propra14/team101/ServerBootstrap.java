package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Networking.NetworkServer;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Start the server in here
 */
public class ServerBootstrap {

    /**
     * Main method; entry point
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        // Don't load any graphics
        Main.headless = true;
        try {
            start();
        } catch (IOException e) {
            System.out.println("Couldn't start server!");
        }
    }

    /**
     * Start server.
     *
     * @throws IOException
     */
    public static void start() throws IOException {
        int port = 7601;
        ServerSocket server = new ServerSocket(port);
        //server.setSoTimeout(30000); // Timeout after 30s
        NetworkServer networkServer = new NetworkServer();
        while (true) {
            try {
                Socket client = server.accept();
                new Thread(new HandleConnectionThread(client, networkServer)).start();
            } catch (InterruptedIOException e) {
                System.out.println("Timeout on a client!");
            }
        }
    }

    /**
     * Handles incoming connections
     * <p></p>
     * Code example:
     * <pre>
     * {@code
     * try {
     *  Socket client = server.accept();
     *  new Thread(new HandleConnectionThread(client, networkServer)).start();
     * } catch (InterruptedIOException e) {
     *  System.out.println("Timeout on a client!");
     * }
     * }
     * </pre>
     */
    static class HandleConnectionThread implements Runnable {

        Socket client;
        NetworkServer networkServer;

        /**
         * Start handling.
         *
         * @param client        client socket
         * @param networkServer game server
         */
        public HandleConnectionThread(Socket client, NetworkServer networkServer) {
            this.client = client;
            this.networkServer = networkServer;
        }

        /**
         * Entry point of thread, which handled each client connection.
         */
        @Override
        public void run() {
            String line = "NIL";
            String answer;
            try {

                Scanner input = new Scanner(client.getInputStream());
                PrintWriter output = new PrintWriter(client.getOutputStream());

                while (true) {
                    line = input.nextLine();
                    answer = networkServer.interpret(line, output);

                    output.println(answer);
                    output.flush();
                }
            } catch (IOException e) {
                System.out.println("Error while communicating with client");
                e.printStackTrace();
            } catch (NoSuchElementException e) {
                // Search for an UUID in the last line, to clean up the associated user
                Pattern pattern = Pattern.compile("(" + NetworkServer.uuidRegex + ")");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String uuid = matcher.group(1);
                    System.out.println("Cleaning up after user " + uuid);
                    networkServer.cleanUp(UUID.fromString(uuid));
                }
            }
        }
    }
}
