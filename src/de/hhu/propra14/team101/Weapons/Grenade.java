package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Weapons.AbstractWeapon;

public class Grenade extends AbstractWeapon {

    public Grenade() {
        this.name = "Grenade";
        this.damage = 20;
        this.radius = 3.0;
    }

    public Bullet fire(int[][] path) {
        return new Bullet(path, this);
    }
}

