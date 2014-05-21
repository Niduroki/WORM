package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
        // We're out of the playfield
        if (this.currentTime == this.path.length) {
            return true;
        }

        boolean collision = false;
        for (int i=0; i<game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);
            for (int j=0; j<player.wormList.size(); j++) {
                Worm currentWorm = player.wormList.get(j);
                if (
                        Physics.checkCollision(
                                currentWorm.getXCoordinate(),
                                currentWorm.getYCoordinate(),
                                currentWorm.size,
                                this.path[currentTime][0],
                                this.path[currentTime][1],
                                5
                        )
                ) {
                    collision = true;
                    currentWorm.health -= (int) this.weapon.damage;
                }
            }
        }

        if (collision) {
            return true;
        } else {
            this.draw(gc);
            this.currentTime += 1;
            return false;
        }
    }

    private void draw (GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(this.path[currentTime][0], this.path[currentTime][1], 5, 5);
        //gc.drawImage(this.weapon.image, this.path[currentTime][0], this.path[currentTime][1]);
    }
}
