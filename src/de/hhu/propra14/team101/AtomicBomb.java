package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;

public class AtomicBomb extends Weapons {

    public AtomicBomb() {
        this.name = "Atomic bomb";
        this.damage = 25;
        this.radius = 20.5;
    }

    public Bullet fire(int[][] path) {
        return new Bullet(path, this);
    }
}

