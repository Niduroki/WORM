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
    /** Weight of the weapons bullet */
    public double weight;
    /** Graphic of the weapon itself */
    public Image weaponImage;
    /** Graphic of the weapons bullet */
    public Image bulletImage;


    abstract public Bullet fire (Physics physics);

    public Map serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", this.name);
        return data;
    }

    public static AbstractWeapon deserialize(Map input) {
        String weaponName = input.get("name").toString();
        switch (weaponName) {
            case "Atomic bomb":
                return new AtomicBomb();
            case "Bazooka":
                return new Bazooka();
            case "Grenade":
                return new Grenade();
            default:
                System.out.println("AbstractWeapon.deserialize: Unknown weapon");
                return null;
        }
    }
}
