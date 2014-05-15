package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class to create terrain with
 */
public class TriangleBuildingBlock extends AbstractTerrainObject {

    protected int size = 10;

    protected int color = 0x883300; // Dirt-ish html-color

    protected boolean destructibility = true;

    protected boolean slopedLeft = true;

    public TriangleBuildingBlock (int x, int y, boolean slopedLeft) {
        super(x, y);
        this.slopedLeft = slopedLeft;
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
        gc.setFill(Color.web(Integer.toHexString(this.color)));
        if (this.slopedLeft) {
            double[] xPoints = {this.x_coord, this.x_coord + this.size, this.x_coord + this.size};
            double[] yPoints = {this.y_coord + this.size, this.y_coord, this.y_coord + this.size};
            gc.fillPolygon(xPoints, yPoints,3);
        } else {
            double[] xPoints = {this.x_coord + this.size, this.x_coord, this.x_coord};
            double[] yPoints = {this.y_coord + this.size, this.y_coord, this.y_coord + this.size};
            gc.fillPolygon(xPoints, yPoints, 3);
        }

    }
}
