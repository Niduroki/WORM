package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A sandy block where it's difficult to move on
 */
public class SandBuildingBlock extends AbstractTerrainObject {

    private int color;

    /**
     *
     * @param x
     * @param y
     */
    public SandBuildingBlock (int x, int y) {
        super(x, y);
        this.color = 0xFFFF00; // Stone-ish html-color
        this.destructible = true;
    }

    /**
     * Draws the obstacle
     * @deprecated Should be removed, when there's a graphic for SyndBuildingBlock
     * @param gc Canvas to draw on
     */
    public void draw (GraphicsContext gc) {
        gc.setFill(Color.web(Integer.toHexString(this.color)));
        gc.fillRect(
                this.xCoordinate * (AbstractTerrainObject.baseSize * Main.sizeMultiplier),
                this.yCoordinate * (AbstractTerrainObject.baseSize * Main.sizeMultiplier),
                (AbstractTerrainObject.baseSize * Main.sizeMultiplier),
                (AbstractTerrainObject.baseSize * Main.sizeMultiplier)
        );
    }
}
