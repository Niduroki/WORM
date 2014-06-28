package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Main;
import javafx.scene.image.Image;

/**
 * Creates a square, dirt building block
 *
 * @see de.hhu.propra14.team101.Terrain
 */
public class SquareBuildingBlock extends AbstractTerrainObject {

    /**
     * Initialize a new SquareBuildingBlock.
     *
     * @param x x-coordinate of the object
     * @param y y-coordinate of the object
     */
    public SquareBuildingBlock(int x, int y) {
        super(x, y);
        this.destructible = true;

        if (!Main.headless) {
            this.image = new Image("images/Normal-Ground-Square.jpg");
        }
    }
}
