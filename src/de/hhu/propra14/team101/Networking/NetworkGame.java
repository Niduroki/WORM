package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Game;

public class NetworkGame {
    public NetworkRoom room;
    private Game game;

    public NetworkGame(NetworkRoom room) {
        this.room = room;
        // TODO construct an ArrayList<Player> from room here
        //this.game = new Game();
    }

    public void doAction(NetworkUser user, String line) {
        //
    }
}
