
package compiler.runtime.safepoints;

public class TestRegisterRestoring {

    public static void main(String[] args) throws Exception {
        float[] array = new float[100];
        for (int i = 0; i < array.length; ++i) {
            array[i] = 0;
        }
        for (int j = 0; j < 20_000; ++j) {
            increment(array);
            for (int i = 0; i < array.length; i++) {
                if (array[i] != 10_000) {
                    throw new RuntimeException("Test failed: array[" + i + "] = " + array[i] + " but should be 10.000");
                }
                array[i] = 0;
            }
        }
    }

    static void increment(float[] array) {
        for (long l = 0; l < 10_000; l++) {
            for (int i = 0; i < array.length; ++i) {
                array[i] += 1;
            }
        }
    }
}
