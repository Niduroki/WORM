package de.hhu.propra14.team101.Physics;

/**
 * Represents a 2d vector.
 *
 * Code example:
 * <pre>
 * {@code
 * //Initialize a new Vector
 * Vector vector1 = new Vector(0, 0, 1, 1);
 * System.out.println(vector1.getXCoordinate());
 * System.out.println(vector1.getYCoordinate());
 * vector1.normalize();
 * System.out.println(vector1.length());
 * System.out.println(vector1.getXCoordinate());
 * System.out.println(vector1.getYCoordinate());
 * System.out.println(Vector.internalAngle(vector1, new Vector(1, 0, 0, 0)));
 *
 * //==Output==
 * 1.0
 * 1.0
 * 0.9999999999999999
 * 0.7071067811865475
 * 0.7071067811865475
 * 2.356194490192345
 * }</pre>
 */
public class Vector {
    private double xCoordinate;
    private double yCoordinate;

    /**
     * Initialize a new vector.
     *
     * @param startPosX     x coordinate of initial point
     * @param startPosY     y coordinate of initial point
     * @param PosXDirection x coordinate of terminal point
     * @param PosYDirection y coordinate of terminal point
     */
    public Vector(double startPosX, double startPosY, double PosXDirection, double PosYDirection) {
        this.xCoordinate = PosXDirection - startPosX;
        this.yCoordinate = PosYDirection - startPosY;
    }

    /**
     * Gets the x coordinate.
     *
     * @return value of x coordinate
     */
    public double getXCoordinate() {
        return this.xCoordinate;
    }

    /**
     * Get the y coordinate
     * @return Y Coordinate
     */
    public double getYCoordinate() {
        return this.yCoordinate;
    }

    /**
     * Normalize the vector.
     */
    public void normalize() {
        double length = this.length();
        this.xCoordinate = this.xCoordinate / length;
        this.yCoordinate = this.yCoordinate / length;
    }

    /**
     * Gets the length of the vector.
     *
     * @return length value
     */
    public double length() {
        return Math.sqrt(Math.pow(this.xCoordinate, 2) + Math.pow(this.yCoordinate, 2));
    }

    /**
     * Calculate the internal angel of two vectors.
     *
     * @param vector1 first vector
     * @param vector2 second vector
     * @return angel in radians
     */
    public static double internalAngle(Vector vector1, Vector vector2) {
        return Math.acos(
                (vector1.getXCoordinate() * vector2.getXCoordinate() + vector1.getYCoordinate() * vector2.getYCoordinate()) /
                        vector1.length() * vector2.length()
        );
    }
}