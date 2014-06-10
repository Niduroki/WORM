package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Weapons.AbstractWeapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class for a flying bullet
 */
public class Bullet {

    public  Physics physics;

    /** What weapon we are */
    public AbstractWeapon weapon;

    /** What time of path we are at now */
    public int currentTime = 0;

    /**
     * Contructs a bullet
     * @param physics physics for the weapon
     * @param weapon weapon that's fired
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
        if (this.weapon.bulletImage != null) {
            gc.drawImage(this.weapon.bulletImage, physics.getXCoordinate(), physics.getYCoordinate(), 5, 5);
        } else {
            gc.setFill(Color.RED);
            gc.fillRect(physics.getXCoordinate(), physics.getYCoordinate(), 5, 5);
        }
    }
}
