package de.hhu.propra14.team101.Savers;

import de.hhu.propra14.team101.Savers.AbstractSaver;

import java.io.*;
import java.util.Map;

/**
 * Class to load and save settings
 */

public class SettingSaves extends AbstractSaver {

    /**
     * @param data Settings data
     * @param path Path to save to
     */
    public void save(Map data, String path) {
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
     * @param path Path to settings file
     */
    public Map load(String path) throws FileNotFoundException {
        InputStream input = new FileInputStream(new File(path));
        Map<String, Object> data = (Map<String, Object>) this.yaml.load(input);
        return data;
    }
}
