package de.hhu.propra14.team101.TerrainObjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Class to create terrain with
 */
public class TriangleBuildingBlock extends AbstractTerrainObject {

    protected int size = 10;

    protected boolean slopedLeft;

    public TriangleBuildingBlock (int x, int y, boolean slopedLeft) {
        super(x, y);
        this.slopedLeft = slopedLeft;
        this.color = 0x883300; // Dirt-ish html-color
        this.destructible = true;
    }

    public void setSlopedLeft (boolean slopedLeft) {
        this.slopedLeft = slopedLeft;
    }

    public boolean getSlopedLeft() {
        return this.slopedLeft;
    }

    /**
     * Draws the block
     */
    public void draw (GraphicsContext gc) {

        if (this.getSlopedLeft()) {
            Image image = new Image("ground-triangle-right.png");
            gc.drawImage(image, this.xCoordinate*this.size, this.yCoordinate*this.size, this.size, this.size);
        } else {
            Image image = new Image("ground-triangle-left.png");
            gc.drawImage(image, this.xCoordinate*this.size, this.yCoordinate*this.size, this.size, this.size);
        }

    }
}
