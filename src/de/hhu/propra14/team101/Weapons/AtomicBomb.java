package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Weapons.AbstractWeapon;

public class AtomicBomb extends AbstractWeapon {

    public AtomicBomb() {
        this.name = "Atomic bomb";
        this.damage = 25;
        this.radius = 20.5;
    }

    public Bullet fire(int[][] path) {
        return new Bullet(path, this);
    }
}

