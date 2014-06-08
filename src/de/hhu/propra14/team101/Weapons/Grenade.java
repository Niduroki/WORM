package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Main;
import de.hhu.propra14.team101.Physics;
import javafx.scene.image.Image;

public class Grenade extends AbstractWeapon {


    public Grenade() {
        this.name = "Grenade";
        this.damage = 20;
        this.radius = 3.0;
        if (!Main.headless) {
            this.image = new Image("grenade.png");
        }
    }

    public Bullet fire(Physics physics) {
        return new Bullet(physics, this);
    }}

