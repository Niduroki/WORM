package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Main;
import de.hhu.propra14.team101.Physics.BallisticMovement;
import javafx.scene.image.Image;
import org.newdawn.easyogg.OggClip;

import java.io.IOException;

/**
 Represent a bazooka.
 <p/>
 Code example:
 * <pre>
 * {@code
 * Bazooka weapon = Bazooka();
 * Bullet bullet = weapon.fire(movement);
 * }
 * </pre>
 */
public class Bazooka extends AbstractWeapon {

    /**
     * Initialize a new bazooka.
     */
    public Bazooka() {
        this.name = "Bazooka";
        this.damage = 10;
        this.radius = 5.5;
        this.weight = 1;
        if (!Main.headless) {
            this.weaponImage = new Image("images/Bazooka.png");
            this.bulletImage = new Image("images/Bazooka.png");
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
                OggClip doThisClip = new OggClip("sfx/weapons/Bazooka fire.ogg");
                doThisClip.setGain(Main.svol);
                doThisClip.play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Bullet(physics, this);
    }
}