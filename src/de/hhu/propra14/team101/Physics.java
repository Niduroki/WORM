package de.hhu.propra14.team101;


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

    public double getXCoordinate(){
        return (xCoordinate);
    }

    public double getYCoordinate(){
        return (yCoordinate);
    }

}
