package de.hhu.propra14.team101;

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
}
