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

    public int armor = 0;

    public int health = 100;

    protected int x_coord = 0;

    protected int y_coord = 0;

    public Worm () {
        this.WeaponArray = new Weapons[3];
        WeaponArray[0] = new Bazooka();
        WeaponArray[1] = new AtomicBomb();
        WeaponArray[2] = new Grenade();
    }

    public int getXCoordinate()
    {
        return x_coord;
    }

    public void setXCoordinate(int xCoordinate)
    {
        x_coord = xCoordinate;
    }

    public int getYCoordinate()
    {
        return y_coord;
    }

    public void setYCoordinate(int yCoordinate)
    {
        y_coord = yCoordinate;
    }

    /**
     * @param gc Canvas to draw on
     * Draws the worm
     */
    public void draw (GraphicsContext gc) {
        Image image = new Image("resources/wurm.gif");
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
