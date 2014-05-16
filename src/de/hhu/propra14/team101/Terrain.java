package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;

/**
 * Class to create terrain
 */
public class Terrain {
    private AbstractTerrainObject[][] terrainObjects;
    private int width;
    private int height;

    /**
     * Initialize a new terrain.
     * @param width width of terrain. measurement: size of a block
     * @param height height of terrain. measurement size of a block.
     */
    public Terrain (int width, int height) {
        // TODO load this from a file instead
        //MapSaves mapLoader = new MapSaves();
        //this.ObjectArray = mapLoader.load("Flatlands.ymp");
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
    public void addTerrainObject(AbstractTerrainObject object)
    {
        int xCoordinate = object.xCoordinate;
        int yCoordinate = object.yCoordinate;

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
        for (int i = 0; i < terrainObjects.length; i++) {
            for (int j = 0; j < terrainObjects[i].length; j++) {
                if (terrainObjects[i][j] != null) {
                    terrainObjects[i][j].draw(gc);
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

        if (terrainObjects[xCoordinate][yCoordinate] == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Remove a terrain object.
     * @exception java.lang.IllegalArgumentException if x or y-coordinate is negative or higher or wider as the terrain.
     */
    public void removeTerrainObject(int xCoordinate, int yCoordinate) {
        if (xCoordinate < 0 || xCoordinate > width || yCoordinate < 0 || yCoordinate > height) {
            throw new IllegalArgumentException("x- and yCoordinate must be positive and not higher or wider as the terrain.");
        }

        SquareBuildingBlock dummy = null;
        this.terrainObjects[xCoordinate][yCoordinate] = dummy;
    }
}