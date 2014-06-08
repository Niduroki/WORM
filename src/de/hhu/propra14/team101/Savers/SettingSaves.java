package de.hhu.propra14.team101.Savers;

import java.io.*;
import java.util.Map;

/**
 * Class to load and save settings
 */

public class SettingSaves extends AbstractSaver {

    /**
     * Saves Settings
     * @param data Settings data
     * @param path Path to save to
     */
    public void save(Map data, String path) {
        StringWriter writer = new StringWriter();
        yaml.dump(data, writer);
        try {
            FileOutputStream file = new FileOutputStream(path);
            file.write(GZipper.gzip(writer.toString()));
            file.close();
        } catch (IOException e) {
            //
        }
    }

    /**
     * Loaded Settings
     * @param path Path to settings file
     * @return Map with Settings data
     */
    public Map load(String path) throws FileNotFoundException {
        Map<String, Object> data = (Map<String, Object>) this.yaml.load(GZipper.gunzip(path));
        return data;
    }
}
