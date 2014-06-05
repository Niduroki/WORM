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
        InputStream input;
        try {
            // TODO https://stackoverflow.com/questions/1913565/how-can-i-access-a-txt-file-in-a-jar-with-fileinputstream
            // This can't work in a jar
            input = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            // If we're not in a jar the maps are under resources
            input = new FileInputStream(new File("resources/" + path));
        }
        Map<String, Object> raw = (Map<String, Object>) this.yaml.load(input);
        return Level.deserialize(raw);
    }

    /**
     * @param level level to save
     * @param path path to save to
     */
    public void save (Level level, String path) {
        StringWriter writer = new StringWriter();
        this.yaml.dump(level.serialize(), writer);
        try {
            FileWriter file = new FileWriter(path);
            file.write(writer.toString());
            file.close();
        } catch (IOException e) {
            //
        }
    }
}
