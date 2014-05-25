package de.hhu.propra14.team101;

/**
 * Created by danie_000 on 2014-05-25.
 */
public class Collision {
    private Object collisionElement;
    private CollisionType collisionType;

    public Collision(Object collisionElement, CollisionType type) {
        this.collisionElement = collisionElement;
        collisionType = type;
    }

    public Object getCollisionElement() {
        return collisionElement;
    }

    public CollisionType getType() {
        return collisionType;
    }
}