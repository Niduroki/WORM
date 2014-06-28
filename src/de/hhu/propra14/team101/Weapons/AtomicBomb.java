package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Main;
import de.hhu.propra14.team101.Physics.BallisticMovement;
import javafx.scene.image.Image;
import org.newdawn.easyogg.OggClip;

import java.io.IOException;

/**
 * Represent a atomic bomb.
 * <pre>
 * {@code
 * AtomicBomb weapon = AtomicBomb();
 * Bullet bullet = weapon.fire(movement);
 * }
 * </pre>
 */
public class AtomicBomb extends AbstractWeapon {

    /**
     * Initialize a new atomic bomb.
     */
    public AtomicBomb() {
        this.name = "Atomic bomb";
        this.damage = 25;
        this.radius = 20.5;
        this.weight = 1;
        if (!Main.headless) {
            this.weaponImage = new Image("images/AtomicBomb.png");
            this.bulletImage = new Image("images/AtomicBomb.png");
        }
    }

    /**
     * Use this weapon and create a bullet.
     * @param physics BallisticMovement, which describe the movement path of bullet.
     * @return bullet of the shoot
     */
    public Bullet fire(BallisticMovement physics) {

        if (!Main.headless) {
            try {
                OggClip doThisClip = new OggClip("sfx/weapons/Rocket firing.ogg");
                doThisClip.setGain(Main.svol);
                doThisClip.play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Bullet(physics, this);
    }

}