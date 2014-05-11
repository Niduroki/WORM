package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;

/**
 * Class to create terrain
 */

public class Terrain {
    private AbstractTerrainObject[][] ObjectArray;

    public Terrain () {
        this.ObjectArray = new AbstractTerrainObject[5][20];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 20; j++) {
                ObjectArray[i][j] = new SquareBuildingBlock(0,0);//i*10, j*10+200);
            }
        }
    }

    /**
     * @param gc GraphicsContext to draw on
     * Draws the terrain
     */
    public void draw (GraphicsContext gc) {
        for (int i = 0; i < ObjectArray.length; i++) {
            for (int j = 0; j < ObjectArray[i].length; j++) {
                ObjectArray[i][j].draw(gc);
            }
        }
    }
}
