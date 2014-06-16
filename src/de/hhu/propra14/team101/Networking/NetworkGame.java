package de.hhu.propra14.team101.Networking;

import de.hhu.propra14.team101.Game;
import de.hhu.propra14.team101.Player;
import de.hhu.propra14.team101.Weapons.*;
import de.hhu.propra14.team101.Worm;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class NetworkGame {
    public NetworkRoom room;
    public Game game;

    public NetworkGame(NetworkRoom room) {
        this.room = room;
        ArrayList<Player> players = new ArrayList<>();
        for (NetworkUser user: room.users) {
            if (!user.team.equals("spectator")) {
                ArrayList<Worm> worms = new ArrayList<>();
                ArrayList<AbstractWeapon> weapons = new ArrayList<>();
                for (Map.Entry<String, Boolean> entry: this.room.selectedWeapons.entrySet()) {
                    if (entry.getValue()) {
                        if (entry.getKey().equals("bazooka")) {
                            weapons.add(new Bazooka());
                        } else if (entry.getKey().equals("grenade")) {
                            weapons.add(new Grenade());
                        } else if (entry.getKey().equals("atomicbomb")) {
                            weapons.add(new AtomicBomb());
                        } else {
                            System.out.println("Unknown weapon: "+entry.getKey());
                        }
                    }
                }
                // Add three worms for each team with the created weapons
                Collections.addAll(worms, new Worm(weapons), new Worm(weapons), new Worm(weapons));
                Player tmpPlayer = new Player(worms, user.name);

                // Assign a color
                switch (user.team) {
                    case "red":
                        tmpPlayer.color = Color.RED;
                        break;
                    case "blue":
                        tmpPlayer.color = Color.BLUE;
                        break;
                    case "green":
                        tmpPlayer.color = Color.GREEN;
                        break;
                    case "yellow":
                        tmpPlayer.color = Color.YELLOW;
                        break;
                    default:
                        tmpPlayer.color = Color.BROWN;
                        break;
                }
                players.add(tmpPlayer);
            }
        }
        this.game = new Game(players);
        this.game.loadLevel(room.selectedMap);
        // TODO check who's got the lowest fps and use that, instead of 16
        this.game.fps = 16;
        this.game.startGameplay();
    }

    public void doAction(NetworkUser user, String line) {
        System.out.println("Turn of:"+this.game.turnOfPlayer+",sender:"+this.room.users.indexOf(user));
        if (this.game.turnOfPlayer == this.room.users.indexOf(user)) {
            Worm currentWorm = this.game.getPlayers().get(this.game.turnOfPlayer).wormList.get(this.game.getPlayers().get(this.game.turnOfPlayer).currentWorm);
            if (line.equals("move_left")) {
                currentWorm.move('l',game.getCurrentTerrain(),game.getPlayers());
            } else if (line.equals("move_right")) {
                currentWorm.move('r', game.getCurrentTerrain(), game.getPlayers());
            } else if (line.equals("jump")) {
                //TODO: Complete jump
            } else if (line.equals("next_weapon")) {
                currentWorm.nextWeapon();
            } else if (line.equals("prev_weapon")) {
                currentWorm.prevWeapon();
            } else if (line.startsWith("fire")) {
                // Don't fire without a weapon
                if (currentWorm.weaponList.size() != 0) {
                    String[] coords = line.split(" ");
                    this.game.fireBullet(currentWorm.fireWeapon(Double.parseDouble(coords[1]), Double.parseDouble(coords[2])));
                }
            } else if (line.equals("pause") && user == this.room.owner) {
                this.game.paused = !this.game.paused;
            }
            this.game.doAction(line);
            this.room.propagate("game " + line);
        }
        // Just don't do anything otherwise
    }
}
