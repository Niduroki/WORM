package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * An indestructible obstacle
 *
 * @see de.hhu.propra14.team101.Terrain
 */
public class Obstacle extends AbstractTerrainObject {

    private int color;

    /**
     * Initialize a new obstacle.
     *
     * @param x x-coordinate of the object
     * @param y y-coordinate of the object
     */
    public Obstacle(int x, int y) {
        super(x, y);
        this.color = 0x777777; // Stone-ish html-color
        this.destructible = false;
    }

    /**
     * Draws the obstacle
     *
     * @param gc graphics context of the game.
     */
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.web(Integer.toHexString(this.color)));
        gc.fillOval(
                this.xCoordinate * (AbstractTerrainObject.baseSize * Main.sizeMultiplier),
                this.yCoordinate * (AbstractTerrainObject.baseSize * Main.sizeMultiplier),
                (AbstractTerrainObject.baseSize * Main.sizeMultiplier),
                (AbstractTerrainObject.baseSize * Main.sizeMultiplier)
        );
    }
}
