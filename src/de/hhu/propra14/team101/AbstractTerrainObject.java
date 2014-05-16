package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;

/**
 * Class to create terrain with
 */
abstract public class AbstractTerrainObject {
    protected static int size = 10;

    protected int color;

    protected boolean destructible;

    /**
     * @param size Size for the object
     * Sets the size
     */
    public static void setSize (int size) {
        AbstractTerrainObject.size = size;
    }

    /**
     * @return Size of the object
     * Gets the size
     */
    public static int getSize () {
        return AbstractTerrainObject.size;
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
    abstract public void draw (GraphicsContext gc, int xCoordinate, int yCoordinate);
}
