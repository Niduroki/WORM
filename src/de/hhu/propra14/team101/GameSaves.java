package de.hhu.propra14.team101;

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
        MapSaves mapSaver = new MapSaves();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("terrain", mapSaver.serialize(game.getCurrentTerrain()));
        data.put("players", game.getPlayers());
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
        MapSaves mapSaver = new MapSaves();
        InputStream input = new FileInputStream(new File(path));
        Map<String, Object> data = (Map<String, Object>) this.yaml.load(input);
        Game game = new Game();
        game.setCurrentTerrain(mapSaver.deserialize((ArrayList<ArrayList<Map>>) data.get("terrain")));
        game.setPlayers((ArrayList<Player>) data.get("players"));
        game.round = (Integer) data.get("round");
        game.turnOfPlayer = (Integer) data.get("turn_of_player");
        return game;
    }
}
