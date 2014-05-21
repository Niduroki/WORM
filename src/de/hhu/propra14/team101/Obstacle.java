package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class to create terrain with
 */
public class Obstacle extends AbstractTerrainObject {

    protected int size = 10;

    public Obstacle (int x, int y) {
        super(x, y);
        this.color = 0x777777; // Stone-ish html-color
        this.destructible = false;
    }

    /**
     * Draws the obstacle
     */
    public void draw (GraphicsContext gc) {
        gc.setFill(Color.web(Integer.toHexString(this.color)));
        gc.fillOval(
                this.xCoordinate * Obstacle.getSize(),
                this.yCoordinate * Obstacle.getSize(),
                Obstacle.getSize(), Obstacle.getSize()
        );
    }
}
