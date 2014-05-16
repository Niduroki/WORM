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

    public TriangleBuildingBlock (boolean slopedLeft) {
        super();
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
    public void draw (GraphicsContext gc, int xCoordinate, int yCoordinate) {
        gc.setFill(Color.web(Integer.toHexString(this.color)));
        if (this.getSlopedLeft()) {
            double[] xPoints = {xCoordinate, xCoordinate + this.getSize(), xCoordinate + this.getSize()};
            double[] yPoints = {yCoordinate + this.getSize(), yCoordinate, yCoordinate + this.getSize()};
            gc.fillPolygon(xPoints, yPoints, 3);
        } else {
            double[] xPoints = {xCoordinate + this.getSize(), xCoordinate, xCoordinate};
            double[] yPoints = {yCoordinate + this.getSize(), yCoordinate, yCoordinate + this.getSize()};
            gc.fillPolygon(xPoints, yPoints, 3);
        }

    }
}
