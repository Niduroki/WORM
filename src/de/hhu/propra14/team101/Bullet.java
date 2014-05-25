package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Weapons.AbstractWeapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class for a flying bullet
 */
public class Bullet {
    /** Two-dimensional path, first dimension is time, second are coordinates ([0] is x, [1] is y) */
    public  Physics physics;

    /** What weapon we are */
    public AbstractWeapon weapon;

    /** What time of path we are at now */
    public int currentTime = 0;

    private double xCoordinate;
    private double yCoordinate;

    /**
     * Contructs a bullet
     *
     */
    public Bullet(Physics physics, AbstractWeapon weapon) {
        this.physics = physics;
        this.weapon = weapon;
    }

   public double getXCoordinate() {
      return physics.getXCoordinate();
   }

   public double getYCoordinate() {
       return physics.getYCoordinate();
   }

    public void draw (GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(physics.getXCoordinate(), physics.getYCoordinate(), 5, 5);
        //gc.drawImage(this.weapon.image, this.path[currentTime][0], this.path[currentTime][1]);
    }
}
