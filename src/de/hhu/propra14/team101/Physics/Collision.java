package de.hhu.propra14.team101.Physics;

public class Collision {
    private Object collisionElement;
    private CollisionType collisionType;

    /**
     *
     * @param collisionElement
     * @param type
     */
    public Collision(Object collisionElement, CollisionType type) {
        this.collisionElement = collisionElement;
        this.collisionType = type;
    }

    /**
     *
     * @return
     */
    public Object getCollisionElement() {
        return this.collisionElement;
    }

    /**
     *
     * @return
     */
    public CollisionType getType() {
        return this.collisionType;
    }
}