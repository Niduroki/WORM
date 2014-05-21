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
        MapSaves mapSaver = new MapSaves();
        InputStream input = new FileInputStream(new File(path));
        Map<String, Object> data = (Map<String, Object>) this.yaml.load(input);
        Game game = new Game();
        game.setCurrentTerrain(mapSaver.deserialize((ArrayList<ArrayList<Map>>) data.get("terrain")));
        game.setPlayers(this.deserializePlayerArray((ArrayList<Map>) data.get("players")));
        game.round = (Integer) data.get("round");
        game.turnOfPlayer = (Integer) data.get("turn_of_player");
        return game;
    }

    private Object[] serializePlayerArray(ArrayList<Player> playerArray) {
        Object[] result = new Object[playerArray.size()];
        for (int i=0; i<playerArray.size(); i++) {
            result[i] = this.serializePlayer(playerArray.get(i));
        }
        return result;
    }

    private ArrayList<Player> deserializePlayerArray(ArrayList<Map> input) {
        ArrayList<Player> result = new ArrayList<Player>();
        for (int i=0; i<input.size(); i++) {
            result.add(deserializePlayer(input.get(i)));
        }
        return result;
    }

    private Map serializePlayer(Player player) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", player.type);
        map.put("current_worm", player.currentWorm);
        ArrayList<Map> wormArray = new ArrayList<Map>();
        for (int i=0; i<player.wormList.size(); i++) {
            wormArray.add(player.wormList.get(i).serialize());
        }
        map.put("worm_array", wormArray);
        return map;
    }

    private Player deserializePlayer(Map input) {
        ArrayList<Map> rawWorms = new ArrayList<Map>();
        rawWorms = (ArrayList<Map>) input.get("worm_array");
        ArrayList<Worm> wormList = new ArrayList<>();
        for (int i=0; i<rawWorms.size(); i++) {
            wormList.add(Worm.deserialize(rawWorms.get(i)));
        }
        Player player = new Player(wormList, (String)input.get("type"));
        player.currentWorm = (Integer) input.get("current_worm");
        return player;
    }
}
