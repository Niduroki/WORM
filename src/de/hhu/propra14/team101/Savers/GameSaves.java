package de.hhu.propra14.team101.Savers;

import de.hhu.propra14.team101.Game;
import de.hhu.propra14.team101.Player;
import de.hhu.propra14.team101.Savers.AbstractSaver;
import de.hhu.propra14.team101.Terrain;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Class to load and save games
 */

public class GameSaves extends AbstractSaver {

    /**
     * @param path Path to save to
     */
    public void save(Game game, String path) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("terrain", game.getCurrentTerrain().serialize());
        data.put("players", this.serializePlayerArray(game.getPlayers()));
        data.put("round", game.round);
        data.put("turn_of_player", game.turnOfPlayer);
        StringWriter writer = new StringWriter();
        yaml.dump(data, writer);
        try {
            FileWriter file = new FileWriter(path);
            file.write(writer.toString());
            file.close();
        } catch (java.io.IOException e) {
            //
        }
    }

    /**
     * @param path Path to save file
     */
    public Game load(String path) throws FileNotFoundException {
        InputStream input = new FileInputStream(new File(path));
        Map<String, Object> data = (Map<String, Object>) this.yaml.load(input);
        Game game = new Game();
        game.setCurrentTerrain(Terrain.deserialize((ArrayList<ArrayList<Map>>) data.get("terrain")));
        game.setPlayers(this.deserializePlayerArray((ArrayList<Map>) data.get("players")));
        game.round = (Integer) data.get("round");
        game.turnOfPlayer = (Integer) data.get("turn_of_player");
        return game;
    }

    private Object[] serializePlayerArray(ArrayList<Player> playerArray) {
        Object[] result = new Object[playerArray.size()];
        for (int i=0; i<playerArray.size(); i++) {
            result[i] =  playerArray.get(i).serialize();
        }
        return result;
    }

    private ArrayList<Player> deserializePlayerArray(ArrayList<Map> input) {
        ArrayList<Player> result = new ArrayList<Player>();
        for (int i=0; i<input.size(); i++) {
            result.add(Player.deserialize(input.get(i)));
        }
        return result;
    }
}
