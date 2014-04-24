package de.hhu.propra14.team101;

public class NetworkClient {

    private int port;
    private String server;
    private int user_id = 0;
    private int connection; // TODO this should be the socket of our connection

    /**
     * @param port Port to listen on
     * @param server Server to connect to
     * Constructs a class for networking
     */
    public NetworkClient (int port, String server) {
        this.port = port;
        this.server = server;
        this.user_id = this.sign_in();
    }

    /**
     * @param data Data to send to the server
     * @return Answer received from the server
     * This method should only be used internally
     */
    private String send (String data) {
        if (this.user_id != 0) {
            // We're signed in, send our user id, too
            return "...";
        } else {
            // We're not signed in yet
            return "...";
        }
    }

    /**
     * Callback for socket to handle incoming data
     */
    private void handle_incoming_data() {
        //
    }

    /**
     * @return Returns an unique user id
     */
    private int sign_in () {
        String id;

        id = this.send("sign me in");
        return Integer.parseInt(id);
    }

    /**
     * @param name How to name the room
     * @return Returns the ID of the room we've just created
     */
    public int create_room (String name) {
        String answer;

        answer = this.send("create room " + name);
        return Integer.parseInt(answer);
    }
}
