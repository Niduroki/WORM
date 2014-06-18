package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Main;
import de.hhu.propra14.team101.Physics.BallisticMovement;
import javafx.scene.image.Image;
import org.newdawn.easyogg.OggClip;

import java.io.IOException;

public class Grenade extends AbstractWeapon {


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

    public Bullet fire(BallisticMovement physics) {
        if (!Main.headless)
            try {
                OggClip doThisClip = new OggClip("sfx/weapons/Click.ogg");
                doThisClip.play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        {
            return new Bullet(physics, this);
        }
    }
}
