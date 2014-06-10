package de.hhu.propra14.team101;

import de.hhu.propra14.team101.TerrainObjects.AbstractTerrainObject;
import de.hhu.propra14.team101.TerrainObjects.Obstacle;
import de.hhu.propra14.team101.TerrainObjects.SquareBuildingBlock;
import de.hhu.propra14.team101.TerrainObjects.TriangleBuildingBlock;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to create terrain
 */
public class Terrain {
    protected AbstractTerrainObject[][] terrainObjects;
    private int width;
    private int height;

    /**
     * Initialize a new terrain.
     * @param width width of terrain. measurement: size of a block
     * @param height height of terrain. measurement size of a block.
     */
    public Terrain (int width, int height) {
        this.terrainObjects = new AbstractTerrainObject[width][height];
        this.width = width;
        this.height = height;
    }

    /**
     * Get the height of the terrain.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the width of the terrain.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Add a terrain object.
     */
    public void addTerrainObject(AbstractTerrainObject object) {
        int xCoordinate = object.getCoords()[0];
        int yCoordinate = object.getCoords()[1];

        if (xCoordinate < 0 || xCoordinate > width || yCoordinate < 0 || yCoordinate > height) {
            throw new IllegalArgumentException("x- and yCoordinate must be positive and not higher or wider as the terrain.");
        }

        terrainObjects[xCoordinate][yCoordinate] = object;
    }

    /**
     * Draws the terrain
     * @param gc GraphicsContext to draw on
     */
    public void draw (GraphicsContext gc) {

        for (AbstractTerrainObject[] terrainObject : terrainObjects) {
            for (AbstractTerrainObject aTerrainObject : terrainObject) {
                if (aTerrainObject != null) {
                    aTerrainObject.draw(gc);
                }
            }
        }
    }

    /**
     * true, if at the specific position is a terrain object
     */
    public boolean isTerrain(int xCoordinate, int yCoordinate) {
        if (xCoordinate < 0 || xCoordinate > width || yCoordinate < 0 || yCoordinate > height) {
            throw new IllegalArgumentException("x- and yCoordinate must be positive and not higher or wider as the terrain.");
        }

        return terrainObjects[xCoordinate][yCoordinate] != null;
    }

    /**
     * Remove a terrain object.
     * @exception java.lang.IllegalArgumentException if x or y-coordinate is negative or higher or wider as the terrain.
     */
    public void removeTerrainObject(int xCoordinate, int yCoordinate) {
        if (xCoordinate < 0 || xCoordinate > width || yCoordinate < 0 || yCoordinate > height) {
            throw new IllegalArgumentException("x- and yCoordinate must be positive and not higher or wider as the terrain.");
        }

        this.terrainObjects[xCoordinate][yCoordinate] = null;
    }

    /**
     * @return 2-dimensional array to use with snakeyaml
     */
    public Object[][] serialize () {
        /** 2-dimensional array to store terrain in.*/
        Object[][] result = new Object[this.getWidth()][this.getHeight()];

        for (int i=0; i<this.getWidth(); i++) {
            for (int j=0; j<this.getHeight(); j++) {
                AbstractTerrainObject workingBlock = this.terrainObjects[i][j];
                if (workingBlock != null) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("coords", workingBlock.getCoords());
                    map.put("class", workingBlock.getClass().getName());
                    // Save slope if we have a triangle
                    if (workingBlock.getClass().getName().equals("de.hhu.propra14.team101.TerrainObjects.TriangleBuildingBlock")) {
                        TriangleBuildingBlock tmp = (TriangleBuildingBlock) this.terrainObjects[i][j];
                        map.put("slopedLeft", tmp.getSlopedLeft());
                    }
                    result[i][j] = map;
                }
            }
        }
        return result;
    }

    public static Terrain deserialize (ArrayList<ArrayList<Map>> input) {
        Terrain terrain = new Terrain(input.size(), input.get(0).size());
        for (ArrayList<Map> xInput : input) {
            for (Map yInput : xInput) {
                if (yInput != null) {
                    //Rebuild the block
                    ArrayList<Integer> coords = (ArrayList<Integer>) yInput.get("coords");
                    if (yInput.get("class").equals("de.hhu.propra14.team101.TerrainObjects.SquareBuildingBlock")) {
                        SquareBuildingBlock workingBlock = new SquareBuildingBlock(coords.get(0), coords.get(1));
                        terrain.addTerrainObject(workingBlock);
                    } else if (yInput.get("class").equals("de.hhu.propra14.team101.TerrainObjects.TriangleBuildingBlock")) {
                        boolean slopedLeft = Boolean.parseBoolean(yInput.get("slopedLeft").toString());
                        TriangleBuildingBlock workingBlock = new TriangleBuildingBlock(coords.get(0), coords.get(1), slopedLeft);
                        terrain.addTerrainObject(workingBlock);
                    } else if (yInput.get("class").equals("de.hhu.propra14.team101.TerrainObjects.Obstacle")) {
                        Obstacle workingBlock = new Obstacle(coords.get(0), coords.get(1));
                        terrain.addTerrainObject(workingBlock);
                    } else {
                        System.out.println("Terrain.deserialize:Unknown block");
                    }
                }
            }
        }
        return terrain;
    }
}