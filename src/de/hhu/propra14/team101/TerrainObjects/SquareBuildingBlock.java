package de.hhu.propra14.team101.TerrainObjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * Class to create terrain with
 */
public class SquareBuildingBlock extends AbstractTerrainObject {

    protected int size = 10;

    public SquareBuildingBlock (int x, int y) {
        super(x, y);
        this.color = 0x883300; // Dirt-ish html-color
        this.destructible = true;
    }

    /**
     * Draws the block
     */
    public void draw (GraphicsContext gc) {
        Image image = new Image("ground.jpg");
        gc.drawImage(image, this.xCoordinate*this.size, this.yCoordinate*this.size, this.size, this.size);
    }
}
