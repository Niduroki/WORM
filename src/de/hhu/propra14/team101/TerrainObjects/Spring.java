package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Main;
import javafx.scene.image.Image;

/**
 * Creates a spring item, that makes worms jump higher
 *
 * @see de.hhu.propra14.team101.Terrain
 */
public class Spring extends Item {

    /**
     * Initialize a new spring.
     *
     * @param x x-coordinate of the object
     * @param y y-coordinate of the object
     */
    public Spring(int x, int y) {
        super(x, y);
        this.destructible = true;
        this.name = "Spring";

        if (!Main.headless) {
            this.image = new Image("images/Spring.gif");
        }
    }
}