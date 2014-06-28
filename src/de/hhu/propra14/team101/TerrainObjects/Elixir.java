package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Main;
import javafx.scene.image.Image;

/**
 * Creates an elixir item that may heal a worm
 *
 * @see de.hhu.propra14.team101.Terrain
 */
public class Elixir extends Item {

    /**
     * Initialize a new elixir
     *
     * @param x X-coordinate of the object
     * @param y Y-coordinate of the object
     */
    public Elixir(int x, int y) {
        super(x, y);
        this.destructible = true;
        this.name = "Elixir";

        if (!Main.headless) {
            this.image = new Image("images/Elixir.gif");
        }
    }
}

