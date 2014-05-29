package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Game;
import de.hhu.propra14.team101.Player;
import de.hhu.propra14.team101.Weapons.AbstractWeapon;
import de.hhu.propra14.team101.Weapons.Bazooka;
import de.hhu.propra14.team101.Worm;

import java.util.ArrayList;
import java.util.Collections;

public class NetworkGame {
    public NetworkRoom room;
    private Game game;

    public NetworkGame(NetworkRoom room) {
        this.room = room;
        // TODO construct an ArrayList<Player> from room here
        ArrayList<Player> players = new ArrayList<>();
        for (NetworkUser user: room.users) {
            ArrayList<Worm> worms = new ArrayList<>();
            ArrayList<AbstractWeapon> weapons = new ArrayList<>();
            weapons.add(new Bazooka());
            Collections.addAll(worms, new Worm(weapons), new Worm(weapons));
            players.add(new Player(worms, user.name));
        }
        this.game = new Game(players);
    }

    public void doAction(NetworkUser user, String line) {
        if (this.game.turnOfPlayer == this.room.users.indexOf(user)) {
            if (line.equals("move_left")) {
                int currentWorm = this.game.getPlayers().get(game.turnOfPlayer).currentWorm;
                this.game.getPlayers().get(this.game.turnOfPlayer).wormList.get(currentWorm).move('l');
            } else if (line.equals("move_right")) {

            }

            for (NetworkUser anUser: this.room.users) {
                // Propagate the action
                anUser.send(line);
            }
        }
        // Just don't do anything otherwise
    }
}
