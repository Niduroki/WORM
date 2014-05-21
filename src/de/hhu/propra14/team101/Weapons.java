package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to supply weapons
 */

abstract public class Weapons {
    /** Name of the weapon */
    public String name;
    /** Damage of the weapon */
    public double damage;
    /** Radius of the weapon */
    public double radius;
    /** Graphic of the weapon */
    public Image image;

    abstract public Bullet fire (int[][] path);

    public Map serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", this.name);
        data.put("damage", this.damage);
        data.put("radius", this.radius);
        return data;
    }

    public static Weapons deserialize(Map input) {
        if (input.get("name") == "Atomic Bomb") {
            return new AtomicBomb();
        } else if (input.get("name") == "Bazooka") {
            return new Bazooka();
        } else if (input.get("name") == "Grenade") {
            return new Grenade();
        } else {
            System.out.println("Weapons.deserialize: Unknown weapon");
            return new Weapons() {
                @Override
                public Bullet fire(int[][] path) {
                    return new Bullet(path, this);
                }
            };
        }
    }
}
