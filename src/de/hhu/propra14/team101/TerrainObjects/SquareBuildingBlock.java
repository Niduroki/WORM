package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Main;
import javafx.scene.image.Image;

/**
 * Creates a square, dirt building block
 */
public class SquareBuildingBlock extends AbstractTerrainObject {

    /**
     *
     * @param x
     * @param y
     */
    public SquareBuildingBlock (int x, int y) {
        super(x, y);
        this.destructible = true;

        if (!Main.headless) {
            this.image = new Image("images/Normal-Ground-Square.jpg");
        }
    }
}
