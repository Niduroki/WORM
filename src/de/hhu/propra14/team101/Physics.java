package de.hhu.propra14.team101;


import java.util.ArrayList;

public class Physics {
    private Vector directionVector;
    private double xCoordinate;
    private double yCoordinate;

    public Physics(double startPosX, double startPosY, double posXDirection, double posYDirection) {
        directionVector = new Vector(startPosX, startPosY, posXDirection, posYDirection);
        directionVector.normalize();
        xCoordinate = startPosX;
        yCoordinate = startPosY;
    }

    private Physics(double startPosX, double startPosY, Vector vector) {
        directionVector = vector;
        directionVector.normalize();
        xCoordinate = startPosX;
        yCoordinate = startPosY;
    }

    public void move() {
        xCoordinate = this.getXCoordinate() + directionVector.getXCoordinate();
        yCoordinate = this.getYCoordinate() + directionVector.getYCoordinate();
    }

    public Collision hasCollision(Worm currentWorm, ArrayList<Worm> worms, double width, double height) {
        if (this.getXCoordinate() < 0 || this.getXCoordinate() > width) {
            return new Collision(null, CollisionType.LeftOrRight);
        }
        if (this.getYCoordinate() < 0 || this.getYCoordinate() > height) {
            return new Collision(null, CollisionType.TopOrDown);
        }

        for (Worm worm : worms) {
            if (worm != currentWorm && worm.isHitted(this.getXCoordinate(), this.getYCoordinate())) {
                return new Collision(worm, CollisionType.Worm);
            }
        }
        return null;
    }

    public double getXCoordinate() {
        return (xCoordinate);
    }

    public double getYCoordinate() {
        return (yCoordinate);
    }

    public static Physics Revert(Physics physics) {
        return new Physics(physics.getXCoordinate(), physics.getYCoordinate(), new Vector(0, 0, -physics.directionVector.getXCoordinate(), physics.directionVector.getYCoordinate()));
    }

}
