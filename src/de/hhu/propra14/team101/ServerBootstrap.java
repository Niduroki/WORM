package de.hhu.propra14.team101;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Start the server in here
 */
public class ServerBootstrap {

    public static void main(String[] args) {
        try {
            start();
        } catch (IOException e) {
            System.out.println("Couldn't start server!");
        }
    }

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

    static class HandleConnectionThread implements Runnable {

        Socket client;
        NetworkServer networkServer;

        public HandleConnectionThread (Socket client, NetworkServer networkServer) {
            this.client = client;
            this.networkServer = networkServer;
        }

        @Override
        public void run() {
            try {

                Scanner input = new Scanner(client.getInputStream());
                PrintWriter output = new PrintWriter(client.getOutputStream());

                while (true) {
                    String line = input.nextLine();
                    String answer = networkServer.interpret(line);

                    output.println(answer);
                    output.flush();
                }
            } catch (IOException e) {
                System.out.println("Error while communicating with client");
                e.printStackTrace();
            } catch (NoSuchElementException e) {
                System.out.println("Client seemed to quit");
            }
        }
    }
}
