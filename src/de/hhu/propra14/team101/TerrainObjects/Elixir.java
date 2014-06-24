package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.*;
import javafx.scene.image.Image;

/**
 * Creates an elixir item that may heal a worm
 */
public class Elixir extends Item {

    /**
     *
     * @param x
     * @param y
     */
    public Elixir (int x, int y) {
        super(x, y);
        this.destructible = true;
        this.name = "Elixir";

        if (!Main.headless) {
                this.image = new Image("images/Elixir.gif");
        }
    }
}

