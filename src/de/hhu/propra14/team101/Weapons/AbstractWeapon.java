package de.hhu.propra14.team101.Weapons;

import de.hhu.propra14.team101.Bullet;
import de.hhu.propra14.team101.Physics;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to supply weapons
 */

abstract public class AbstractWeapon {
    /** Name of the weapon */
    public String name;
    /** Damage of the weapon */
    public double damage;
    /** Radius of the weapon */
    public double radius;
    /** Graphic of the weapon */
    public Image image;

    abstract public Bullet fire (Physics physics);

    public Map serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", this.name);
        data.put("damage", this.damage);
        data.put("radius", this.radius);
        return data;
    }

    public static AbstractWeapon deserialize(Map input) {
        if (input.get("name").toString().equals("Atomic bomb")) {
            return new AtomicBomb();
        } else if (input.get("name").toString().equals("Bazooka")) {
            return new Bazooka();
        } else if (input.get("name").toString().equals("Grenade")) {
            return new Grenade();
        } else {
            System.out.println("AbstractWeapon.deserialize: Unknown weapon");
            return null;
        }
    }
}
