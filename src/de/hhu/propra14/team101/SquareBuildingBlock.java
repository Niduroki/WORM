package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class to create terrain with
 */
public class SquareBuildingBlock extends AbstractTerrainObject {

    protected int size = 10;

    protected int color = 0x883300; // Dirt-ish html-color

    protected boolean destructible = true;

    public SquareBuildingBlock (int x, int y) {
        super(x, y);
    }

    /**
     * Draws the block
     */
    public void draw (GraphicsContext gc) {
        gc.setFill(Color.web(Integer.toHexString(this.color)));
        gc.fillRect(
                this.xCoordinate * SquareBuildingBlock.getSize(),
                this.yCoordinate * SquareBuildingBlock.getSize(),
                SquareBuildingBlock.getSize(),
                SquareBuildingBlock.getSize()
        );
    }
}
