package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.*;
import javafx.scene.image.Image;
/**
 * Class to create terrain with
 */
public class Elixir extends Item {

    public Elixir (int x, int y) {
        super(x, y);
        this.destructible = true;

        if (!Main.headless) {
                this.image = new Image("images/Elixir.gif");
        }
    }

    public String getName() {
        return "Elixir";
    }
}

