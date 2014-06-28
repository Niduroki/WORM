package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Main;
import javafx.scene.image.Image;

/**
 * Creates a shoe item, that makes it easier for worms to move on sand
 *
 * @see de.hhu.propra14.team101.Terrain
 */
public class Shoe extends Item {

    /**
     * Initialize a new shoe terrain element.
     *
     * @param x x-coordinate of the object
     * @param y y-coordinate of the object
     */
    public Shoe(int x, int y) {
        super(x, y);
        this.destructible = true;
        this.name = "Shoe";

        if (!Main.headless) {
            this.image = new Image("images/Shoe.gif");
        }
    }
}