package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Main;
import de.hhu.propra14.team101.Physics.BallisticMovement;
import javafx.scene.image.Image;
import org.newdawn.easyogg.OggClip;

import java.io.IOException;

public class Bazooka extends AbstractWeapon {

    /**
     *
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
     *
     * @param physics
     * @return
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