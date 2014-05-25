package de.hhu.propra14.team101;
/**
 * Created by danie_000 on 2014-05-23.
 */
public class Vector {
    private double xCoordinate;
    private double yCoordinate;

    public Vector(double startPosX, double startPosY, double PosXDirection, double PosYDirection) {
        xCoordinate = PosXDirection - startPosX;
        yCoordinate = PosYDirection - startPosY;
    }

    public double getXCoordinate()
    {
        return xCoordinate;
    }

    public double getYCoordinate() {
        return yCoordinate;
    }

    public void normalize() {
        double value = Math.sqrt(Math.pow(this.getXCoordinate(), 2) + Math.pow(this.getYCoordinate(), 2));
        xCoordinate = xCoordinate / value;
        yCoordinate = yCoordinate / value;
    }
}

