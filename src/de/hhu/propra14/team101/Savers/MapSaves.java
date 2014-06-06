package de.hhu.propra14.team101.Savers;

import de.hhu.propra14.team101.Terrain;

import java.io.*;
import java.util.*;

/**
 * Class to load and save maps
 */

public class MapSaves extends AbstractSaver {

    /**
     * @param path Path to load from
     * @return The 2-dimensional map
     * @throws FileNotFoundException
     * Loads a map from a yaml file
     */
    public Terrain load (String path) throws FileNotFoundException {
        ArrayList<ArrayList<Map>> rawTerrain = (ArrayList<ArrayList<Map>>) this.yaml.load(GZipper.gunzip(path));
        return Terrain.deserialize(rawTerrain);
    }

    /**
     * @param terrain 2-dimensional map to save
     * @param path path to save to
     */
    public void save (Terrain terrain, String path) {
        StringWriter writer = new StringWriter();
        yaml.dump(terrain.serialize(), writer);
        try {
            FileOutputStream file = new FileOutputStream(path);
            file.write(GZipper.gzip(writer.toString()));
            file.close();
        } catch (IOException e) {
            //
        }
    }

}
