
package org.openjdk.bench.java.util.stream.tasks.IntegerDuplicate;

import java.util.Random;

public class IntegerDuplicateProblem {

    private static final int DATA_SIZE = Integer.getInteger("bench.problemSize", 10 * 1024 * 1024);

    private final Integer[] data = new Integer[DATA_SIZE];

    public IntegerDuplicateProblem() {
        Random rand = new Random(0x30052012);
        for (int i = 0; i < data.length; i++) {
            data[i] = rand.nextInt();
        }
    }

    public Integer[] get() {
        return data;
    }
}
