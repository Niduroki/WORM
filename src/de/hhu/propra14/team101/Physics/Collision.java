package de.hhu.propra14.team101.Physics;

public class Collision {
    private Object collisionElement;
    private CollisionType collisionType;

    public Collision(Object collisionElement, CollisionType type) {
        this.collisionElement = collisionElement;
        this.collisionType = type;
    }

    public Object getCollisionElement() {
        return this.collisionElement;
    }

    public CollisionType getType() {
        return this.collisionType;
    }
}