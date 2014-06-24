package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Class to create terrain with
 */
abstract public class AbstractTerrainObject {
    /** Normal size in pixels of a terrain block */
    public static int baseSize = 10;

    protected boolean destructible;

    protected int xCoordinate;
    protected int yCoordinate;

    protected Image image;

    /**
     * @param x X-Coordinate of the object
     * @param y Y-Coordinate of the object
     */
    public AbstractTerrainObject (int x, int y) {
        this.xCoordinate = x;
        this.yCoordinate = y;
    }

    /**
     * Sets coordinates
     * @param coords Coordinates [0] is X [1] is Y
     */
    public void setCoords (int[] coords) {
        this.xCoordinate = coords[0];
        this.yCoordinate = coords[1];
    }

    /**
     * Gets coordinates
     * @return Coordinates [0] is X [1] is Y
     */
    public int[] getCoords () {
        return new int[]{this.xCoordinate, this.yCoordinate};
    }

    /**
     * Gets the destructibility
     * @return Destructibility of the object
     */
    public boolean getDestructible() {
        return this.destructible;
    }

    /**
     * Draws the object
     * @param gc Canvas to draw on
     */
    public void draw (GraphicsContext gc) {
        gc.drawImage(
                this.image,
                this.xCoordinate*(AbstractTerrainObject.baseSize * Main.sizeMultiplier),
                this.yCoordinate*(AbstractTerrainObject.baseSize * Main.sizeMultiplier),
                (AbstractTerrainObject.baseSize * Main.sizeMultiplier),
                (AbstractTerrainObject.baseSize * Main.sizeMultiplier)
        );
    }
}
