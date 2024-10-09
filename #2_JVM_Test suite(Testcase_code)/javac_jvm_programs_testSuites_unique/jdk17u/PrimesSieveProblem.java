
package org.openjdk.bench.java.util.stream.tasks.PrimesSieve;

public class PrimesSieveProblem {

    private static final int DATA_SIZE = Integer.getInteger("bench.problemSize", 10 * 1024);

    private final Integer[] data = new Integer[DATA_SIZE];

    public PrimesSieveProblem() {
        for (int i = 0; i < data.length; i++) {
            data[i] = i;
        }
    }

    public Integer[] get() {
        return data;
    }

    public static boolean isNotDivisible(int i, int d) {
        return (i % d) != 0;
    }
}
