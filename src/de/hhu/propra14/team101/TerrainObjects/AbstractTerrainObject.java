package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Class to create terrain with
 */
abstract public class AbstractTerrainObject {
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
     * @param coords Coordinates [0] is X [1] is Y
     * Sets coordinates
     */
    public void setCoords (int[] coords) {
        this.xCoordinate = coords[0];
        this.yCoordinate = coords[1];
    }

    /**
     * @return Coordinates [0] is X [1] is Y
     * Gets coordinates
     */
    public int[] getCoords () {
        return new int[]{this.xCoordinate, this.yCoordinate};
    }

    /**
     * @param destructible Destructibility of the object
     * Sets the destructibility
     */
    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    /**
     * @return Destructibility of the object
     * Gets the destructibility
     */
    public boolean getDestructible() {
        return this.destructible;
    }

    /**
     * Draws the object
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
