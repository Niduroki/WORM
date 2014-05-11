package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;

/**
 * Class to create terrain with
 */
abstract public class AbstractTerrainObject {

    protected int x_coord;

    protected int y_coord;

    protected int size;

    protected int color;

    protected boolean destructible;

    /**
     * @param x X-Coordinate of the object
     * @param y Y-Coordinate of the object
     */
    public AbstractTerrainObject (int x, int y) {
        this.x_coord = x;
        this.y_coord = y;
    }

    /**
     * @param coords Coordinates [0] is X [1] is Y
     * Sets coordinates
     */
    public void setCoords (int[] coords) {
        this.x_coord = coords[0];
        this.y_coord = coords[1];
    }

    /**
     * @return Coordinates [0] is X [1] is Y
     * Gets coordinates
     */
    public int[] getCoords () {
        return new int[]{this.x_coord, this.y_coord};
    }

    /**
     * @param size Size for the object
     * Sets the size
     */
    public void setSize (int size) {
        this.size = size;
    }

    /**
     * @return Size of the object
     * Gets the size
     */
    public int getSize () {
        return this.size;
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
    abstract public void draw (GraphicsContext gc);
}
