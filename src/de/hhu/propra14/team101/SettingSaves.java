package de.hhu.propra14.team101;

import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;

/**
 * Class to load and save settings
 */

public class SettingSaves {

    private Yaml yaml;

    public SettingSaves() {
        this.yaml = new Yaml();
    }

    /**
     * @param data Settings data
     * @param path Path to save to
     */
    public void save(String[][] data, String path) throws FileNotFoundException {
        //
    }

    /**
     * @param path Path to settings file
     */
    public String[][] load(String path) {
        //
        return new String[1][1];
    }
}
