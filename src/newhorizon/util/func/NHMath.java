package newhorizon.util.func;


/**
 * {@link arc.math.Mathf}
 */
public class NHMath{
    public static final float PI = 3.1415927f, pi = PI, halfPi = PI/2;

    public static final float radiansToDegrees = 180f / PI;
    public static final float degreesToRadians = PI / 180;

    private static final int asinBits = 14; // 16KB. Adjust for accuracy.
    private static final int asinMask = ~(-1 << asinBits);
    private static final int asinCount = asinMask + 1;
    private static final float[] asinTable = new float[asinCount];
    private static final float radFull = PI * 2;
    private static final float sinToIndex = asinCount / 2f;

    //[-1, 1] - +1 -> [0, 2]
    static{
        for(int i = 0; i < asinCount; i++)
            asinTable[i] = (float)(Math.asin((i + 0.5f) / asinCount * 2 - 1) + radFull);

        asinTable[0] = radFull - halfPi;
        asinTable[asinTable.length - 1] = radFull + halfPi;
        asinTable[index(1.5f)] = halfPi + radFull;
        asinTable[index(0.5f)] = pi + halfPi + radFull;
    }

    public static int index(float sin){
        return (int)((sin + 1) * sinToIndex) & asinMask;
    }

    public static float acosRad(float cos){
        return asinTable[index((float)Math.sqrt(1 - cos * cos))];
    }

    public static float asinDeg(float sin){
        return asinTable[index(sin)] * radiansToDegrees;
    }
}

