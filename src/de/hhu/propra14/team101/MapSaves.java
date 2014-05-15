package de.hhu.propra14.team101;

import org.yaml.snakeyaml.Yaml;

import java.io.*;

/**
 * Class to load and save maps
 */

public class MapSaves {

    private Yaml yaml;

    /**
     * Initializes with a Yaml object
     */
    public MapSaves() {
        this.yaml = new Yaml();
    }

    /**
     * @param path Path to load from
     * @return The 2-dimensional map
     * @throws FileNotFoundException
     * Loads a map from a yaml file
     */
    public AbstractTerrainObject[][] load (String path) throws FileNotFoundException {
        //AbstractTerrainObject[][] terrain = new AbstractTerrainObject[40][30];
        InputStream input = new FileInputStream(new File(path));
        AbstractTerrainObject[][] terrain = (AbstractTerrainObject[][]) this.yaml.load(input);
        return terrain;
    }

    /**
     * @param terrain 2-dimensional map to save
     * @param path path to save to
     */
    public void save (AbstractTerrainObject[][] terrain, String path) {
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(terrain, writer);
        // TODO save this into a file
    }
}
