package de.hhu.propra14.team101;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Class to load and save games
 */

public class GameSaves {

    private Yaml yaml;

    public GameSaves() {
        this.yaml = new Yaml();
    }

    /**
     * @param path Path to save to
     */
    public void save(AbstractTerrainObject[][] terrain, Worm[] worms, int round, String path) throws FileNotFoundException {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("terrain", terrain);
        data.put("worms", worms);
        data.put("round", round);
        StringWriter writer = new StringWriter();
        yaml.dump(data, writer);
    }

    /**
     * @param path Path to settings file
     */
    public Map<String, Object> load(String path) throws FileNotFoundException {
        InputStream input = new FileInputStream(new File(path));
        Map<String, Object> data = (Map<String, Object>) this.yaml.load(input);
        return data;
    }
}
