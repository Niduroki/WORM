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

public class Worm {

    public Weapons[] WeaponArray;

    public int health = 100;

    protected int x_coord;

    protected int y_coord;

    public Worm (int x_coord, int y_coord) {
        this.x_coord = x_coord;
        this.y_coord = y_coord;
    }

    /**
     * @param gc Canvas to draw on
     * Draws the worm
     */
    public void draw (GraphicsContext gc) {
        Image image = new Image("de/hhu/propra14/team101/resources/worm.gif");
        gc.drawImage(image, this.x_coord, this.y_coord);
    }

    public void move (char direction) {
        if (direction == 'l') {
            this.x_coord -= 5;
            // Send movement-left to the server
        } else if (direction == 'r') {
            this.x_coord += 5;
            // Send movement-right to the server
        }
    }

    public void jump (char direction) {
        //
    }

    public void loseHealth (int amount) {
        this.health -= amount;

        if (this.health <= 0) {
            this.die();
        }
    }

    public void die () {
        //
    }
}
