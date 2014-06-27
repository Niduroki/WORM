package de.hhu.propra14.team101.Physics;

/**
 * Define types of a detected collision.
 */
public enum CollisionType {
    /**
     * collision with a worm
     */
    Worm,
    /**
     * collision with top or bottom border of the screen
     */
    TopOrDown,
    /**
     * collision with left or right border of the screen
     */
    LeftOrRight,
    /**
     * collision with the terrain.
     */
    Terrain
}
