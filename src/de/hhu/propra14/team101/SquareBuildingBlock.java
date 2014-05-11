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

    public SquareBuildingBlock (int x, int y) {
        super(x, y);
    }

    /**
     * Draws the block
     */
    public void draw (GraphicsContext gc) {
        gc.setFill(Color.web(Integer.toHexString(this.color)));
        gc.fillRect(this.x_coord, this.y_coord, this.size, this.size);
    }
}
