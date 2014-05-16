package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;

/**
 * Class to create terrain
 */
public class Terrain {
    private AbstractTerrainObject[][] terrainObjects;
    private int width;
    private int height;

    public Terrain (int width, int height) {
        // TODO load this from a file instead
        //MapSaves mapLoader = new MapSaves();
        //this.ObjectArray = mapLoader.load("Flatlands.ymp");
        this.terrainObjects = new AbstractTerrainObject[width][height];
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void addTerrainObject(AbstractTerrainObject object, int xCoordinate, int yCoordinate)
    {
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
                    terrainObjects[i][j].draw(gc, i, j);
                }
            }
        }
    }

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

    public void removeTerrainObject(int xCoordinate, int yCoordinate) {
        if (xCoordinate < 0 || xCoordinate > width || yCoordinate < 0 || yCoordinate > height) {
            throw new IllegalArgumentException("x- and yCoordinate must be positive and not higher or wider as the terrain.");
        }

        SquareBuildingBlock dummy = null;
        this.terrainObjects[xCoordinate][yCoordinate] = dummy;
    }
}