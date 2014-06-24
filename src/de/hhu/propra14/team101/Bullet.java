package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Physics.BallisticMovement;
import de.hhu.propra14.team101.Weapons.AbstractWeapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class for a flying bullet
 */
public class Bullet {
    /** */
    public BallisticMovement physics;

    /** What weapon we are */
    public AbstractWeapon weapon;

    /** What time of path we are at now */
    public int currentTime = 0;

    /**
     * Constructs a bullet
     * @param physics physics for the weapon
     * @param weapon weapon that's fired
     */
    public Bullet(BallisticMovement physics, AbstractWeapon weapon) {
        this.physics = physics;
        this.weapon = weapon;
    }

    /**
     *
      * @return
     */
    public double getXCoordinate() {
       return physics.getXCoordinate();
    }

    /**
     *
     * @return
     */
    public double getYCoordinate() {
        return physics.getYCoordinate();
    }

    /**
     *
     * @param gc
     */
    public void draw (GraphicsContext gc) {
        if (this.weapon.bulletImage != null) {
            gc.drawImage(this.weapon.bulletImage, physics.getXCoordinate(), physics.getYCoordinate(), 5*Main.sizeMultiplier, 5*Main.sizeMultiplier);
        } else {
            gc.setFill(Color.RED);
            gc.fillRect(physics.getXCoordinate(), physics.getYCoordinate(), 5, 5);
        }
    }
}
