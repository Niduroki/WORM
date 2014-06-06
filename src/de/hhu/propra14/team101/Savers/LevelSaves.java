package de.hhu.propra14.team101.Savers;

import de.hhu.propra14.team101.Level;

import java.io.*;
import java.util.Map;

/**
 * Class to load and save levels
 */

public class LevelSaves extends AbstractSaver {

    /**
     * @param path Path to load from
     * @return Level
     * @throws java.io.FileNotFoundException
     * Loads a map from a yaml file
     */
    public Level load (String path) throws FileNotFoundException {
        String data;
        try {
            // If we're not in a jar the maps are under resources
            data = GZipper.gunzip("resources/" + path);
        } catch (FileNotFoundException e) {
            // Read from a jar
            data = GZipper.gunzip("/"+path);
        }
        Map<String, Object> raw = (Map<String, Object>) this.yaml.load(data);
        return Level.deserialize(raw);
    }

    /**
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
