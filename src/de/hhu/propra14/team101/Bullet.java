package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Class for a flying bullet
 */
public class Bullet {
    /** Two-dimensional path, first dimension is time, second are coordinates ([0] is x, [1] is y) */
    public int[][] path;

    /** What weapon we are */
    public Weapons weapon;

    /** What time of path we are at now */
    public int currentTime = 0;


    /**
     * Contructs a bullet
     *
     */
    public Bullet(int[][] path, Weapons weapon) {
        this.path = path;
        this.weapon = weapon;
    }

    /**
     * Move the bullet
     * @param gc Canvas to draw on
     * @param game Game to check for collisions
     */
    public void move (GraphicsContext gc, Game game) {
        // TODO check for collisions
        this.draw(gc);
    }

    private void draw (GraphicsContext gc) {
        gc.drawImage(this.weapon.image, this.path[currentTime][0], this.path[currentTime][1]);
    }
}
