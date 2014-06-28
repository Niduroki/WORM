package de.hhu.propra14.team101.Savers;

import java.io.*;
import java.util.Map;

/**
 * Class to load and save settings
 *
 * <pre>
 * {@code
 * SettingSaves settingSaves = new SettingSaves();
 * Map settings = settingSaves.load("settings.gz");
 * System.out.println(settings.get("name"));
 * settings.put("name", "myname");
 * settingSaves.save("settings.gz");
 * }
 * </pre>
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
     * @throws java.io.FileNotFoundException If file not found
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> load(String path) throws FileNotFoundException {
        return (Map<String, Object>) this.yaml.load(GZipper.gunzip(path));
    }
}
