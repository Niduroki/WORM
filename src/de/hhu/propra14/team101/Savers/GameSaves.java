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
            FileOutputStream file = new FileOutputStream(path);
            file.write(GZipper.gzip(writer.toString()));
            file.close();
        } catch (IOException e) {
            //
        }
    }

    /**
     * @param path Path to save file
     */
    public Game load(String path, boolean headless) throws FileNotFoundException {
        Map<String, Object> data = (Map<String, Object>) this.yaml.load(GZipper.gunzip(path));
        Game game = Game.deserialize(data, headless);
        return game;
    }
}
