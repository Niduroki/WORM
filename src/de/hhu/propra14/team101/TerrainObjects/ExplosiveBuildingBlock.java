package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Level;
import de.hhu.propra14.team101.Main;
import javafx.scene.image.Image;

/**
 * An explosive terrain block
 *
 * @see de.hhu.propra14.team101.Terrain
 */
public class ExplosiveBuildingBlock extends AbstractTerrainObject {

    /**
     * Initialize a new AbstractTerrainObject
     *
     * @param x x-coordinate of the object
     * @param y y-coordinate of the object
     */
    public ExplosiveBuildingBlock(int x, int y) {
        super(x, y);
        this.destructible = true;

        if (!Main.headless) {
            String capitalizedTheme = Level.theme.substring(0, 1).toUpperCase() + Level.theme.substring(1);
            this.image = new Image("images/"+capitalizedTheme+"-Ground-Explosive.jpg");
        }
    }
}
