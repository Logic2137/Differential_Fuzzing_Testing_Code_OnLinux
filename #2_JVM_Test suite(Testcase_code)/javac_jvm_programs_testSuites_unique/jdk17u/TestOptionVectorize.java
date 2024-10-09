
package compiler.vectorization;

import java.util.stream.IntStream;

public class TestOptionVectorize {

    static final int RANGE = 512;

    static final int ITER = 100;

    static void init(double[] data) {
        IntStream.range(0, RANGE).parallel().forEach(j -> {
            data[j] = j + 1;
        });
    }

    static void test(double[] data, double A, double B) {
        for (int i = RANGE - 1; i > 0; i--) {
            for (int j = 0; j <= i - 1; j++) {
                data[j] = A * data[j + 1] + B * data[j];
            }
        }
    }

    static void verify(double[] data, double[] gold) {
        for (int i = 0; i < RANGE; i++) {
            if (data[i] != gold[i]) {
                throw new RuntimeException(" Invalid result: data[" + i + "]: " + data[i] + " != " + gold[i]);
            }
        }
    }

    public static void main(String[] args) {
        double[] data = new double[RANGE];
        double[] gold = new double[RANGE];
        System.out.println(" Run test ...");
        init(gold);
        test(gold, 1.0, 2.0);
        for (int i = 0; i < ITER; i++) {
            init(data);
            test(data, 1.0, 2.0);
        }
        verify(data, gold);
        System.out.println(" Finished test.");
    }
}
