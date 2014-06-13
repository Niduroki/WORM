package de.hhu.propra14.team101.Physics;

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

    public double length() {
        return (Math.sqrt(Math.pow(this.getXCoordinate(),2)+Math.pow(this.getYCoordinate(),2)));
    }

    public static double internalAngle(Vector vector1, Vector vector2) {
        return Math.acos((vector1.getXCoordinate()*vector2.getXCoordinate()+vector1.getYCoordinate()*vector2.getYCoordinate())/ vector1.length()*vector2.length());
    }
}

