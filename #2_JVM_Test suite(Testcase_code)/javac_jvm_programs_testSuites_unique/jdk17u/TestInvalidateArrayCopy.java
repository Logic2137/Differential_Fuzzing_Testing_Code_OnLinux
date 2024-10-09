
package gc.g1;

public class TestInvalidateArrayCopy {

    static final int NumIterations = 1000000;

    static Object[] sourceArray = new Object[10];

    public static void main(String[] args) {
        for (int i = 0; i < NumIterations; i++) {
            Object[] x = new Object[0];
            if (i % (NumIterations / 10) == 0) {
                System.out.println(x);
            }
            System.arraycopy(sourceArray, 0, x, 0, Math.min(x.length, sourceArray.length));
        }
    }
}
