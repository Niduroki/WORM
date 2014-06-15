package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Main;
import javafx.scene.image.Image;

/**
 * An explosive terrain block
 */
public class ExplosiveBuildingBlock extends AbstractTerrainObject {
    public ExplosiveBuildingBlock(int x, int y) {
        super(x, y);
        this.destructible = true;

        if (!Main.headless) {
            this.image = new Image("images/Normal-Ground-Square.jpg");
        }
    }
}
