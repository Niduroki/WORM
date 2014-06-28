package de.hhu.propra14.team101;

import com.sun.istack.internal.Nullable;
import de.hhu.propra14.team101.TerrainObjects.*;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to create terrain
 *<p/>
 * Code example:
 * <pre>
 * {@code
 * System.out.println(Terrain.getHeightInPixel());
 * System.out.println(Terrain.getWidthInPixel());
 *
 * Terrain terrain = new Terrain();
 * SquareBuildingBlock block = new SquareBuildingBlock(0, 0);
 * terrain.addTerrainObject(block);
 * Elixir elixir = new Elixir(1,0);
 * terrain.addTerrainObject(elixir);
 * System.out.println(terrain.isTerrain(1, 1);
 *
 * TriangleBuildingBlock  triBlock = new TriangleBuildingBlock(0, 2, true);
 * System.out.println(triBlock.getDestructible());
 * System.out.println(triBlock.getSlopedLeft());
 * System.out.println(triBlock.getCoords()[0]);
 * terrain. removeTerrainObject(elixir);
 *
 * ==Output==
 * 1000
 * 1500
 * true
 * false
 * true
 * 0
 * }</pre>
 */
public class Terrain {
    /**
     * Represents width of the terrain (measurement in blocks)
     */
    public final static int width = 60;

    /**
     * Represents height of the terrain (measurement in blocks)
     */
    public final static int height = 40;

    protected AbstractTerrainObject[][] terrainObjects;

    /**
     * Initialize a new terrain.
     */
    public Terrain() {
        this.terrainObjects = new AbstractTerrainObject[width][height];
    }

    /**
     * Get the height of the terrain.
     */
    public static double getHeightInPixel() {
        return height * AbstractTerrainObject.baseSize * Main.sizeMultiplier;
    }

    /**
     * Get the width of the terrain.
     */
    public static double getWidthInPixel() {
        return width * AbstractTerrainObject.baseSize * Main.sizeMultiplier;
    }

    /**
     * Add a terrain object.
     *
     * @param object terrain object, which is added
     * @throws java.lang.IllegalArgumentException coordinates of added terrain object are out of terrain.
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
     *
     * @param gc GraphicsContext to draw on
     */
    public void draw(GraphicsContext gc) {

        for (AbstractTerrainObject[] terrainObject : terrainObjects) {
            for (AbstractTerrainObject aTerrainObject : terrainObject) {
                if (aTerrainObject != null) {
                    aTerrainObject.draw(gc);
                }
            }
        }
    }

    /**
     * terrainObject, if at the specific position is a terrain object, otherwise null. measurement in pixel
     *
     * @param xCoordinate x-coordinate of the point, which is checked
     * @param yCoordinate y-coordinate of the point, which is checked
     * @return terrain object, which is located at given point, otherwise null
     * @throws java.lang.IllegalArgumentException coordinates are out of terrain.
     */
    @Nullable
    public AbstractTerrainObject isTerrain(double xCoordinate, double yCoordinate) {
        int integerXCoordinate = (new Double(xCoordinate / (AbstractTerrainObject.baseSize * Main.sizeMultiplier))).intValue();
        int integerYCoordinate = (new Double(yCoordinate / (AbstractTerrainObject.baseSize * Main.sizeMultiplier))).intValue();
        if (integerXCoordinate < 0 || integerXCoordinate >= width || integerYCoordinate < 0 || integerYCoordinate >= height) {
            throw new IllegalArgumentException("x- and integerYCoordinate must be positive and not higher or wider as the terrain.");
        }

        return terrainObjects[integerXCoordinate][integerYCoordinate];
    }

    /**
     * Remove a terrain object.
     *
     * @param terrainObject terrain object, which is removed
     * @param withExplosion true, if terrain should exploded, otherwise false
     */
    public void removeTerrainObject(AbstractTerrainObject terrainObject, boolean withExplosion) {
        if (terrainObject == null) {
            System.out.println("terrainObject is null");
            return;
        }

        for (int rowIndex = 0; rowIndex < terrainObjects.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < terrainObjects[rowIndex].length; columnIndex++) {
                if (terrainObjects[rowIndex][columnIndex] == terrainObject) {
                    if (withExplosion) {
                        removeTerrainObject(rowIndex, columnIndex);
                        removeTerrainObject(rowIndex + 1, columnIndex + 1);
                        removeTerrainObject(rowIndex + 1, columnIndex);
                        removeTerrainObject(rowIndex, columnIndex + 1);
                        removeTerrainObject(rowIndex - 1, columnIndex - 1);
                        removeTerrainObject(rowIndex - 1, columnIndex);
                        removeTerrainObject(rowIndex, columnIndex - 1);
                        removeTerrainObject(rowIndex - 1, columnIndex + 1);
                        removeTerrainObject(rowIndex + 1, columnIndex - 1);
                    } else {
                        removeTerrainObject(rowIndex, columnIndex);
                    }
                    return;
                }
            }
        }
    }

    private void removeTerrainObject(int y, int x) {
        if (y < terrainObjects.length) {
            if (x < terrainObjects[y].length) {
                if (terrainObjects[y][x] != null) terrainObjects[y][x] = null;
            }
        }
    }

    /**
     * Serialize terrain.
     *
     * @return 2-dimensional array to use with snakeyaml
     */
    public Object[][] serialize() {
        /** 2-dimensional array to store terrain in.*/
        Object[][] result = new Object[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
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

    /**
     * Deserializes terrain.
     *
     * @param input serialized data
     * @return deserialized terrain
     */
    public static Terrain deserialize(ArrayList<ArrayList<Map>> input) {
        Terrain terrain = new Terrain();
        for (ArrayList<Map> xInput : input) {
            for (Map yInput : xInput) {
                if (yInput != null) {
                    //Rebuild the block
                    @SuppressWarnings("unchecked")
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
                    } else if (yInput.get("class").equals("de.hhu.propra14.team101.TerrainObjects.ExplosiveBuildingBlock")) {
                        ExplosiveBuildingBlock workingBlock = new ExplosiveBuildingBlock(coords.get(0), coords.get(1));
                        terrain.addTerrainObject(workingBlock);
                    } else if (yInput.get("class").equals("de.hhu.propra14.team101.TerrainObjects.SandBuildingBlock")) {
                        SandBuildingBlock workingBlock = new SandBuildingBlock(coords.get(0), coords.get(1));
                        terrain.addTerrainObject(workingBlock);
                    } else if (yInput.get("class").equals("de.hhu.propra14.team101.TerrainObjects.Shoe")) {
                        Shoe workingBlock = new Shoe(coords.get(0), coords.get(1));
                        terrain.addTerrainObject(workingBlock);
                    } else if (yInput.get("class").equals("de.hhu.propra14.team101.TerrainObjects.Elixir")) {
                        Elixir workingBlock = new Elixir(coords.get(0), coords.get(1));
                        terrain.addTerrainObject(workingBlock);
                    } else if (yInput.get("class").equals("de.hhu.propra14.team101.TerrainObjects.Spring")) {
                        Spring workingBlock = new Spring(coords.get(0), coords.get(1));
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