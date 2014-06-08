package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Physics;
import javafx.scene.image.Image;

public class AtomicBomb extends AbstractWeapon {
    public Image image = new Image("atomicbomb.png");
    public AtomicBomb() {
        this.name = "Atomic bomb";
        this.damage = 25;
        this.radius = 20.5;
    }

    public Bullet fire(Physics physics) {
        return new Bullet(physics, this);
    }
}

