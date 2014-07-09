package de.hhu.propra14.team101.Savers;

import de.hhu.propra14.team101.Level;

import java.io.*;
import java.util.Map;

/**
 * Class to load and save levels
 *
 * <pre>
 * {@code
 * LevelSaves levelSaver = new LevelSaves();
 * Level loaded = levelSaver.load("path/to/level1.gz");
 * // Do something with the level here
 * levelSaver.save(loaded, "path/to/new_level1.gz");
 * }
 * </pre>
 */
public class LevelSaves extends AbstractSaver {

    /**
     * Loads a map from a yaml file
     * @param path Path to load from
     * @return loaded Level
     * @throws java.io.FileNotFoundException If file not found
     */
    public Level load (String path) throws FileNotFoundException {
        String data;
        try {
            // If we're not in a jar the maps are under resources
            data = GZipper.gunzip("resources/" + path);
        } catch (FileNotFoundException e) {
            if (path.matches("maps/maps/.*\\.gz\\.gz")) {
                // Local map (map editor) – they already have a prepended "maps/" and a appended ".gz"
                // We need to get rid of these
                path = path.substring(5);
                path = path.replaceAll("\\.gz", "");
                data = GZipper.gunzip(path+".gz");
            } else {
                // Last stand: read from a jar
                data = GZipper.gunzip(this.getClass().getResourceAsStream("/" + path));
            }
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> raw = (Map<String, Object>) this.yaml.load(data);
        return Level.deserialize(raw);
    }

    /**
     * Saves Game
     * @param level level to save
     * @param path path to save to
     */
    public void save (Level level, String path) {
        StringWriter writer = new StringWriter();
        yaml.dump(level.serialize(), writer);
        try {
            FileOutputStream file = new FileOutputStream(path);
            file.write(GZipper.gzip(writer.toString()));
            file.close();
        } catch (IOException e) {
            //
        }
    }
}
