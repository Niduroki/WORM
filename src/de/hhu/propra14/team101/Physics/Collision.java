package de.hhu.propra14.team101.Physics;

/**
 * Represents a detected collision.
 * <p/>
 * Code example:
 * <pre>
 * {@code
 * //When a collision was detected, then:
 * Worm worm = new Worm(weaponList);
 * Collision collision = new Collision(worm, CollisionType.Worm);
 * }
 * </pre>
 */
public class Collision {
    private Object collisionElement;
    private CollisionType collisionType;

    /**
     *  Initialize a new collision.
     * @param collisionElement element, which collided with moved element.
     * @param type type of the collision.
     */
    public Collision(Object collisionElement, CollisionType type) {
        this.collisionElement = collisionElement;
        this.collisionType = type;
    }

    /**
     * Gets element, which collided with moved element.
     * @return collision element.
     */
    public Object getCollisionElement() {
        return this.collisionElement;
    }

    /**
     * Gets type of the collision.
     * @return type of the collision
     */
    public CollisionType getType() {
        return this.collisionType;
    }
}