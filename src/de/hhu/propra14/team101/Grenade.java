package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;

public class Grenade extends Weapons {

    public Grenade() {
        this.name = "Grenade";
        this.damage = 20;
        this.radius = 3.0;
    }

    public Bullet fire(int[][] path) {
        return new Bullet(path, this);
    }
}

