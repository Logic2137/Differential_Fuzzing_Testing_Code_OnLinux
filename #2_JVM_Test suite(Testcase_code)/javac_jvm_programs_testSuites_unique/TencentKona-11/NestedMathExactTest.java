



package compiler.intrinsics.mathexact;

public class NestedMathExactTest {
    public static final int LIMIT = 100;
    public static int[] result = new int[LIMIT];
    public static int value = 17;

    public static void main(String[] args) {
        for (int i = 0; i < 100; ++i) {
            result[i] = runTest();
        }
    }

    public static int runTest() {
        int sum = 0;
        for (int j = 0; j < 100000; j = Math.addExact(j, 1)) {
            sum = 1;
            for (int i = 0; i < 5; ++i) {
                sum *= value;
            }
        }
        return sum;
    }
}
