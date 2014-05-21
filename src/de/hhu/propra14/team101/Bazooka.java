package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;

public class Bazooka extends Weapons {

    public Bazooka() {
        this.name = "Bazooka";
        this.damage = 10;
        this.radius = 5.5;
    }

    public Bullet fire(int[][] path) {
        return new Bullet(path, this);
    }
}
