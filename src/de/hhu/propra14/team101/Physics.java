package de.hhu.propra14.team101;


import java.util.ArrayList;

public class Physics {
    private Vector directionVector;
    private double xCoordinate;
    private double yCoordinate;

    public Physics(double startPosX, double startPosY, double posXDirection, double posYDirection)
    {
        directionVector = new Vector(startPosX, startPosY, posXDirection, posYDirection);
        directionVector.normalize();
        xCoordinate = startPosX;
        yCoordinate = startPosY;
    }

    public void move()
    {
        xCoordinate = this.getXCoordinate()+directionVector.getXCoordinate();
        yCoordinate = this.getYCoordinate()+directionVector.getYCoordinate();
    }

    public Worm hasCollision(Worm currentWorm, ArrayList<Worm> worms) {
        for(Worm worm: worms) {
            if(worm != currentWorm && worm.isHitted(xCoordinate, yCoordinate)) {
                return worm;
            }
        }
        return null;
    }

    public double getXCoordinate(){
        return (xCoordinate);
    }

    public double getYCoordinate(){
        return (yCoordinate);
    }

}
