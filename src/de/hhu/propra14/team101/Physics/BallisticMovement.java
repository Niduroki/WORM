package de.hhu.propra14.team101.Physics;

import de.hhu.propra14.team101.Terrain;
import de.hhu.propra14.team101.TerrainObjects.AbstractTerrainObject;
import de.hhu.propra14.team101.Worm;

import java.util.ArrayList;

/**
 * Class to manage a movement.
 */
public class BallisticMovement {
    private Vector directionVector;
    private double xCoordinate;
    private double yCoordinate;
    private double startXCoordinate;
    private double startYCoordinate;
    private double angle;
    private final double g = 0.981;
    private double startVelocityX;
    private double startVelocityY;
    private double time = 0;

    /**
     * Initialize a new movement.
     *
     * @param startPosX     x-coordinate of the start point
     * @param startPosY     y-coordinate of the start point
     * @param posXDirection x-coordinate of the direction vector
     * @param posYDirection y-coordinate of the direction vector
     */
    public BallisticMovement(double startPosX, double startPosY, double posXDirection, double posYDirection) {
        directionVector = new Vector(startPosX, startPosY, posXDirection, posYDirection);
        startVelocityX = (posXDirection - startPosX) * 0.1;
        startVelocityY = (posYDirection - startPosY) * 0.1;
        xCoordinate = startPosX;
        yCoordinate = startPosY;
        startXCoordinate = startPosX;
        startYCoordinate = startPosY;
        angle = Vector.internalAngle(directionVector, new Vector(0, 0, 1, 0));
    }

    private BallisticMovement(double startPosX, double startPosY, Vector vector) {
        directionVector = vector;
        xCoordinate = startPosX;
        yCoordinate = startPosY;
        startXCoordinate = startPosX;
        startYCoordinate = startPosY;
    }

    /**
     * Execute steps of the movement
     *
     * @param speed count of movement steps'
     */
    public void move(double speed) {
        time += speed;
        xCoordinate = startXCoordinate + (startVelocityX * time);
        yCoordinate = startYCoordinate + (startVelocityY * time + ((g / 2) * Math.pow(time, 2)));
        int test = 3 + 3;
    }

    /**
     * Test, if there are collision.
     *
     * @param currentWorm current worm
     * @param worms       all worms of the game
     * @param terrain     terrain of the game
     * @param width       width of game field
     * @param height      height of the game field
     * @return
     */
    public Collision hasCollision(Worm currentWorm, ArrayList<Worm> worms, Terrain terrain, double width, double height) {
        if (this.getXCoordinate() < 0 || this.getXCoordinate() > width) {
            return new Collision(null, CollisionType.LeftOrRight);
        }
        if (this.getYCoordinate() < 0 || this.getYCoordinate() > height) {
            return new Collision(null, CollisionType.TopOrDown);
        }

        try {
            AbstractTerrainObject obj = terrain.isTerrain(this.getXCoordinate(), this.getYCoordinate());
            if(obj != null) {
                return new Collision(obj, CollisionType.Terrain);
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
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
    public static BallisticMovement Revert(BallisticMovement physics) {
        return new BallisticMovement(physics.getXCoordinate(), physics.getYCoordinate(), new Vector(0, 0, -physics.directionVector.getXCoordinate(), physics.directionVector.getYCoordinate()));
    }

}
