package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class to create terrain with
 */
public class SquareBuildingBlock extends AbstractTerrainObject {

    protected int size = 10;

    protected int color = 0x883300; // Dirt-ish html-color

    protected boolean destructibility = true;

    /**
     * Draws the block
     */
    public void draw (GraphicsContext gc, int xCoordinate, int yCoordinate) {
        gc.setFill(Color.web(Integer.toHexString(this.color)));
        gc.fillRect(xCoordinate * this.getSize(), yCoordinate * getSize(), this.getSize(), this.getSize());
    }
}
