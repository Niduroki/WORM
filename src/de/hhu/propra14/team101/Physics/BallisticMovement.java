package de.hhu.propra14.team101.Physics;

import de.hhu.propra14.team101.Terrain;
import de.hhu.propra14.team101.TerrainObjects.AbstractTerrainObject;
import de.hhu.propra14.team101.Worm;

import java.util.ArrayList;

/**
 * Class to manage ballistic movement
 */
public class BallisticMovement {
    private Vector directionVector;
    private double xCoordinate;
    private double yCoordinate;
    private double startXCoordinate;
    private double startYCoordinate;
    private final double g = 0.981;
    private double startVelocityX;
    private double startVelocityY;
    private double time = 0;
    private boolean finished = false;
    private boolean autoStop;

    /**
     * Initialize a new movement
     *
     * @param startPosX     x-coordinate of the start point
     * @param startPosY     y-coordinate of the start point
     * @param posXDirection x-coordinate of the direction vector
     * @param posYDirection y-coordinate of the direction vector
     * @param autoStop Whether movement should stop automatically
     */
    public BallisticMovement(double startPosX, double startPosY, double posXDirection, double posYDirection, boolean autoStop) {
        directionVector = new Vector(startPosX, startPosY, posXDirection, posYDirection);
        startVelocityX = (posXDirection - startPosX) * 0.1;
        startVelocityY = (posYDirection - startPosY) * 0.1;
        xCoordinate = startPosX;
        yCoordinate = startPosY;
        startXCoordinate = startPosX;
        startYCoordinate = startPosY;
        this.autoStop = autoStop;
    }

    private BallisticMovement(double startPosX, double startPosY, Vector vector) {
        directionVector = vector;
        xCoordinate = startPosX;
        yCoordinate = startPosY;
        startXCoordinate = startPosX;
        startYCoordinate = startPosY;
    }

    /**
     * Gets a value indicating whether movement is finished.
     * @return true, if the movement is finished, otherwise false.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Execute steps of the movement
     * @param speed count of movement steps
     * @return detected collision
     */
    public Collision move(double speed, Worm currentWorm, ArrayList<Worm> worms, Terrain terrain, double width, double height) {
        if(!finished) {
            double stepSpeed = speed / 10;
            for(int i = 0;i < 10;i++)
            {
                time += stepSpeed;
                xCoordinate = startXCoordinate + (startVelocityX * time);
                yCoordinate = startYCoordinate + (startVelocityY * time + ((g / 2) * Math.pow(time, 2)));
                Collision collision = hasCollision(currentWorm,worms, terrain,width,height);
                if(collision != null) {
                    time -= stepSpeed;
                    xCoordinate = startXCoordinate + (startVelocityX * time);
                    yCoordinate = startYCoordinate + (startVelocityY * time + ((g / 2) * Math.pow(time, 2)));
                    return collision;
                }
            }

            if(autoStop) {
                if(yCoordinate > startYCoordinate) finished = true;
            }
       }
        return null;
    }

    /**
     * Test, if there is a collision
     *
     * @param currentWorm current worm
     * @param worms       all worms of the game
     * @param terrain     terrain of the game
     * @param width       width of game field
     * @param height      height of the game field
     * @return Collision
     */
    private Collision hasCollision(Worm currentWorm, ArrayList<Worm> worms, Terrain terrain, double width, double height) {
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
     * Gets the x-coordinate.
     * @return x coordinate
     */
    public double getXCoordinate() {
        return (xCoordinate);
    }

    /**
     * Gets the y-coordinate
     * @return y coordinate
     */
    public double getYCoordinate() {
        return (yCoordinate);
    }

    /**
     * Revert the given movement.
     * @param physics Physics to revert
     * @return Reverted movement
     */
    public static BallisticMovement revert(BallisticMovement physics) {
        return new BallisticMovement(
                physics.getXCoordinate(), physics.getYCoordinate(),
                new Vector(0, 0, -physics.directionVector.getXCoordinate(), physics.directionVector.getYCoordinate())
        );
    }
}
