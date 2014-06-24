package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.*;
import javafx.scene.image.Image;
/**
 * Creates a spring item, that makes worms jump higher
 */
public class Spring extends Item {

    public Spring(int x, int y) {
        super(x, y);
        this.destructible = true;
        this.name = "Spring";

        if (!Main.headless) {
            this.image = new Image("images/Spring.gif");
        }
    }
}