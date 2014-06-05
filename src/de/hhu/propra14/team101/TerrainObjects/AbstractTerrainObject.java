package de.hhu.propra14.team101.TerrainObjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Class to create terrain with
 */
abstract public class AbstractTerrainObject {
    protected int size = 10;

    protected int color;

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
     * @param color Color for the object
     * Sets the color
     */
    public void setColor (int color) {
        this.color = color;
    }

    /**
     * @return Color of the object
     * Gets the color
     */
    public int getColor () {
        return this.color;
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
        gc.drawImage(this.image, this.xCoordinate*this.size, this.yCoordinate*this.size, this.size, this.size);
    }
}
