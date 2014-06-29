package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Main;
import de.hhu.propra14.team101.Physics.BallisticMovement;
import javafx.scene.image.Image;
import org.newdawn.easyogg.OggClip;

import java.io.IOException;

/**
 * Represent a grenade.
 * <p>
 * Code example:
 * <pre>
 * {@code
 * Grenade weapon = Grenade();
 * Bullet bullet = weapon.fire(movement);
 * }
 * </pre> unnecessary
 */
public class Grenade extends AbstractWeapon {

    /**
     * Initialize a new grenade.
     */
    public Grenade() {
        this.name = "Grenade";
        this.damage = 20;
        this.radius = 3.0;
        this.weight = 1;
        if (!Main.headless) {
            this.weaponImage = new Image("images/Grenade.png");
            this.bulletImage = new Image("images/Grenade.png");
        }
    }

    /**
     * Use this weapon and create a bullet.
     *
     * @param physics BallisticMovement, which describe the movement path of bullet.
     * @return bullet of the shoot
     */
    public Bullet fire(BallisticMovement physics) {
        if (!Main.headless) {
            try {
                OggClip doThisClip = new OggClip("sfx/weapons/Click.ogg");
                doThisClip.setGain(Main.svol);
                doThisClip.play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Bullet(physics, this);
    }
}
