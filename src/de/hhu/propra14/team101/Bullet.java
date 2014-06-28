package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Physics.BallisticMovement;
import de.hhu.propra14.team101.Weapons.AbstractWeapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class for a flying bullet
 * {@code
 * AtomicBomb weapon = AtomicBomb();
 * Bullet bullet = weapon.fire(movement);
 * bullet.draw(graphics);
 * }
 */
public class Bullet {
    /**
     * describe path of movement
     */
    public BallisticMovement physics;

    /**
     * What weapon we are
     */
    public AbstractWeapon weapon;

    /**
     * Constructs a bullet
     *
     * @param physics physics for the weapon
     * @param weapon  weapon that's fired
     */
    public Bullet(BallisticMovement physics, AbstractWeapon weapon) {
        this.physics = physics;
        this.weapon = weapon;
    }

    /**
     * Draws bullet.
     *
     * @param gc graphics context to draw
     */
    public void draw(GraphicsContext gc) {
        if (this.weapon.bulletImage != null) {
            gc.drawImage(this.weapon.bulletImage, physics.getXCoordinate(), physics.getYCoordinate(), 5 * Main.sizeMultiplier, 5 * Main.sizeMultiplier);
        } else {
            gc.setFill(Color.RED);
            gc.fillRect(physics.getXCoordinate(), physics.getYCoordinate(), 5, 5);
        }
    }
}