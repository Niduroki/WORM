package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;

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

    abstract public void fire ();

    abstract public void draw (GraphicsContext gc);

    public Map serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", this.name);
        data.put("damage", this.damage);
        data.put("radius", this.radius);
        return data;
    }

    public static Weapons deserialize(Map input) {
        // TODO
        return new Weapons() {
            @Override
            public void fire() {

            }

            @Override
            public void draw(GraphicsContext gc) {

            }
        };
    }
}
