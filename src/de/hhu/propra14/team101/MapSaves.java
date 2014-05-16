package de.hhu.propra14.team101;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;
import java.util.function.BooleanSupplier;

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
    public Terrain load (String path) throws FileNotFoundException {
        //AbstractTerrainObject[][] terrain = new AbstractTerrainObject[40][30];
        InputStream input = new FileInputStream(new File(path));
        //AbstractTerrainObject[][] rawTerrain = (AbstractTerrainObject[][]) this.yaml.load(input);
        ArrayList<ArrayList<Map>> rawTerrain = (ArrayList<ArrayList<Map>>) this.yaml.load(input);
        return this.deserialize(rawTerrain);
    }

    /**
     * @param terrain 2-dimensional map to save
     * @param path path to save to
     */
    public void save (Terrain terrain, String path) {
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(this.serialize(terrain), writer);
        try {
            FileWriter file = new FileWriter(path);
            file.write(writer.toString());
            file.close();
        } catch (java.io.IOException e) {
            //
        }
    }

    private Object[][] serialize (Terrain terrain) {
        /** 2-dimensional array to store terrain in.*/
        Object[][] result = new Object[terrain.getWidth()][terrain.getHeight()];

        for (int i=0; i<terrain.getWidth(); i++) {
            for (int j=0; j<terrain.getHeight(); j++) {
                AbstractTerrainObject workingBlock = terrain.terrainObjects[i][j];
                if (workingBlock != null) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("color", workingBlock.getColor());
                    map.put("coords", workingBlock.getCoords());
                    map.put("destructibility", workingBlock.getDestructible());
                    map.put("class", workingBlock.getClass().getName());
                    // Save slope if we have a triangle
                    if (workingBlock.getClass().getName().equals("de.hhu.propra14.team101.TriangleBuildingBlock")) {
                        TriangleBuildingBlock tmp = (TriangleBuildingBlock) terrain.terrainObjects[i][j];
                        map.put("slopedLeft", tmp.getSlopedLeft());
                    }
                    result[i][j] = map;
                }
            }
        }
        return result;
    }

    private Terrain deserialize (ArrayList<ArrayList<Map>> input) {
        Terrain terrain = new Terrain(input.size(), input.get(0).size());
        for (int i=0; i<input.size(); i++) {
            for (int j=0; j<input.get(i).size(); j++) {
                if (input.get(i).get(j) != null) {
                    //Rebuild the block
                    Map workingMap = input.get(i).get(j);
                    boolean destructibility = Boolean.parseBoolean(workingMap.get("destructibility").toString());
                    Integer color = (Integer) workingMap.get("color");
                    ArrayList<Integer> coords = (ArrayList<Integer>) workingMap.get("coords");
                    if (workingMap.get("class").equals("de.hhu.propra14.team101.SquareBuildingBlock")) {
                        SquareBuildingBlock workingBlock = new SquareBuildingBlock(coords.get(0), coords.get(1));
                        workingBlock.setDestructible(destructibility);
                        terrain.addTerrainObject(workingBlock);
                    } else if (workingMap.get("class").equals("de.hhu.propra14.team101.TriangleBuildingBlock")) {
                        boolean slopedLeft = Boolean.parseBoolean(workingMap.get("slopedLeft").toString());
                        TriangleBuildingBlock workingBlock = new TriangleBuildingBlock(coords.get(0), coords.get(1), slopedLeft);
                        workingBlock.setDestructible(destructibility);
                        terrain.addTerrainObject(workingBlock);
                    } else if (workingMap.get("class").equals("de.hhu.propra14.team101.Obstacle")) {
                        Obstacle workingBlock = new Obstacle(coords.get(0), coords.get(1));
                        workingBlock.setDestructible(destructibility);
                        terrain.addTerrainObject(workingBlock);
                    } else {
                        System.out.println("MapSaves.deserialize:Unknown block");
                    }
                }
            }
        }
        return terrain;
    }
}
