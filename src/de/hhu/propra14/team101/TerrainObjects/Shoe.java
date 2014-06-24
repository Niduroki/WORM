package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.*;
import javafx.scene.image.Image;
/**
 * Creates a shoe item, that makes it easier for worms to move on sand
 */
public class Shoe extends Item {

    /**
     *
     * @param x
     * @param y
     */
    public Shoe (int x, int y) {
        super(x, y);
        this.destructible = true;
        this.name = "Shoe";

        if (!Main.headless) {
            this.image = new Image("images/Shoe.gif");
        }
    }
}