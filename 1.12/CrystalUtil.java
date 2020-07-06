import org.lwjgl.util.vector.Quaternion;

public class CrystalUtil {
    // Rotation of individual cubelets
    public static Quaternion[] cubeletStatus = {
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion()
    };

    // Get ID of cublet by position
    public static int[][][] cubletLookup = {
            // x
            {
                    //y
                    {17,9,0},
                    {20,16,3},
                    {23,15,6}
            },
            {
                    //y
                    {18,10,1},
                    {21,-1,4},
                    {24,14,7}
            },
            {
                    //y
                    {19,11,2},
                    {22,12,5},
                    {25,13,8}
            }
    };

    // Get cublet IDs of a side
    public static int[][] cubeSides = {
            // front
            {0,1,2,3,4,5,6,7,8},
            // back
            {19,18,17,22,21,20,25,24,23},
            // top
            {0,1,2,9,10,11,17,18,19},
            // bottom
            {23,24,25,15,14,13,6,7,8},
            // left
            {17,9,0,20,16,3,23,15,6},
            // right
            {2,11,19,5,12,22,8,13,25}
    };

    // Transformations of cube sides
    public static int[][] cubeSideTransforms = {
            {0,0,1},
            {0,0,-1},
            {0,1,0},
            {0,-1,0},
            {-1,0,0},
            {1,0,0}
    };

    // extra method from different class
    public static double easeInOutCubic (double t) {
        return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;
    }
}
