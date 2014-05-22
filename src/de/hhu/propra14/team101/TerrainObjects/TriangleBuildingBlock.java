package de.hhu.propra14.team101.TerrainObjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
        gc.setFill(Color.web(Integer.toHexString(this.color)));
        if (this.getSlopedLeft()) {
            double[] xPoints = {
                    this.xCoordinate * TriangleBuildingBlock.getSize(),
                    this.xCoordinate * TriangleBuildingBlock.getSize() + TriangleBuildingBlock.getSize(),
                    this.xCoordinate * TriangleBuildingBlock.getSize() + TriangleBuildingBlock.getSize()
            };
            double[] yPoints = {
                    this.yCoordinate * TriangleBuildingBlock.getSize() + TriangleBuildingBlock.getSize(),
                    this.yCoordinate * TriangleBuildingBlock.getSize(),
                    this.yCoordinate * TriangleBuildingBlock.getSize() + TriangleBuildingBlock.getSize()
            };
            gc.fillPolygon(xPoints, yPoints, 3);
        } else {
            double[] xPoints = {
                    this.xCoordinate * TriangleBuildingBlock.getSize(),
                    this.xCoordinate * TriangleBuildingBlock.getSize(),
                    this.xCoordinate * TriangleBuildingBlock.getSize() + TriangleBuildingBlock.getSize()
            };
            double[] yPoints = {
                    this.yCoordinate * TriangleBuildingBlock.getSize(),
                    this.yCoordinate * TriangleBuildingBlock.getSize() + TriangleBuildingBlock.getSize(),
                    this.yCoordinate * TriangleBuildingBlock.getSize() + TriangleBuildingBlock.getSize()
            };
            gc.fillPolygon(xPoints, yPoints, 3);
        }

    }
}
