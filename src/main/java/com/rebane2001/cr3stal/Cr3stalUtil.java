package com.rebane2001.cr3stal;


import net.minecraft.util.math.Quaternion;


public final class Cr3stalUtil {
    // Rotation of individual cubelets
    public static final Quaternion[] CUBELET_STATUS = {
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1),
            new Quaternion(0, 0, 0, 1)
    };
    
    // Get ID of cublet by position
    public static final int[][][] CUBLET_LOOKUP = {
            // x axis
            {       // y axis
                    { 17, 9, 0 }, { 20, 16, 3 }, { 23, 15, 6 }
            },
            {       // y axis
                    { 18, 10, 1 }, { 21, -1, 4 }, { 24, 14, 7 }
            },
            {       // y axis
                    { 19, 11, 2 }, { 22, 12, 5 }, { 25, 13, 8 }
            }
    };
    
    // Get cublet IDs of a side
    public static final int[][] CUBE_SIDES = {
            // front
            { 0, 1, 2, 3, 4, 5, 6, 7, 8 },
            // back
            { 19, 18, 17, 22, 21, 20, 25, 24, 23 },
            // top
            { 0, 1, 2, 9, 10, 11, 17, 18, 19 },
            // bottom
            { 23, 24, 25, 15, 14, 13, 6, 7, 8 },
            // left
            { 17, 9, 0, 20, 16, 3, 23, 15, 6 },
            // right
            { 2, 11, 19, 5, 12, 22, 8, 13, 25 }
    };
    
    // Transformations of cube sides
    public static final int[][] CUBE_SIDE_TRANSFORMS = {
            { 0, 0, 1 },
            { 0, 0, -1 },
            { 0, 1, 0 },
            { 0, -1, 0 },
            { -1, 0, 0 },
            { 1, 0, 0 }
    };
    
    private Cr3stalUtil() {
    }
    
    public static double easeInOutCubic(double t) {
        return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;
    }
}
