package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class to create Worms
 */

public class Worm extends  AbstractTerrainObject
{
    public Weapons[] WeaponArray = new Weapons[10];

    public Worm(int x, int y)
    {
        super(x, y);
    }

    /**
     * Draws the object
     *
     * @param gc
     */
    @Override
    public void draw(GraphicsContext gc)
    {
            Image image = new Image("worm.gif");
            gc.drawImage(image, this.x_coord, this.y_coord);
    }
}
