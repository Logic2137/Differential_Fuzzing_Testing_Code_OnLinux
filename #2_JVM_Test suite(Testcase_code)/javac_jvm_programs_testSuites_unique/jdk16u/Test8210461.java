



package compiler.intrinsics.math;

import java.util.Arrays;

public class Test8210461 {
    private static final double[] testCases = new double[] {
        1647100.0d,
        16471000.0d,
        164710000.0d
    };

    public static void main(String[] args) {
        Arrays.stream(testCases).forEach(Test8210461::test);
    }

    private static void test(double arg) {
        double strictResult = StrictMath.cos(arg);
        double mathResult = Math.cos(arg);
        if (Math.abs(strictResult - mathResult) > Math.ulp(strictResult))
            throw new AssertionError(mathResult + " while expecting " + strictResult);
    }
}
