package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Main;
import javafx.scene.image.Image;

/**
 * A sandy block where it's difficult to move on
 *
 * @see de.hhu.propra14.team101.Terrain
 */
public class SandBuildingBlock extends AbstractTerrainObject {

    /**
     * Initialize a new SandBuildingBlock
     *
     * @param x x-coordinate of the object
     * @param y y-coordinate of the object
     */
    public SandBuildingBlock(int x, int y) {
        super(x, y);
        this.destructible = true;

        if (!Main.headless) {
            this.image = new Image("images/Sand.jpg");
        }
    }
}
