package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Weapons.AbstractWeapon;

public class Bazooka extends AbstractWeapon {

    public Bazooka() {
        this.name = "Bazooka";
        this.damage = 10;
        this.radius = 5.5;
    }

    public Bullet fire(int[][] path) {
        return new Bullet(path, this);
    }
}
