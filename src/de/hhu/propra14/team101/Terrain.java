package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;

/**
 * Class to create terrain
 */

public class Terrain {
    private AbstractTerrainObject[][] ObjectArray;

    public Terrain () {
        // TODO load this from a file instead
        MapSaves mapLoader = new MapSaves();
        //this.ObjectArray = mapLoader.load("Flatlands.ymp");
        this.ObjectArray = new AbstractTerrainObject[60][40];

        // TODO this should be removed then
        for (int i = 0; i < 60; i++) {
            for (int j = 20; j < 40; j++) {
                ObjectArray[i][j] = new SquareBuildingBlock(i * 10, j * 10);
            }
        }

        ObjectArray[10][19] = new TriangleBuildingBlock(100, 190, true);
        ObjectArray[11][18] = new TriangleBuildingBlock(110, 180, true);
        ObjectArray[11][19] = new SquareBuildingBlock(110, 190);
        ObjectArray[12][18] = new TriangleBuildingBlock(120, 180, false);
        ObjectArray[12][19] = new SquareBuildingBlock(120, 190);
        ObjectArray[13][19] = new TriangleBuildingBlock(130, 190, false);
        ObjectArray[15][19] = new Obstacle(150, 190);
        ObjectArray[15][18] = new Obstacle(150, 180);
        ObjectArray[15][17] = new Obstacle(150, 170);
    }

    /**
     * @param gc GraphicsContext to draw on
     * Draws the terrain
     */
    public void draw (GraphicsContext gc) {
        for (int i = 0; i < ObjectArray.length; i++) {
            for (int j = 0; j < ObjectArray[i].length; j++) {
                if(ObjectArray[i][j] != null)
                {
                    ObjectArray[i][j].draw(gc);
                }
            }
        }
    }
}
