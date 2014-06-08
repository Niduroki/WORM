package de.hhu.propra14.team101;

import java.util.ArrayList;

/**
 * Class to manage a movement.
 */
public class Physics {
    private Vector directionVector;
    private double xCoordinate;
    private double yCoordinate;

    /**
     * Initialize a new movement.
     * @param startPosX x-coordinate of the start point
     * @param startPosY y-coordinate of the start point
     * @param posXDirection x-coordinate of the direction vector
     * @param posYDirection y-coordinate of the direction vector
     */
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

    /**
     * Execute steps of the movement
     * @param speed count of movement steps'
     */
    public void move(int speed) {
        for(int step = 0; step < speed; step++) {
            xCoordinate = this.getXCoordinate() + directionVector.getXCoordinate();
            yCoordinate = this.getYCoordinate() + directionVector.getYCoordinate();
        }
    }

    /**
     * Test, if there are collision.
     * @param currentWorm current worm
     * @param worms all worms of the game
     * @param terrain terrain of the game
     * @param width width of game field
     * @param height height of the game field
     * @return
     */
    public Collision hasCollision(Worm currentWorm, ArrayList<Worm> worms, Terrain terrain, double width, double height) {
        if (this.getXCoordinate() < 0 || this.getXCoordinate() > width) {
            return new Collision(null, CollisionType.LeftOrRight);
        }
        if (this.getYCoordinate() < 0 || this.getYCoordinate() > height) {
            return new Collision(null, CollisionType.TopOrDown);
        }

        if(terrain.isTerrain(new Double(this.getXCoordinate()/10).intValue(), new Double(this.getYCoordinate()/10).intValue())) {
            //TODO: remove array
            try {
                int[] obj = {new Double(this.getXCoordinate()).intValue(), new Double(this.getYCoordinate()).intValue()};
                return new Collision(obj, CollisionType.Terrain);
            } catch(IllegalArgumentException ex) {
             System.out.println(ex.getMessage());
            }
        }

        for (Worm worm : worms) {
            if (worm != currentWorm && worm.isHitted(this.getXCoordinate(), this.getYCoordinate())) {
                return new Collision(worm, CollisionType.Worm);
            }
        }
        return null;
    }

    /**
     * Get the x-coordinate
     */
    public double getXCoordinate() {
        return (xCoordinate);
    }

    /**
     * Get the y-coordinate
     */
    public double getYCoordinate() {
        return (yCoordinate);
    }

    /**
     * Revert the given movement.
     */
    public static Physics Revert(Physics physics) {
        return new Physics(physics.getXCoordinate(), physics.getYCoordinate(), new Vector(0, 0, -physics.directionVector.getXCoordinate(), physics.directionVector.getYCoordinate()));
    }

}
