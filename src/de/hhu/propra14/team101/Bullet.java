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
     * @return whether the bullet hit something and should be removed
     */
    public boolean move (GraphicsContext gc, Game game) {
        for (int i=0; i<game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);
            for (int j=0; j<player.wormList.size(); j++) {
                // TODO check for collisions
            }
        }
        boolean collision = false;
        if (collision) {
            // TODO Do damage, destroy terrain, etc.
            return true;
        } else {
            this.draw(gc);
            this.currentTime += 1;
            return false;
        }
    }

    private void draw (GraphicsContext gc) {
        gc.drawImage(this.weapon.image, this.path[currentTime][0], this.path[currentTime][1]);
    }
}
