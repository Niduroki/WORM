package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Game;
import de.hhu.propra14.team101.Player;
import de.hhu.propra14.team101.Weapons.AbstractWeapon;
import de.hhu.propra14.team101.Weapons.Bazooka;
import de.hhu.propra14.team101.Worm;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;

public class NetworkGame {
    public NetworkRoom room;
    public Game game;

    public NetworkGame(NetworkRoom room) {
        this.room = room;
        // TODO construct an ArrayList<Player> from room here
        ArrayList<Player> players = new ArrayList<>();
        for (NetworkUser user: room.users) {
            ArrayList<Worm> worms = new ArrayList<>();
            ArrayList<AbstractWeapon> weapons = new ArrayList<>();
            weapons.add(new Bazooka());
            Collections.addAll(worms, new Worm(weapons, true), new Worm(weapons, true));
            Player tmpPlayer = new Player(worms, user.name);
            tmpPlayer.color = Color.RED;
            players.add(tmpPlayer);
        }
        this.game = new Game(players);
        this.game.loadLevel(room.selectedMap);
        this.game.startLevel();
    }

    public void doAction(NetworkUser user, String line) {
        if (this.game.turnOfPlayer == this.room.users.indexOf(user)) {
            Worm currentWorm = game.getPlayers().get(game.turnOfPlayer).wormList.get(game.getPlayers().get(game.turnOfPlayer).currentWorm);
            if (line.equals("move_left")) {
                currentWorm.move('l');
            } else if (line.equals("move_right")) {
                currentWorm.move('r');
            } else if (line.equals("next_weapon")) {
                currentWorm.nextWeapon();
            } else if (line.equals("prev_weapon")) {
                currentWorm.prevWeapon();
            } else if (line.startsWith("fire")) {
                // Don't fire without a weapon
                if (currentWorm.weaponList.size() != 0) {
                    String[] coords = line.split(" ");
                    game.fireBullet(currentWorm.fireWeapon(Double.parseDouble(coords[1]), Double.parseDouble(coords[2])));
                }
            }
            for (NetworkUser anUser: this.room.users) {
                // Propagate the action
                anUser.send("game " + line);
            }
        }
        // Just don't do anything otherwise
    }
}
