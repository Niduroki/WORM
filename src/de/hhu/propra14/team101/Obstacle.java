package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class to create terrain with
 */
public class Obstacle extends AbstractTerrainObject {

    protected int size = 10;

    protected int color = 0x777777; // Stone-ish html-color

    protected boolean destructibility = false;

    /**
     * Draws the obstacle
     */
    public void draw (GraphicsContext gc, int xCoordinate, int yCoordinate) {
        gc.setFill(Color.web(Integer.toHexString(this.color)));
        gc.fillOval(xCoordinate * this.getSize(), yCoordinate * this.getSize(), this.getSize(), this.getSize());
    }
}
