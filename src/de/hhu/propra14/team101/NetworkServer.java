package de.hhu.propra14.team101;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class to do networking on the server side
 */

public class NetworkServer {

    private int port;
    private ServerSocket server;

    public NetworkServer () throws IOException {
        this.port = 7601;
        this.server = new ServerSocket(this.port);
        //server.setSoTimeout(30000); // Timeout after 30s
        while (true) {
            try {
                Socket client = server.accept();
                new Thread(new HandleConnectionThread(client)).start();
            } catch (InterruptedIOException e) {
                System.out.println("Timeout on a client!");
            }
        }
    }

    static class HandleConnectionThread implements Runnable {

        Socket client;

        public HandleConnectionThread (Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                Scanner input = new Scanner(client.getInputStream());
                PrintWriter output = new PrintWriter(client.getOutputStream());

                while (true) {
                    String line = input.nextLine();
                    String answer = interpret(line);

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

    private static String interpret(String line) {
        if (line.equals("ping")) {
            return "pong";
        }
        return "error";
    }
}
