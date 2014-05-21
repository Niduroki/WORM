package de.hhu.propra14.team101;

import java.util.Vector;

public class Physics {
    public static int[][] calcLinearPath(int[] start, int[] target) {
        //Vector<int[]> vector = new Vector<>();
        int[][] path = new int[2][2];
        path[0][0] = start[0];
        path[0][1] = start[1];
        path[1][0] = target[0];
        path[1][1] = target[1];
        return path;
    }

    public static boolean checkCollision(int targetX, int targetY, int targetSize, int movingX, int movingY, int movingSize) {
        // Moving is in target
        if (movingX >= targetX && movingX <= targetX+targetSize && movingY >= targetY && movingY <= targetY+targetSize) {
            return true;
        } else
        // Top-left of moving touches top of target
        if (movingX >= targetX && movingX <= targetX+targetSize && movingY == targetY) {
            return true;
        } else
        // Top-right of moving touches top of target
        if (movingX+movingSize >= targetX && movingX+movingSize <= targetX+targetSize && movingY == targetY) {
            return true;
        //} else
        // Bottom-left of moving touches top of target
        } else {
            return false;
        }
    }
}
