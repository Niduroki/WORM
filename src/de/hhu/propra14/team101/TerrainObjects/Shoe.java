package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.*;
import javafx.scene.image.Image;
/**
 * Class to create terrain with
 */
public class Shoe extends Item {

    public Shoe (int x, int y) {
        super(x, y);
        this.destructible = true;

        if (!Main.headless) {
            this.image = new Image("images/Shoe.gif");
        }
    }

    public String getName() {
        return "Shoe";
    }
}