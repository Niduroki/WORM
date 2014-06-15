package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Main;
import de.hhu.propra14.team101.Physics.BallisticMovement;
import javafx.scene.image.Image;

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
        return new Bullet(physics, this);
    }}

