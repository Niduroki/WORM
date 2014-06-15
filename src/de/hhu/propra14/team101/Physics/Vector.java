package de.hhu.propra14.team101.Physics;

public class Vector {
    private double xCoordinate;
    private double yCoordinate;

    public Vector(double startPosX, double startPosY, double PosXDirection, double PosYDirection) {
        this.xCoordinate = PosXDirection - startPosX;
        this.yCoordinate = PosYDirection - startPosY;
    }

    public double getXCoordinate() {
        return this.xCoordinate;
    }

    public double getYCoordinate() {
        return this.yCoordinate;
    }

    public void normalize() {
        double length = this.length();
        this.xCoordinate = this.xCoordinate / length;
        this.yCoordinate = this.yCoordinate / length;
    }

    public double length() {
        return Math.sqrt(Math.pow(this.xCoordinate, 2) + Math.pow(this.yCoordinate, 2));
    }

    public static double internalAngle(Vector vector1, Vector vector2) {
        return Math.acos(
                (vector1.getXCoordinate()*vector2.getXCoordinate()+vector1.getYCoordinate()*vector2.getYCoordinate())/
                vector1.length()*vector2.length()
        );
    }
}

