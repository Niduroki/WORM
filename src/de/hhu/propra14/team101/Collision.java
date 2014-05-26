package de.hhu.propra14.team101;

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