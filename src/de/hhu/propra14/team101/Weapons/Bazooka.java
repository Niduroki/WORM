package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Main;
import de.hhu.propra14.team101.Physics;
import javafx.scene.image.Image;

public class Bazooka extends AbstractWeapon {


    public Bazooka() {
        this.name = "Bazooka";
        this.damage = 10;
        this.radius = 5.5;
        if (!Main.headless) {
            this.image = new Image("Bazooka.png");
        }
    }

    public Bullet fire(Physics physics) {
        return new Bullet(physics, this);
    }
}
