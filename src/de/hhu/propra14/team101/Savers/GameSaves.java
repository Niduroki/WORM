package de.hhu.propra14.team101.Savers;

import de.hhu.propra14.team101.Game;

import java.io.*;
import java.util.Map;


/**
 * Class to load and save games
 */

public class GameSaves extends AbstractSaver {

    /**
     * @param path Path to save to
     */
    public void save(Game game, String path) {
        StringWriter writer = new StringWriter();
        yaml.dump(game.serialize(), writer);
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
        Game game = Game.deserialize(data);
        return game;
    }
}
